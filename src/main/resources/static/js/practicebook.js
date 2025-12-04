console.log("practicebook.js loaded");

document.addEventListener("DOMContentLoaded", () => {
    loadProblems(6); // start with 6 problems

    const newSetBtn = document.getElementById("btn-new-set");
    if (newSetBtn) {
        newSetBtn.addEventListener("click", () => loadProblems(6));
    }
});

/* -----------------------------------------------------------
   ===  MULTIPLE CHOICE MODE — HELPERS & ICONS  ===
----------------------------------------------------------- */

function getChoiceIcon(index) {
    const icons = ["🔵", "🟡", "❤️", "💚"];
    return icons[index] || "✨";
}

function showMCSection() {
    document.getElementById("mc-section").style.display = "block";
    document.getElementById("problems-container").style.display = "none";
}

function exitMC() {
    document.getElementById("mc-section").style.display = "none";
    document.getElementById("problems-container").style.display = "flex";
}

/* -----------------------------------------------------------
   ===  START MC MODE  ===
----------------------------------------------------------- */
async function startMC(topic, grade) {
    const res = await fetch("/api/mc/start", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            topic: topic,
            gradeLevel: grade
        })
    });
    const data = await res.json();
    renderMCQuestion(data);
}

/* -----------------------------------------------------------
   ===  RENDER MC QUESTION  ===
----------------------------------------------------------- */
function renderMCQuestion(q) {
    showMCSection();

    const colors = ["mc-blue", "mc-yellow", "mc-pink", "mc-green"];

    document.getElementById("mc-question-box").innerHTML = `
        <h3 class="fw-bold mb-3">🤓 ${q.question}</h3>
    `;

    document.getElementById("mc-choices-box").innerHTML =
        q.choices.map((choice, i) => `
            <div class="mc-card ${colors[i]}"
                onclick="submitMC('${choice}', ${q.sessionId}, this)">
                ${getChoiceIcon(i)} ${choice}
            </div>
        `).join("");
}

/* -----------------------------------------------------------
   ===  SUBMIT MC ANSWER  ===
----------------------------------------------------------- */
async function submitMC(answer, sessionId, element) {

    // Prevent double-click
    document.querySelectorAll(".mc-card").forEach(btn => btn.style.pointerEvents = "none");

    const res = await fetch("/api/mc/answer", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            sessionId: sessionId,
            userAnswer: answer
        })
    });

    /* -----------------------------------------------------------
   === GUIDED (STEP-BY-STEP) MODE JS ===
----------------------------------------------------------- */

    async function startGuidedLesson(topic, grade) {
        const res = await fetch("/api/chat-step/start", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({ topic, grade })
        });

        const data = await res.json();
        renderGuidedMessage(data);
    }

    function renderGuidedMessage(data) {
        showMCSection(); // reuse MC section UI container

        document.getElementById("mc-question-box").innerHTML = `
        <h3 class="fw-bold mb-3">👣 ${data.botMessage}</h3>
    `;

        document.getElementById("mc-choices-box").innerHTML = `
        <div class="input-group">
            <input id="guided-input" type="text" class="form-control" placeholder="Your answer…" />
            <button class="btn btn-primary" onclick="submitGuided(${data.sessionId})">
                ➡️ Next
            </button>
        </div>
    `;
    }

    async function submitGuided(sessionId) {
        const msg = document.getElementById("guided-input").value.trim();
        if (!msg) return;

        const res = await fetch("/api/chat-step/next", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({ sessionId, userMessage: msg })
        });

        const data = await res.json();
        renderGuidedMessage(data);
    }



    const data = await res.json();

    // Correct
    if (data.correct) {
        element.classList.add("mc-correct");
        element.innerHTML = "⭐😊 " + element.innerHTML;

        setTimeout(() => {
            renderMCQuestion(data.next);
        }, 700);

    } else {
        // Wrong
        element.classList.add("mc-wrong");

        setTimeout(() => {
            alert("❌😅 Try again!\n\n" + data.explanation);
            renderMCQuestion(data.next);
        }, 600);
    }
}

/* -----------------------------------------------------------
   ===  PRACTICE PROBLEMS (your original code)  ===
----------------------------------------------------------- */

async function fetchProblem() {
    const res = await fetch("/api/practice/next?mode=add20");
    if (!res.ok) {
        throw new Error("Failed to fetch practice problem");
    }
    return await res.json(); // { question, correctAnswer }
}

async function loadProblems(count) {
    const container = document.getElementById("problems-container");
    if (!container) return;

    container.innerHTML = `
        <div class="col-12 text-center text-muted py-3">
            Loading practice problems...
        </div>
    `;

    const problemCards = [];

    try {
        for (let i = 0; i < count; i++) {
            const problem = await fetchProblem();
            problemCards.push(problem);
        }

        container.innerHTML = "";

        problemCards.forEach((problem, index) => {
            const col = createProblemCard(problem, index + 1);
            container.appendChild(col);
        });

    } catch (err) {
        console.error("Error loading problems:", err);
        container.innerHTML = `
            <div class="col-12 text-center text-danger py-3">
                Oops, I couldn't load your problems. Please try again.
            </div>
        `;
    }
}

function createProblemCard(problem, number) {
    const col = document.createElement("div");
    col.className = "col-md-6 col-lg-4";

    const card = document.createElement("div");
    card.className = "problem-card card h-100 p-3 d-flex flex-column";

    const title = document.createElement("h3");
    title.className = "h6 text-muted mb-1";
    title.textContent = `Problem #${number}`;

    const question = document.createElement("p");
    question.className = "fs-4 mb-2";
    question.textContent = problem.question;

    const inputGroup = document.createElement("div");
    inputGroup.className = "input-group mb-2";

    const input = document.createElement("input");
    input.type = "number";
    input.className = "form-control";
    input.placeholder = "Your answer";

    const checkBtn = document.createElement("button");
    checkBtn.type = "button";
    checkBtn.className = "btn btn-primary";
    checkBtn.textContent = "Check";

    inputGroup.appendChild(input);
    inputGroup.appendChild(checkBtn);

    const hintBtn = document.createElement("button");
    hintBtn.type = "button";
    hintBtn.className = "btn btn-outline-secondary btn-sm mt-1";
    hintBtn.textContent = "Need a hint? 💡";

    const feedback = document.createElement("div");
    feedback.className = "mt-2 small";

    const hintBox = document.createElement("div");
    hintBox.className = "mt-2 small text-muted";

    card.appendChild(title);
    card.appendChild(question);
    card.appendChild(inputGroup);
    card.appendChild(hintBtn);
    card.appendChild(feedback);
    card.appendChild(hintBox);

    // === Event handlers ===

    checkBtn.addEventListener("click", async () => {
        const answer = input.value.trim();
        if (!answer) {
            feedback.className = "mt-2 small text-danger";
            feedback.textContent = "Try typing an answer first!";
            return;
        }

        try {
            const res = await fetch("/api/practice/check", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    question: problem.question,
                    userAnswer: answer
                })
            });

            if (!res.ok) throw new Error("Failed to check answer");

            const data = await res.json(); // { correct, correctAnswer, stats }

            if (data.correct) {
                feedback.className = "mt-2 small text-success";
                feedback.textContent = `Nice job! ✅ ${problem.question.replace("?", "")} ${data.correctAnswer}.`;
            } else {
                feedback.className = "mt-2 small text-danger";
                feedback.textContent = `Not quite yet. Try again!`;
            }

            if (data.stats) updateStats(data.stats);

        } catch (err) {
            console.error("Error checking answer:", err);
            feedback.className = "mt-2 small text-danger";
            feedback.textContent = "Something went wrong. Please try again.";
        }
    });

    hintBtn.addEventListener("click", async () => {
        hintBox.className = "mt-2 small text-muted";
        hintBox.textContent = "Thinking of a hint... 💭";

        try {
            const res = await fetch("/api/practice/hint", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    problem: problem.question,
                    userAnswer: input.value.trim()
                })
            });

            if (!res.ok) throw new Error("Failed to get hint");

            const data = await res.json();
            hintBox.textContent = data.hintText;

        } catch (err) {
            console.error("Error getting hint:", err);
            hintBox.className = "mt-2 small text-danger";
            hintBox.textContent = "I couldn't get a hint right now. Please try again.";
        }
    });

    col.appendChild(card);
    return col;
}

function updateStats(stats) {
    const totalEl = document.getElementById("stats-total");
    const correctEl = document.getElementById("stats-correct");
    const accuracyEl = document.getElementById("stats-accuracy");
    const bestStreakEl = document.getElementById("stats-best-streak");

    if (!totalEl) return;

    totalEl.textContent = stats.totalAnswered ?? 0;
    correctEl.textContent = stats.totalCorrect ?? 0;
    bestStreakEl.textContent = stats.bestStreak ?? 0;

    if (stats.totalAnswered && stats.totalAnswered > 0) {
        const accuracy = Math.round((stats.totalCorrect / stats.totalAnswered) * 100);
        accuracyEl.textContent = accuracy + "%";
    } else {
        accuracyEl.textContent = "—";
    }
}

