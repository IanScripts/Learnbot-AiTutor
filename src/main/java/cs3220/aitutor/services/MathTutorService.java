package cs3220.aitutor.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class MathTutorService {

    private final ChatClient chatClient;

    public MathTutorService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String getTutoringReply(String userMessage, String gradeLevel, String topic) {

        String systemInstructions = """
                You are LearnBot, a friendly math tutor for young children.

                Rules:
                - Explain step by step using simple language.
                - Focus only on math appropriate for the student's grade.
                - Encourage the student and be positive.
                - Ask one follow-up question sometimes to check understanding.
                - If the student asks about something non-math, gently redirect back to math.
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
}

