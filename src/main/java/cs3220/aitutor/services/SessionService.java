package cs3220.aitutor.services;

import cs3220.aitutor.model.LearnSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private final Map<Long, LearnSession> sessions = new ConcurrentHashMap<>();

    // used somewhere else in your file (not shown in the snippet you pasted)
    private final AtomicLong idSequence = new AtomicLong(1);

    // counter for "Math Mission #X"
    private final AtomicLong missionCounter = new AtomicLong(1);

    /** Create a new session for the given user. */
    public LearnSession createSession(String username,
                                      String title,
                                      String topic,
                                      String gradeLevel) {
        Long id = idSequence.getAndIncrement();

        // --- NEW: generate a simple, sweet title ---
        String cuteTitle;
        if (topic != null
                && !topic.isBlank()
                && !"Welcome".equalsIgnoreCase(topic)) {
            // e.g. "Math Mission – Fractions"
            cuteTitle = "Math Mission \u2013 " + topic;
        } else {
            // fallback: Math Mission #1, #2, ...
            long missionNumber = missionCounter.getAndIncrement();
            cuteTitle = "Math Mission #" + missionNumber;
        }

        LearnSession session = new LearnSession(
                id,
                username,
                cuteTitle,   // use our generated title
                topic,
                gradeLevel,
                LocalDateTime.now()
        );
        sessions.put(id, session);
        return session;
    }

    /** Delete a session, but only if it belongs to this user. */
    public boolean deleteSessionForUser(long id, String username) {
        LearnSession s = sessions.get(id);
        if (s == null || !Objects.equals(s.getUsername(), username)) {
            return false;
        }
        sessions.remove(id);
        return true;
    }

    /** Find a session by id, but only if it belongs to this user. */
    public Optional<LearnSession> findByIdForUser(long id, String username) {
        LearnSession s = sessions.get(id);
        if (s == null || !Objects.equals(s.getUsername(), username)) {
            return Optional.empty();
        }
        return Optional.of(s);
    }

    /** Add a turn (user or bot) to a session. Safe if session doesn't exist. */
    public void addTurn(Long sessionId, String role, String content) {
        LearnSession s = sessions.get(sessionId);
        if (s != null) {
            s.addTurn(role, content);
        }
    }

    /** List all sessions for a specific user, newest first. */
    public List<LearnSession> listSessionsForUser(String username) {
        return sessions.values().stream()
                .filter(s -> Objects.equals(s.getUsername(), username))
                .sorted(Comparator.comparing(LearnSession::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    /** Build a short text summary (used by your SessionSummaryDto). */
    public String buildSummary(LearnSession s) {
        int totalTurns = s.getTurns().size();
        long userTurns = s.getTurns().stream()
                .filter(t -> "user".equalsIgnoreCase(t.getRole()))
                .count();
        long botTurns = totalTurns - userTurns;

        return String.format(
                "%d messages (%d from you, %d from LearnBot).",
                totalTurns, userTurns, botTurns
        );
    }
}

