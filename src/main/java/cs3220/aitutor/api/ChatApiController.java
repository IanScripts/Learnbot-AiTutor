package cs3220.aitutor.api;

import cs3220.aitutor.model.LearnSession;
import cs3220.aitutor.services.MathTutorService;
import cs3220.aitutor.services.SessionService;
import cs3220.aitutor.services.UserContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// 🔹 now includes stepByStep
record ChatRequest(String message, String gradeLevel, String topic, Long sessionId, Boolean stepByStep) {}

record ChatResponseDto(String reply, Long sessionId) {}

record SessionSummaryDto(
        Long id,
        String title,
        String topic,
        String createdAt,
        String gradeLevel,
        String summary
) {}

record SessionDetailDto(
        Long id,
        String title,
        String topic,
        String createdAt,
        String gradeLevel,
        List<LearnSession.Turn> turns
) {}

// 🔹 request type for the welcome endpoint (now also includes stepByStep)
record WelcomeRequest(String gradeLevel, String topic, Boolean stepByStep) {}

@RestController
@RequestMapping("/api")
public class ChatApiController {

    private final MathTutorService mathTutorService;
    private final SessionService sessionService;
    private final UserContext userContext;

    public ChatApiController(MathTutorService mathTutorService,
                             SessionService sessionService,
                             UserContext userContext) {
        this.mathTutorService = mathTutorService;
        this.sessionService = sessionService;
        this.userContext = userContext;
    }

    // 🔹 welcome endpoint used when a chat starts
    @PostMapping("/chat-welcome")
    public ChatResponseDto chatWelcome(@RequestBody WelcomeRequest request) {

        String username = userContext.getCurrentUsername();
        if (username == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User must be logged in to chat."
            );
        }

        String topic = (request.topic() == null || request.topic().isBlank())
                ? "Welcome"
                : request.topic();

        String gradeLevel = (request.gradeLevel() == null || request.gradeLevel().isBlank())
                ? "1st grade"
                : request.gradeLevel();

        boolean stepByStep = request.stepByStep() != null ? request.stepByStep() : true;

        // Create a brand-new session for this user
        String title = topic;
        LearnSession session = sessionService.createSession(username, title, topic, gradeLevel);

        // Internal prompt: user never sees this — it is NOT stored as a user turn
        String styleInstruction = stepByStep
                ? "Explain ideas using short, clear, numbered steps that match the student's grade level."
                : "Give clear but concise explanations that match the student's grade level.";

        String internalPrompt =
                "Please introduce yourself as LearnBot, a friendly " + gradeLevel + " math tutor, " +
                        "and invite the student to ask a math question. " +
                        styleInstruction;

        // Ask the AI using the internal prompt
        String reply = mathTutorService.getTutoringReply(internalPrompt, gradeLevel, topic);

        // Only store the bot greeting so the transcript starts clean
        sessionService.addTurn(session.getId(), "bot", reply);

        return new ChatResponseDto(reply, session.getId());
    }

    @PostMapping("/chat")
    public ChatResponseDto chat(@RequestBody ChatRequest request) {

        String username = userContext.getCurrentUsername();
        if (username == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User must be logged in to chat."
            );
        }

        String topic = (request.topic() == null || request.topic().isBlank())
                ? "General math practice"
                : request.topic();

        String gradeLevel = (request.gradeLevel() == null || request.gradeLevel().isBlank())
                ? "1st grade"
                : request.gradeLevel();

        boolean stepByStep = request.stepByStep() != null ? request.stepByStep() : true;

        LearnSession session;
        Long sessionId = request.sessionId();

        if (sessionId == null) {
            // NEW session for this user
            String title = topic;
            session = sessionService.createSession(username, title, topic, gradeLevel);
        } else {
            // Existing session – verify it belongs to this user
            session = sessionService.findByIdForUser(sessionId, username)
                    .orElseGet(() -> sessionService.createSession(username, topic, topic, gradeLevel));
        }

        // store the raw user message (not the augmented one)
        sessionService.addTurn(session.getId(), "user", request.message());

        // add tutoring style instructions when step-by-step mode is on
        String userContent = request.message();
        if (stepByStep) {
            userContent =
                    "Explain the solution in short, clear, numbered steps appropriate for "
                            + gradeLevel + ". Then answer this question:\n\n"
                            + request.message();
        }

        String reply = mathTutorService.getTutoringReply(
                userContent,
                gradeLevel,
                topic
        );

        // store bot turn
        sessionService.addTurn(session.getId(), "bot", reply);

        return new ChatResponseDto(reply, session.getId());
    }

    @GetMapping("/sessions")
    public List<SessionSummaryDto> listSessions() {
        String username = userContext.getCurrentUsername();
        if (username == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User must be logged in to list sessions."
            );
        }

        return sessionService.listSessionsForUser(username).stream()
                .map(s -> new SessionSummaryDto(
                        s.getId(),
                        s.getTitle(),
                        s.getTopic(),
                        s.getCreatedAt().toString(),
                        s.getGradeLevel(),
                        sessionService.buildSummary(s)
                ))
                .toList();
    }

    @GetMapping("/sessions/{id}")
    public SessionDetailDto getSession(@PathVariable long id) {
        String username = userContext.getCurrentUsername();
        if (username == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User must be logged in to view a session."
            );
        }

        LearnSession s = sessionService.findByIdForUser(id, username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Session not found for this user."
                ));

        return new SessionDetailDto(
                s.getId(),
                s.getTitle(),
                s.getTopic(),
                s.getCreatedAt().toString(),
                s.getGradeLevel(),
                s.getTurns()
        );
    }

    @DeleteMapping("/sessions/{id}")
    public void deleteSession(@PathVariable long id) {
        String username = userContext.getCurrentUsername();
        if (username == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User must be logged in to delete a session."
            );
        }

        boolean deleted = sessionService.deleteSessionForUser(id, username);
        if (!deleted) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Session not found for this user."
            );
        }
    }
}
