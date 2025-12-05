package cs3220.aitutor.api;

import cs3220.aitutor.model.LearnSession;
import cs3220.aitutor.services.MathTutorService;
import cs3220.aitutor.services.SessionService;
import cs3220.aitutor.services.UserContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

record GameModeStatsDto(long gameAttempts) {}

// now includes stepByStep, persona, miniLecture
record ChatRequest(
        String message,
        String gradeLevel,
        String topic,
        Long sessionId,
        Boolean stepByStep,
        String persona,
        Boolean miniLecture
) {}

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

// Welcome request now also carries persona
record WelcomeRequest(
        String gradeLevel,
        String topic,
        Boolean stepByStep,
        String persona
) {}

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

        boolean stepByStep = (request.stepByStep() == null) || request.stepByStep();
        String difficulty = stepByStep ? "guided" : "normal";

        String persona = request.persona(); // may be null, service will default

        LearnSession session = sessionService.createSession(
                username,
                topic,       // title
                topic,       // topic
                gradeLevel,
                "teacher",   // mode
                difficulty
        );

        // store persona choice in the session
        session.setPersona(persona);

        // This acts as the "user message" instructing the teacher how to greet
        String welcomePrompt =
                "Please introduce yourself as LearnBot, a friendly " + gradeLevel + " math tutor, " +
                        "and invite the student to ask a math question.";

        // Use Teacher Mode generator
        String reply = mathTutorService.generateTeacherReply(
                welcomePrompt,
                gradeLevel,
                topic,
                stepByStep,
                persona,
                false  // miniLecture
        );

        // Store bot greeting
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
                ? "General Math"
                : request.topic();

        String gradeLevel = (request.gradeLevel() == null || request.gradeLevel().isBlank())
                ? "1st grade"
                : request.gradeLevel();

        boolean stepByStep = (request.stepByStep() == null) || request.stepByStep();
        String difficulty = stepByStep ? "guided" : "normal";

        String persona = request.persona(); // "coach", "wizard", "space", etc.
        boolean miniLecture = Boolean.TRUE.equals(request.miniLecture());

        Long sessionId = request.sessionId();
        LearnSession session;

        if (sessionId == null) {

            session = sessionService.createSession(
                    username,
                    topic,
                    topic,
                    gradeLevel,
                    "teacher",
                    difficulty
            );
        } else {
            // Validate existing session
            session = sessionService.findByIdForUser(sessionId, username)
                    .orElseGet(() -> sessionService.createSession(
                            username,
                            topic,
                            topic,
                            gradeLevel,
                            "teacher",
                            difficulty
                    ));
        }

        // Update unified metadata each turn
        session.setTopic(topic);
        session.setGradeLevel(gradeLevel);
        session.setMode("teacher");
        session.setDifficulty(difficulty);
        session.setPersona(persona); // remember persona choice

        // Store USER message
        String rawMessage = request.message();
        String storedUserMessage =
                (miniLecture && (rawMessage == null || rawMessage.isBlank()))
                        ? "[Mini lecture requested]"
                        : rawMessage;
        sessionService.addTurn(session.getId(), "user", storedUserMessage);

        // Ask AI using Teacher Mode generator
        String reply = mathTutorService.generateTeacherReply(
                rawMessage,
                gradeLevel,
                topic,
                stepByStep,
                persona,
                miniLecture
        );

        // Store BOT message
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

    @GetMapping("/gamemode-stats")
    public GameModeStatsDto gameModeStats() {
        String username = userContext.getCurrentUsername();
        if (username == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User must be logged in to view game stats."
            );
        }

        long attempts = sessionService.countGameModeAttemptsForUser(username);
        return new GameModeStatsDto(attempts);
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

