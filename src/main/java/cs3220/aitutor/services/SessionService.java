package cs3220.aitutor.services;

import cs3220.aitutor.model.LearnSession;
import cs3220.aitutor.repositories.LearnSessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SessionService {

    private final LearnSessionRepository repository;

    public SessionService(LearnSessionRepository repository) {
        this.repository = repository;
    }

    public LearnSession createSession(
            String username,
            String title,
            String topic,
            String gradeLevel,
            String mode,         // "teacher" | "game"
            String difficulty    // "guided" | "normal" | "easy" | "hard"
    ) {
        LearnSession session = new LearnSession(
                username,
                title,
                topic,
                gradeLevel,
                LocalDateTime.now()
        );
        session.setMode(mode);
        session.setDifficulty(difficulty);

        // when saved, JPA assigns the ID
        return repository.save(session);
    }

    public void save(LearnSession session) {
        if (session == null) {
            return;
        }
        repository.save(session);
    }
    public void addTurn(Long sessionId, String role, String content) {
        if (sessionId == null) return;

        Optional<LearnSession> opt = repository.findById(sessionId);
        if (opt.isEmpty()) return;

        LearnSession s = opt.get();
        s.addTurn(role, content);
        repository.save(s);
    }
    public Optional<LearnSession> findByIdForUser(Long sessionId, String username) {
        if (sessionId == null || username == null) {
            return Optional.empty();
        }

        return repository.findById(sessionId)
                .filter(s -> Objects.equals(s.getUsername(), username));
    }
    public boolean deleteSessionForUser(Long sessionId, String username) {
        if (sessionId == null || username == null) return false;

        Optional<LearnSession> opt = repository.findById(sessionId);
        if (opt.isPresent() && Objects.equals(opt.get().getUsername(), username)) {
            repository.delete(opt.get());
            return true;
        }
        return false;
    }

    public List<LearnSession> listSessionsForUser(String username) {
        if (username == null) return List.of();
        return repository.findByUsernameAndModeOrderByCreatedAtDesc(username, "teacher");
    }
    public long countGameModeAttemptsForUser(String username) {
        if (username == null) {
            return 0L;
        }

        java.util.List<LearnSession> gameSessions =
                repository.findByUsernameAndModeOrderByCreatedAtDesc(username, "game");

        long total = 0L;
        for (LearnSession session : gameSessions) {
            if (session.getTurns() == null) continue;
            total += session.getTurns().stream()
                    .filter(t -> "user".equals(t.getRole()))
                    .count();
        }
        return total;
    }

    public String buildSummary(LearnSession session) {
        if (session.getTurns().isEmpty()) {
            return "(empty)";
        }

        // show first bot line as summary
        return session.getTurns().stream()
                .filter(t -> "bot".equals(t.getRole()))
                .map(LearnSession.Turn::getContent)
                .findFirst()
                .orElse("(no summary)");
    }
}
