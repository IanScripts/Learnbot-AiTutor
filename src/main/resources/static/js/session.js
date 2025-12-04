console.log("sessions.js loaded");

document.addEventListener("DOMContentLoaded", loadPastSessions);

// 🔹 helper that calls backend to delete a session, then remove from UI
async function deleteSession(id, listItem) {
    if (!confirm("Are you sure you want to delete this Math Mission?")) {
        return;
    }

    try {
        const res = await fetch(`/api/sessions/${id}`, {
            method: "DELETE"
        });

        if (!res.ok) {
            throw new Error("HTTP " + res.status);
        }

        // Remove the session from the UI
        if (listItem && listItem.parentElement) {
            listItem.parentElement.removeChild(listItem);
        }
    } catch (err) {
        console.error("Error deleting session:", err);
        alert("Sorry, I couldn't delete that session. Please try again.");
    }
}

async function loadPastSessions() {
    const list = document.getElementById("past-sessions");
    if (!list) {
        console.warn("No #past-sessions element found.");
        return;
    }

    // Show loading while we fetch
    list.innerHTML = `
        <li class="session-item">
            <span>Loading…</span>
        </li>
    `;

    try {
        const response = await fetch("/api/sessions", {
            headers: {
                "Accept": "application/json"
            }
        });

        console.log("GET /api/sessions -> status", response.status);

        if (!response.ok) {
            throw new Error("HTTP " + response.status);
        }

        const sessions = await response.json();
        console.log("sessions payload:", sessions);

        if (!Array.isArray(sessions) || sessions.length === 0) {
            list.innerHTML = `
                <li class="session-item">
                    <span>You don’t have any sessions yet. Try asking LearnBot a math question on the Home page!</span>
                </li>
            `;
            return;
        }

        list.innerHTML = "";

        sessions.forEach(s => {
            // Your record fields: id, title, topic, createdAt, gradeLevel, summary
            const li = document.createElement("li");
            li.className = "session-item";

            // ---- OPEN BUTTON ----
            const openBtn = document.createElement("button");
            openBtn.type = "button";
            openBtn.className = "session-pill";

            const title = s.title || s.topic || "Math practice";
            const created = s.createdAt || "";
            openBtn.textContent = created
                ? `${title} — ${created}`
                : title;

            // 👉 UPDATED: open Practice Book for this mission
            openBtn.addEventListener("click", () => {
                const sessionId = s.id;
                console.log("Opening session", sessionId);

                const topic = s.topic ? encodeURIComponent(s.topic) : "";
                const grade = s.gradeLevel ? encodeURIComponent(s.gradeLevel) : "";

                // /practicebook?sessionId=ID&topic=...&grade=...
                let url = `/practicebook?sessionId=${encodeURIComponent(sessionId)}`;
                if (topic) {
                    url += `&topic=${topic}`;
                }
                if (grade) {
                    url += `&grade=${grade}`;
                }

                window.location.href = url;
            });

            // ---- DELETE BUTTON ----
            const deleteBtn = document.createElement("button");
            deleteBtn.type = "button";
            deleteBtn.className = "session-delete-btn";
            deleteBtn.textContent = "Delete";

            deleteBtn.addEventListener("click", (event) => {
                // prevent click from also triggering the open button
                event.stopPropagation();
                event.preventDefault();
                deleteSession(s.id, li);
            });

            li.appendChild(openBtn);
            li.appendChild(deleteBtn);
            list.appendChild(li);
        });

    } catch (err) {
        console.error("Error loading sessions:", err);
        list.innerHTML = `
            <li class="session-item">
                <span>Sorry, I couldn’t load your sessions. Please try again later.</span>
            </li>
        `;
    }
}
