let currentSessionId = null;
let currentTopic = null;
let currentGradeLevel = '1st grade';
let stepByStepMode = true; // default: ON

/**
 * Map of grade levels to available math topics.
 */
const gradeTopics = {
    '1st grade': [
        'Counting to 100',
        'Addition within 20',
        'Subtraction within 20',
        'Place value (ones & tens)'
    ],
    '2nd grade': [
        'Addition within 100',
        'Subtraction within 100',
        'Word problems',
        'Measurement (length & time)'
    ],
    '3rd grade': [
        'Multiplication basics',
        'Division basics',
        'Fractions (1/2, 1/3, 1/4)',
        'Area of rectangles'
    ],
    '4th grade': [
        'Multiplying 2-digit numbers',
        'Long division',
        'Fractions & mixed numbers',
        'Decimals (tenths & hundredths)'
    ],
    '5th grade': [
        'Adding & subtracting fractions',
        'Multiplying fractions',
        'Decimals',
        'Coordinate plane basics'
    ]
};

/**
 * Append a message bubble to the chat window.
 * @param {string} text
 * @param {'user'|'bot'} sender
 */
function appendMessage(text, sender) {
    const chatWindow = document.getElementById('chat-window');
    if (!chatWindow) return;

    const bubble = document.createElement('div');
    bubble.classList.add('chat-message');
    if (sender === 'user') {
        bubble.classList.add('user-message');
    } else {
        bubble.classList.add('bot-message');
    }

    // Simple newline handling
    text.split('\n').forEach((line, index) => {
        if (index > 0) {
            bubble.appendChild(document.createElement('br'));
        }
        bubble.appendChild(document.createTextNode(line));
    });

    chatWindow.appendChild(bubble);
    chatWindow.scrollTop = chatWindow.scrollHeight;
}

/**
 * Read query string param from URL.
 */
function getQueryParam(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

/**
 * Automatically start a short, kid-friendly practice question when a topic is chosen.
 * This uses the same /api/chat endpoint as normal user messages, but sends a
 * pre-built prompt so students don't have to type anything to get started.
 */
async function autoStartTopicPractice(topic) {
    const chatWindow = document.getElementById('chat-window');
    if (!chatWindow) return;

    const loadingBubble = document.createElement('div');
    loadingBubble.classList.add('chat-message', 'bot-message');
    loadingBubble.textContent = "Let me think of a good practice question...";
    chatWindow.appendChild(loadingBubble);
    chatWindow.scrollTop = chatWindow.scrollHeight;

    const autoPrompt = `Please start a fun, kid-friendly ${currentGradeLevel} math practice on "${topic}". ` +
        `Ask one clear practice question first and then wait for the student's answer. ` +
        `Use simple language, and when step-by-step mode is ON, be ready to explain the solution in numbered steps.`;

    try {
        const response = await fetch('/api/chat', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                message: autoPrompt,
                gradeLevel: currentGradeLevel,
                topic: topic || currentTopic || 'General math practice',
                sessionId: currentSessionId,
                stepByStep: stepByStepMode
            })
        });

        if (!response.ok) {
            throw new Error("HTTP " + response.status);
        }

        const data = await response.json();
        loadingBubble.remove();

        if (data.sessionId != null) {
            currentSessionId = data.sessionId;
            const url = new URL(window.location.href);
            url.searchParams.set('sessionId', currentSessionId);
            window.history.replaceState({}, '', url);
        }

        appendMessage(data.reply, 'bot');
    } catch (err) {
        console.error('Error auto-starting topic practice:', err);
        loadingBubble.textContent = "Hmm, I had trouble starting that practice. Try asking me a math question!";
    }
}

/*
 * Render the topic buttons for the current grade level.
 */
function renderTopicsForGrade() {
    const container = document.getElementById('button-grid');
    if (!container) return;

    container.innerHTML = '';

    const topicsForGrade = gradeTopics[currentGradeLevel] || [];
    if (topicsForGrade.length === 0) {
        return;
    }

    topicsForGrade.forEach(topic => {
        const btn = document.createElement('button');
        btn.classList.add('topic-btn');
        btn.textContent = topic;
        btn.dataset.topic = topic;

        if (topic === currentTopic) {
            btn.classList.add('topic-btn-active');
        }

        btn.addEventListener('click', () => {
            currentTopic = topic;

            const allBtns = container.querySelectorAll('.topic-btn');
            allBtns.forEach(b => b.classList.remove('topic-btn-active'));
            btn.classList.add('topic-btn-active');

            // Clear the chat and reset the session so this topic feels like a fresh lesson
            const chatWindow = document.getElementById('chat-window');
            if (chatWindow) {
                chatWindow.innerHTML = '';
            }

            currentSessionId = null;
            const url = new URL(window.location.href);
            url.searchParams.delete('sessionId');
            window.history.replaceState({}, '', url);

            appendMessage(`Let's practice: ${topic}`, 'bot');

            // Auto-start a kid-friendly practice question for this topic
            autoStartTopicPractice(topic);
        });

        container.appendChild(btn);
    });

}

/**
 * Send the user's message to /api/chat and show the AI response.
 */
async function sendUserMessage() {
    const input = document.getElementById('user-message-input');
    if (!input) return;

    const text = input.value.trim();
    if (!text) return;

    // Show user bubble
    appendMessage(text, 'user');
    input.value = '';

    const chatWindow = document.getElementById('chat-window');

    // Temporary "thinking" bubble
    const loadingBubble = document.createElement('div');
    loadingBubble.classList.add('chat-message', 'bot-message');
    loadingBubble.textContent = "Let me think about that...";
    chatWindow.appendChild(loadingBubble);
    chatWindow.scrollTop = chatWindow.scrollHeight;

    try {
        const response = await fetch('/api/chat', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                message: text,
                gradeLevel: currentGradeLevel,
                topic: currentTopic || 'General math practice',
                sessionId: currentSessionId,
                stepByStep: stepByStepMode    // 👈 NEW
            })
        });

        if (!response.ok) {
            throw new Error("HTTP " + response.status);
        }

        const data = await response.json();
        loadingBubble.remove();

        if (data.sessionId != null) {
            currentSessionId = data.sessionId;

            const url = new URL(window.location.href);
            url.searchParams.set('sessionId', currentSessionId);
            window.history.replaceState({}, '', url);
        }

        appendMessage(data.reply, 'bot');
    } catch (err) {
        console.error('Error sending message:', err);
        loadingBubble.textContent = "Oops! I had trouble answering. Try again in a moment.";
    }
}

/**
 * Load the initial welcome message from the tutor.
 */
async function loadWelcomeMessage() {
    const chatWindow = document.getElementById('chat-window');
    if (!chatWindow) return;

    const loadingBubble = document.createElement('div');
    loadingBubble.classList.add('chat-message', 'bot-message');
    loadingBubble.textContent = "Loading LearnBot...";
    chatWindow.appendChild(loadingBubble);

    try {
        const response = await fetch('/api/chat-welcome', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                gradeLevel: currentGradeLevel,
                topic: currentTopic || 'Welcome',
                stepByStep: stepByStepMode    // 👈 NEW
            })
        });

        if (!response.ok) {
            throw new Error("HTTP " + response.status);
        }

        const data = await response.json();
        loadingBubble.remove();

        if (data.sessionId != null) {
            currentSessionId = data.sessionId;
            const url = new URL(window.location.href);
            url.searchParams.set('sessionId', currentSessionId);
            window.history.replaceState({}, '', url);
        }

        appendMessage(data.reply, 'bot');
    } catch (err) {
        console.error('Error loading welcome message:', err);
        loadingBubble.textContent = "Hi! I'm LearnBot. Ask me any math question to get started!";
    }
}

/**
 * Load an existing session's transcript and replay it in the UI.
 */
async function loadExistingSession(id) {
    try {
        const res = await fetch(`/api/sessions/${id}`);
        if (!res.ok) {
            throw new Error('HTTP ' + res.status);
        }
        const data = await res.json();
        currentSessionId = data.id;
        currentGradeLevel = data.gradeLevel || currentGradeLevel;
        currentTopic = data.topic || currentTopic;

        const chatWindow = document.getElementById('chat-window');
        if (!chatWindow) return;
        chatWindow.innerHTML = '';

        if (Array.isArray(data.messages)) {
            data.messages.forEach(m => {
                appendMessage(m.text, m.sender === 'USER' ? 'user' : 'bot');
            });
        }

        const gradeSelect = document.getElementById('grade-level-select');
        if (gradeSelect && data.gradeLevel) {
            gradeSelect.value = data.gradeLevel;
        }

        renderTopicsForGrade();
    } catch (err) {
        console.error('Error loading existing session:', err);
    }
}

/**
 * Initialize the page once the DOM is ready.
 */
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('chat-form');
    const newSessionBtn = document.getElementById('new-session-btn');

    // GRADE LEVEL SETUP ---------------------------------------
    const gradeSelect = document.getElementById('grade-level-select');

    if (gradeSelect) {
        const savedGrade = localStorage.getItem('learnbot-grade-level');
        if (savedGrade) {
            currentGradeLevel = savedGrade;
            gradeSelect.value = savedGrade;
        } else {
            currentGradeLevel = gradeSelect.value || currentGradeLevel;
        }

        gradeSelect.addEventListener('change', () => {
            currentGradeLevel = gradeSelect.value;
            localStorage.setItem('learnbot-grade-level', currentGradeLevel);

            currentTopic = null;
            renderTopicsForGrade();
        });
    }

    // STEP-BY-STEP MODE SETUP --------------------------------
    const stepToggle = document.getElementById('step-mode-toggle');
    if (stepToggle) {
        const savedStepMode = localStorage.getItem('learnbot-step-mode');
        if (savedStepMode !== null) {
            stepByStepMode = (savedStepMode === 'true');
            stepToggle.checked = stepByStepMode;
        } else {
            stepToggle.checked = stepByStepMode;
        }

        stepToggle.addEventListener('change', () => {
            stepByStepMode = stepToggle.checked;
            localStorage.setItem('learnbot-step-mode', String(stepByStepMode));
        });
    }

    // TOPIC GRID INITIAL RENDER ------------------------------
    renderTopicsForGrade();

    // CHAT FORM HANDLING -------------------------------------
    if (form) {
        form.addEventListener('submit', (e) => {
            e.preventDefault();
            sendUserMessage();
        });

        const input = document.getElementById('user-message-input');
        if (input) {
            input.addEventListener('keydown', (e) => {
                if (e.key === 'Enter' && !e.shiftKey) {
                    e.preventDefault();
                    sendUserMessage();
                }
            });
        }
    }

    // NEW SESSION BUTTON -------------------------------------
    if (newSessionBtn) {
        newSessionBtn.addEventListener('click', () => {
            const chatWindow = document.getElementById('chat-window');
            if (chatWindow) {
                chatWindow.innerHTML = '';
            }

            currentSessionId = null;

            const url = new URL(window.location.href);
            url.searchParams.delete('sessionId');
            window.history.replaceState({}, '', url);

            loadWelcomeMessage();
        });
    }

    const fromUrlSession = getQueryParam('sessionId');
    const fromUrlTopic = getQueryParam('topic');

    if (fromUrlTopic) {
        currentTopic = decodeURIComponent(fromUrlTopic);
    }

    if (fromUrlSession) {
        currentSessionId = parseInt(fromUrlSession, 10);
        loadExistingSession(currentSessionId);
    } else {
        loadWelcomeMessage();
    }
});
