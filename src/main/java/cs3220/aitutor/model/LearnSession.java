package cs3220.aitutor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "learn_sessions")
public class LearnSession {

    // ======================================================
    // One turn in the conversation, stored as an element collection
    // ======================================================
    @Embeddable
    public static class Turn {

        private String role;        // "user" or "bot"

        @Lob
        @Column(columnDefinition = "CLOB")
        private String content;

        private LocalDateTime timestamp;

        public Turn() {
        }

        public Turn(String role, String content, LocalDateTime timestamp) {
            this.role = role;
            this.content = content;
            this.timestamp = timestamp;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }

    // ======================================================
    // Basic session metadata
    // ======================================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    private String title;
    private String topic;
    private String gradeLevel;
    private LocalDateTime createdAt;

    // stored in separate table learn_session_turns
    @ElementCollection
    @CollectionTable(
            name = "learn_session_turns",
            joinColumns = @JoinColumn(name = "session_id")
    )
    private List<Turn> turns = new ArrayList<>();

    // ======================================================
    // Step E — NEW metadata
    // ======================================================
    private String mode;        // "teacher" | "game"
    private String difficulty;  // "guided" | "normal" | "easy" | "hard"

    // 🔹 NEW: Persona used in teacher mode
    private String persona;     // "coach" | "wizard" | "space" | etc.

    // ======================================================
    // Guided Mode state
    // ======================================================
    private Integer stepIndex;
    private String currentProblem;

    @ElementCollection
    @CollectionTable(
            name = "learn_session_steps",
            joinColumns = @JoinColumn(name = "session_id")
    )
    @Column(name = "step_text")
    private List<String> steps = new ArrayList<>();

    // ======================================================
    // Constructors
    // ======================================================
    public LearnSession() {
    }

    // New constructor without id (JPA will generate the id)
    public LearnSession(String username,
                        String title,
                        String topic,
                        String gradeLevel,
                        LocalDateTime createdAt) {
        this.username = username;
        this.title = title;
        this.topic = topic;
        this.gradeLevel = gradeLevel;
        this.createdAt = createdAt;
    }

    // ======================================================
    // Getters / Setters
    // ======================================================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {   // needed by JPA
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) { // JPA + flexibility
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { // optional but nice to have
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) { // for JPA
        this.createdAt = createdAt;
    }

    public List<Turn> getTurns() {
        return turns;
    }

    public void setTurns(List<Turn> turns) {
        this.turns = turns;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    // ======================================================
    // 🔹 Persona getter/setter
    // ======================================================
    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    // ======================================================
    // Add conversation turn
    // ======================================================
    public void addTurn(String role, String content) {
        turns.add(new Turn(role, content, LocalDateTime.now()));
    }

    // ======================================================
    // Guided Mode fields
    // ======================================================
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


