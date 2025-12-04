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

    // ================================================================
    // SHARED TUTORING FUNCTION (Simple Language + Child-Friendly Tone)
    // ================================================================
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

    // ================================================================
    // MULTIPLE CHOICE MODE
    // ================================================================

    public record MultipleChoiceProblem(String question, List<String> choices, String correctAnswer) {}
    public record AnswerResult(boolean correct, String explanation, String correctAnswer) {}

    private String lastCorrectAnswer = null;

    public MultipleChoiceProblem generateMultipleChoice(String topic, String grade) {

        String prompt = """
Generate a single multiple-choice math problem.
Grade level: %s.
Topic: %s.

Return ONLY JSON in this structure:
{
  "question": "...",
  "choices": ["A", "B", "C", "D"],
  "correct": "A"
}
""".formatted(grade, topic);

        String json = getTutoringReply(prompt, grade, topic);

        try {
            var mapper = new ObjectMapper();
            var node = mapper.readTree(json);

            String q = node.get("question").asText();
            List<String> choices = mapper.readValue(
                    node.get("choices").toString(),
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class)
            );
            String correct = node.get("correct").asText();

            this.lastCorrectAnswer = correct;
            return new MultipleChoiceProblem(q, choices, correct);

        } catch (Exception e) {
            return new MultipleChoiceProblem(
                    "Error generating question.",
                    List.of("1", "2", "3", "4"),
                    "1"
            );
        }
    }

    public AnswerResult checkMultipleChoiceAnswer(String userAnswer) {

        boolean correct = userAnswer.equalsIgnoreCase(lastCorrectAnswer);

        return new AnswerResult(
                correct,
                correct ? "Correct! Great job!" :
                        "Nice try! The right answer was: " + lastCorrectAnswer,
                lastCorrectAnswer
        );
    }

    // ================================================================
    // STEP-BY-STEP GUIDED MODE (Step 6)
    // ================================================================

    public record GuidedProblem(String problem, List<String> steps) {}

    /**
     * Generates a math problem AND breaks it into 2–4 small steps.
     */
    public GuidedProblem generateGuidedProblem(String topic, String grade) {

        String prompt = """
Create ONE grade-appropriate math problem for a young student.

Then break the solution into 2–4 VERY small steps.
Each step should be something the child can try.

Return ONLY JSON in this exact structure:
{
  "problem": "...",
  "steps": [
    "First do ...",
    "Next do ...",
    "Then ...",
    "Finally ..."
  ]
}
""";

        String json = getTutoringReply(prompt, grade, topic);

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
                            "Count forward 2 numbers.",
                            "Say the number you land on."
                    )
            );
        }
    }

    /**
     * Tutor checks the child's response to the current step.
     */
    public String checkGuidedStep(String problem, String step, String userAnswer) {

        String prompt = """
We are solving: %s
The current step is: %s
The student's response was: %s

Respond like a friendly tutor:
- Encourage the child.
- Say if they are right or close.
- If wrong, give a tiny nudge.
- Use simple language.
- Keep it SHORT (1–2 sentences).
""".formatted(problem, step, userAnswer);

        return getTutoringReply(prompt, null, null);
    }

}
