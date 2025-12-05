console.log("practicebook.js loaded");

let currentSessionId = null;
let currentGradeLevel = "1st grade";
let currentTopic = "";


const topicChoicesByGrade = {
    "1st grade": [
        "Counting to 100",
        "Addition within 20",
        "Subtraction within 20",
        "Word problems"
    ],
    "2nd grade": [
        "Addition within 100",
        "Subtraction within 100",
        "Measurement & time",
        "Word problems"
    ],
    "3rd grade": [
        "Multiplication basics",
        "Division basics",
        "Fractions (1/2, 1/3, 1/4)",
        "Area of rectangles"
    ],
    "4th grade": [
        "Multiplying 2-digit numbers",
        "Long division",
        "Fractions & mixed numbers",
        "Decimals (tenths & hundredths)"
    ],
    "5th grade": [
        "Adding & subtracting fractions",
        "Multiplying fractions",
        "Decimals",
        "Coordinate plane basics"
    ]
};

function renderTopicGrid(preselectedTopic) {
    const grid = document.getElementById("topic-grid");
    const topicInput = document.getElementById("topic-input");
    const gradeSelect = document.getElementById("grade-select");

    if (!grid || !topicInput || !gradeSelect) return;

    const grade = gradeSelect.value || currentGradeLevel;
    const topics = topicChoicesByGrade[grade] || [];

    grid.innerHTML = "";

    topics.forEach((topic, index) => {
        const btn = document.createElement("button");
        btn.type = "button";

        // colored topic pill
        btn.className = "topic-btn topic-color-" + (index % 4);
        btn.textContent = topic;

        // If this topic matches the current one, mark it as selected
        if (preselectedTopic && preselectedTopic === topic) {
            btn.classList.add("selected");
        }

        btn.addEventListener("click", () => {
            // Clear selection from all buttons first
            grid.querySelectorAll(".topic-btn").forEach(b =>
                b.classList.remove("selected")
            );

            btn.classList.add("selected");
            currentTopic = topic;
            topicInput.value = topic;

            // First question for this topic
            startGame();
        });

        grid.appendChild(btn);
    });

    // If we have a currentTopic that matches one of the buttons, mark it
    if (currentTopic) {
        grid.querySelectorAll(".topic-btn").forEach(b => {
            if (b.textContent === currentTopic) {
                b.classList.add("selected");
            }
        });
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const nextBtn = document.getElementById("next-btn");
    const gradeSelect = document.getElementById("grade-select");
    const topicInput = document.getElementById("topic-input");

    // Grade select init
    if (gradeSelect) {
        currentGradeLevel = gradeSelect.value || currentGradeLevel;

        // When grade changes, update and re-render topic grid
        gradeSelect.addEventListener("change", () => {
            currentGradeLevel = gradeSelect.value;
            currentTopic = "";
            if (topicInput) topicInput.value = "";
            renderTopicGrid(null);

            // Reset prompt, since they changed grade
            const questionText = document.getElementById("question-text");
            const feedback = document.getElementById("feedback-text");
            if (questionText) {
                questionText.textContent = "Pick a topic to get your first question!";
            }
            if (feedback) {
                feedback.textContent = "";
            }
        });
    }

    if (topicInput) {
        // keep state in sync if something ever changes the hidden input
        topicInput.addEventListener("input", () => {
            currentTopic = topicInput.value.trim();
        });
    }

    // Initial render of the 4-square grid
    renderTopicGrid(currentTopic);

    // NEXT QUESTION button: same topic & grade, new question
    if (nextBtn) {
        nextBtn.addEventListener("click", () => {
            startGame();
        });
    }
});


async function startGame() {
    const feedback = document.getElementById("feedback-text");
    const gradeSelect = document.getElementById("grade-select");

    if (gradeSelect) {
        currentGradeLevel = gradeSelect.value || currentGradeLevel;
    }

    // If no topic chosen yet, gently bail
    if (!currentTopic || currentTopic.trim().length === 0) {
        if (feedback) {
            feedback.textContent = "Pick a topic first to start your game!";
        }
        return;
    }

    if (feedback) {
        feedback.textContent = "Thinking of a good question for you...";
    }

    try {
        const res = await fetch("/api/mc/start", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                topic: currentTopic,
                gradeLevel: currentGradeLevel
            })
        });

        if (!res.ok) {
            throw new Error("HTTP " + res.status);
        }

        const data = await res.json();
        renderQuestion(data);
    } catch (err) {
        console.error("Error starting game mode:", err);
        if (feedback) {
            feedback.textContent = "Sorry, I couldn't start the game. Please try again.";
        }
    }
}


function renderQuestion(q) {
    const questionText = document.getElementById("question-text");
    const choicesDiv = document.getElementById("choices");
    const feedback = document.getElementById("feedback-text");

    if (!questionText || !choicesDiv) return;

    currentSessionId = q.sessionId;

    // Add emojis to question text
    const emojis = ["✏️", "⭐", "🧠", "🔢", "🎯", "🏆"];
    const emoji = emojis[Math.floor(Math.random() * emojis.length)];

    const decoratedQuestion = q.question
        ? `${q.question} ${emoji}`
        : "Let's practice some math! ✏️";

    questionText.textContent = decoratedQuestion;

    if (feedback) {
        feedback.textContent = "";
    }

    choicesDiv.innerHTML = "";

    if (!Array.isArray(q.choices) || q.choices.length === 0) {
        const msg = document.createElement("p");
        msg.className = "text-muted";
        msg.textContent = "I couldn't find answer choices. Please try again.";
        choicesDiv.appendChild(msg);
        return;
    }

    const letters = ["A", "B", "C", "D"];

    q.choices.forEach((choice, index) => {
        const btn = document.createElement("button");
        btn.type = "button";
        btn.className = "btn btn-outline-primary w-100 text-start";

        const letter = letters[index] || "";
        const label = `${letter}) ${choice}`;

        btn.textContent = label;

        // Send raw choice text to backend
        btn.addEventListener("click", () => submitAnswer(choice, btn));

        choicesDiv.appendChild(btn);
    });
}


async function submitAnswer(answer, buttonEl) {
    if (!currentSessionId) return;

    const feedback = document.getElementById("feedback-text");
    const allButtons = document.querySelectorAll("#choices button");
    allButtons.forEach(b => b.disabled = true);

    try {
        const res = await fetch("/api/mc/answer", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                sessionId: currentSessionId,
                userAnswer: answer
            })
        });

        if (!res.ok) {
            throw new Error("HTTP " + res.status);
        }

        const data = await res.json();

        if (feedback) {
            if (data.correct) {
                feedback.textContent = "✅ Nice job! " + (data.explanation || "");
            } else {
                feedback.textContent = "❌ Not quite. " + (data.explanation || "Let's try another one!");
            }
        }

        if (buttonEl) {
            buttonEl.classList.remove("btn-outline-primary");
            buttonEl.classList.add(data.correct ? "btn-success" : "btn-danger");
        }

        // If backend sends a next question automatically, still support it
        if (data.next) {
            setTimeout(() => {
                renderQuestion(data.next);
            }, 800);
        }
    } catch (err) {
        console.error("Error submitting answer:", err);
        if (feedback) {
            feedback.textContent = "Something went wrong. Please try again.";
        }
    } finally {
        allButtons.forEach(b => b.disabled = false);
    }
}







