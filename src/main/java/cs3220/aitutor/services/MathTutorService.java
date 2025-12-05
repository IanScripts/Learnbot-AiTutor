package cs3220.aitutor.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MathTutorService {

    private final ChatClient chatClient;

    public MathTutorService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String getTutoringReply(String userMessage, String gradeLevel, String topic) {

        String systemInstructions = """
You are LearnBot, a friendly math tutor for young children.

Your speaking style:
- Use very simple, child-friendly language.
- Keep sentences short (8–12 words).
- Avoid math jargon unless required.
- Use warm, encouraging tone.
- Stay positive and supportive.
- Keep explanations step-by-step.
- Only teach ONE idea at a time.
- If the student is confused, slow down.
- Use small helpful emojis (⭐😊➕✏️) but only 1–2 per message.
- Never overwhelm the student with long paragraphs.

Teaching rules:
1. Always match the difficulty to the student's grade level.
2. Explain with clear, concrete steps.
3. Ask small check-in questions to confirm understanding.
4. If the student answers incorrectly, gently guide them.
5. Redirect politely if they go off-topic.
6. Never give advanced math beyond the grade level.

Response format:
- Short intro sentence.
- A simple step or explanation.
- Optional: one helpful emoji.
- End with a tiny check-in question to keep them engaged.

Your goal: Make math easy, fun, and clear for young learners.
""";

        String context = """
Student grade level: %s
Current topic / mission: %s

Student message: %s
""".formatted(
                (gradeLevel == null || gradeLevel.isBlank()) ? "1st grade" : gradeLevel,
                (topic == null || topic.isBlank()) ? "general math practice" : topic,
                userMessage
        );

        return chatClient
                .prompt()
                .system(systemInstructions)
                .user(context)
                .call()
                .content();
    }

    public record MultipleChoiceProblem(String question, List<String> choices, String correctAnswer) {}
    public record AnswerResult(boolean correct, String explanation, String correctAnswer) {}

    private String lastCorrectAnswer = null;
    private String lastMcQuestion;

    public synchronized MultipleChoiceProblem generateMultipleChoice(String topic, String grade) {
        final int maxAttempts = 3; // how many times to try for a new question

        MultipleChoiceProblem lastAttempt = null;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            MultipleChoiceProblem problem = generateMultipleChoiceOnce(topic, grade);

            if (lastMcQuestion == null ||
                    problem.question() == null ||
                    !problem.question().equals(lastMcQuestion)) {

                lastMcQuestion = problem.question();
                return problem;
            }

            // otherwise remember and try again
            lastAttempt = problem;
        }
        if (lastAttempt != null) {
            lastMcQuestion = lastAttempt.question();
            return lastAttempt;
        }
        return generateMultipleChoiceOnce(topic, grade);
    }

    private MultipleChoiceProblem generateMultipleChoiceOnce(String topic, String grade) {

        String randomTag = java.util.UUID.randomUUID().toString();

        String prompt = """
You are a friendly elementary and middle-school math tutor.

Generate ONE multiple-choice math problem for a child.

Grade level: %s
Requested topic (may be blank or very general): %s

If the topic is blank or something like "General Math", YOU choose a math subject
that matches the grade. Rotate across:
- Grades 1–2: counting, addition, subtraction, place value, simple word problems.
- Grades 3–4: multiplication, division, multi-step word problems, basic fractions.
- Grades 5–6: fractions, decimals, percent, volume, area, word problems.
- Grades 7–8: ratios, proportional relationships, expressions, equations,
  geometry, negative numbers, word problems.

Keep numbers kid-friendly.

IMPORTANT:
- Each time you are called, create a NEW problem that is different from earlier ones.
- Vary the numbers, story, or structure so it doesn’t repeat exactly.

(For your use only, not shown to the student) Random tag: %s

Return ONLY JSON in this exact structure, with NO extra text:

{
  "question": "text of the question",
  "choices": ["answer1", "answer2", "answer3", "answer4"],
  "correct": "A"
}

Rules:
- "choices" must be exactly 4 answer texts (NO labels like "A) 100").
- "correct" must be just one letter: "A", "B", "C", or "D".
""".formatted(
                grade,
                topic == null ? "" : topic,
                randomTag
        );
        String json = chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            var node = mapper.readTree(json);

            String q = node.get("question").asText();

            java.util.List<String> choices = mapper.readValue(
                    node.get("choices").toString(),
                    mapper.getTypeFactory().constructCollectionType(java.util.List.class, String.class)
            );

            String correctRaw = node.get("correct").asText();
            String correctChoiceText = null;

            // Map letter A/B/C/D -> choice index 0/1/2/3
            if (correctRaw != null) {
                int idx = switch (correctRaw.trim().toUpperCase()) {
                    case "A" -> 0;
                    case "B" -> 1;
                    case "C" -> 2;
                    case "D" -> 3;
                    default -> -1;
                };
                if (idx >= 0 && idx < choices.size()) {
                    correctChoiceText = choices.get(idx);
                }
            }
            if (correctChoiceText == null && !choices.isEmpty()) {
                correctChoiceText = choices.get(0);
            }

            this.lastCorrectAnswer = correctChoiceText;

            return new MultipleChoiceProblem(q, choices, correctChoiceText);

        } catch (Exception e) {
            e.printStackTrace();
            this.lastCorrectAnswer = "1";
            return new MultipleChoiceProblem(
                    "Error generating question.",
                    java.util.List.of("1", "2", "3", "4"),
                    "1"
            );
        }
    }

    private String normalizeAnswer(String s) {
        if (s == null) return "";

        return s.trim().replaceAll("\\s+", " ").toLowerCase();
    }

    public AnswerResult checkMultipleChoiceAnswer(String userAnswer) {

        String ua = normalizeAnswer(userAnswer);
        String correctText = normalizeAnswer(lastCorrectAnswer);

        boolean correct = ua.equals(correctText);

        String explanation;
        if (correct) {
            explanation = "Correct! Great job!";
        } else {
            explanation = "Nice try! The right answer was: " + lastCorrectAnswer;
        }

        return new AnswerResult(
                correct,
                explanation,
                lastCorrectAnswer
        );
    }

    public record GuidedProblem(String problem, List<String> steps) {}

    public GuidedProblem generateGuidedProblem(String topic, String grade) {

        String safeTopic = (topic == null || topic.isBlank()) ? "General Math" : topic;
        String safeGrade = (grade == null || grade.isBlank()) ? "1st grade" : grade;

        String prompt = """
You are LearnBot, a friendly math tutor.

Create ONE math problem and break it into small teaching steps.

Grade level: %s
Topic: %s

Return ONLY JSON in this structure, no extra text:

{
  "problem": "full text of the problem",
  "steps": [
    "Short first step in kid-friendly language.",
    "Short next step.",
    "Another small step.",
    "Final step or wrap-up."
  ]
}

Rules:
- Use simple language for children.
- 3–6 steps total.
- Each step should be short and clear.
""".formatted(safeGrade, safeTopic);

        String json = chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();

        try {
            var mapper = new ObjectMapper();
            var node = mapper.readTree(json);

            String problem = node.get("problem").asText();

            List<String> steps = mapper.readValue(
                    node.get("steps").toString(),
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class)
            );

            return new GuidedProblem(problem, steps);

        } catch (Exception e) {
            return new GuidedProblem(
                    "What is 3 + 2?",
                    List.of(
                            "Start with 3.",
                            "Count up 2 more.",
                            "3 plus 2 makes 5."
                    )
            );
        }
    }

    public String checkGuidedStep(String problem, String step, String userAnswer) {

        String prompt = """
You are LearnBot, a friendly math tutor for kids.

You are checking how a child answered ONE step of a math problem.

Problem:
%s

Current teaching step:
%s

The student's response was:
%s

Respond like a friendly tutor:
- Encourage the child.
- Say if they are right or close.
- If wrong, give a tiny nudge.
- Use simple language.
- Keep it SHORT (1–2 sentences).
""".formatted(problem, step, userAnswer);

        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }

    public String generateTeacherReply(
            String userMessage,
            String gradeLevel,
            String topic,
            boolean stepByStep,
            String persona,
            boolean miniLecture
    ) {
        String safeGrade = (gradeLevel == null || gradeLevel.isBlank()) ? "1st grade" : gradeLevel;
        String safeTopic = (topic == null || topic.isBlank()) ? "general math practice" : topic;
        String safeMessage = (userMessage == null) ? "" : userMessage.trim();

        // 1) Persona style
        String personaStyle = switch (persona == null ? "" : persona) {
            case "wizard" -> """
                    You are a playful math wizard teacher.
                    You use magic, potions, spells, and fantasy metaphors,
                    but you still give correct math with clear steps.
                    Speak with excitement and a sense of wonder.
                    """;
            case "space" -> """
                    You are an excited space explorer math teacher.
                    You explain math using rockets, planets, galaxies, and space adventures,
                    but you still keep the math correct and clear.
                    Speak like a mission leader guiding a young astronaut.
                    """;
            default -> """
                    You are a kind, friendly math coach.
                    You speak clearly, encourage the student, and avoid scary language.
                    Use simple, child-friendly words and a warm, supportive tone.
                    """;
        };

        String modeInstruction;
        if (miniLecture) {
            // MINI-LECTURE MODE: ignore detailed user text, base on grade + topic
            modeInstruction = """
                    Give a SHORT mini-lesson for a %s student about the topic "%s".

                    Structure your answer like this:

                    1) A fun title on the first line.
                    2) A one-sentence goal for the lesson.
                    3) 3 short bullet points with the most important ideas.
                    4) One worked example, shown step by step.
                    5) One quick practice question for the student to try.

                    Keep the whole answer under about 10 sentences.
                    Use simple words for this grade level.
                    Do NOT ask follow-up questions at the end; just give the practice question.
                    """.formatted(safeGrade, safeTopic);
        } else if (stepByStep) {
            modeInstruction = """
                    Help the student with their specific question in a step-by-step way.
                    Ask small check-in questions and wait for the student's reply.
                    Use language that matches a %s student learning "%s".
                    Keep each step short, clear, and child-friendly.
                    """.formatted(safeGrade, safeTopic);
        } else {
            modeInstruction = """
                    Answer the student's question clearly for a %s student about "%s".
                    Keep the explanation focused, friendly, and not too long.
                    Avoid advanced math that is above this grade level.
                    """.formatted(safeGrade, safeTopic);
        }

        // 3) Final prompt
        String prompt = """
                You are LearnBot, an AI math teacher for children.

                %s

                %s

                Student grade level: %s
                Topic: %s

                Student's message or question:
                %s
                """.formatted(
                personaStyle,
                modeInstruction,
                safeGrade,
                safeTopic,
                safeMessage
        );

        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }
}




