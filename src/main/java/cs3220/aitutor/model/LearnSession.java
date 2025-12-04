package cs3220.aitutor.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LearnSession {

    // One turn in the conversation
    public static class Turn {
        private final String role;        // "user" or "bot"
        private final String content;
        private final LocalDateTime timestamp;

        public Turn(String role, String content, LocalDateTime timestamp) {
            this.role = role;
            this.content = content;
            this.timestamp = timestamp;
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }

    private Long id;
    private String username;
    private String title;
    private String topic;
    private String gradeLevel;
    private LocalDateTime createdAt;

    private final List<Turn> turns = new ArrayList<>();

    // ============================================
    // Step-by-Step Guided Mode State
    // ============================================
    private Integer stepIndex;                     // which step student is on
    private String currentProblem;                 // main problem text
    private List<String> steps = new ArrayList<>(); // small guided steps

    public LearnSession(Long id,
                        String username,
                        String title,
                        String topic,
                        String gradeLevel,
                        LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.topic = topic;
        this.gradeLevel = gradeLevel;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getTopic() {
        return topic;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Turn> getTurns() {
        return turns;
    }

    /** Add a new conversation turn (user or bot). */
    public void addTurn(String role, String content) {
        turns.add(new Turn(role, content, LocalDateTime.now()));
    }

    // ============================================
    // Guided Mode State Getters/Setters
    // ============================================

    public Integer getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(Integer stepIndex) {
        this.stepIndex = stepIndex;
    }

    public String getCurrentProblem() {
        return currentProblem;
    }

    public void setCurrentProblem(String currentProblem) {
        this.currentProblem = currentProblem;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }
}
