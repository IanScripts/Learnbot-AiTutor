let currentSessionId = null;
let currentTopic = null;

function appendMessage(text, type) {
    const chatWindow = document.getElementById('chat-window');
    if (!chatWindow) return;

    const div = document.createElement('div');
    div.classList.add('chat-message');

    if (type === 'user') {
        div.classList.add('user-message');
    } else {
        div.classList.add('bot-message');
    }

    div.textContent = text;
    chatWindow.appendChild(div);
    chatWindow.scrollTop = chatWindow.scrollHeight;
}

function getQueryParam(name) {
    const params = new URLSearchParams(window.location.search);
    return params.get(name);
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
                gradeLevel: '1st grade',
                topic: currentTopic || 'General math practice',
                sessionId: currentSessionId
            })
        });

        if (!response.ok) {
            throw new Error("HTTP " + response.status);
        }

        const data = await response.json();
        loadingBubble.remove();

        // Store/refresh current session id
        if (data.sessionId != null) {
            currentSessionId = data.sessionId;

            // update URL so refresh keeps the session
            const url = new URL(window.location.href);
            url.searchParams.set('sessionId', currentSessionId);
            window.history.replaceState({}, '', url);
        }

        appendMessage(data.reply, 'bot');
    } catch (err) {
        console.error('Error calling /api/chat:', err);
        loadingBubble.textContent = "Oops, I had trouble answering. Please try again.";
    }
}
async function loadWelcomeMessage() {
    const chatWindow = document.getElementById('chat-window');
    if (!chatWindow) return;

    const loadingBubble = document.createElement('div');
    loadingBubble.classList.add('chat-message', 'bot-message');
    loadingBubble.textContent = "Loading LearnBot...";
    chatWindow.appendChild(loadingBubble);

    try {
        const response = await fetch('/api/chat-welcome', {   // 👈 NEW ENDPOINT
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                gradeLevel: '1st grade',
                topic: currentTopic || 'Welcome'
                // no "message" field here on purpose
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
        currentTopic = data.topic || currentTopic;

        const turns = data.turns || [];
        const chatWindow = document.getElementById('chat-window');
        chatWindow.innerHTML = '';

        if (turns.length === 0) {
            await loadWelcomeMessage();
            return;
        }

        for (const turn of turns) {
            appendMessage(turn.content, turn.role === 'user' ? 'user' : 'bot');
        }
    } catch (err) {
        console.error('Error loading existing session:', err);
        await loadWelcomeMessage();
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const sendBtn = document.getElementById('send-message-btn');
    const input = document.getElementById('user-message-input');
    const newSessionBtn = document.getElementById('new-session-btn');

    if (sendBtn) {
        sendBtn.addEventListener('click', sendUserMessage);
    }

    if (input) {
        input.addEventListener('keydown', (e) => {
            if (e.key === 'Enter') {
                e.preventDefault();
                sendUserMessage();
            }
        });
    }

    // Start a fresh session
    if (newSessionBtn) {
        newSessionBtn.addEventListener('click', () => {
            const chatWindow = document.getElementById('chat-window');
            if (chatWindow) {
                chatWindow.innerHTML = '';
            }

            // clear current session and URL param
            currentSessionId = null;
            const url = new URL(window.location.href);
            url.searchParams.delete('sessionId');
            window.history.replaceState({}, '', url);

            // this will create a brand new LearnSession on the server
            loadWelcomeMessage();
        });
    }

    // Read session / topic from URL if present
    const fromUrlSession = getQueryParam('sessionId');
    const fromUrlTopic = getQueryParam('topic');

    if (fromUrlTopic) {
        currentTopic = decodeURIComponent(fromUrlTopic);
    }

    if (fromUrlSession) {
        currentSessionId = parseInt(fromUrlSession, 10);
        loadExistingSession(currentSessionId);
    } else {
        // brand new: let the AI say hello and this will create a new session
        loadWelcomeMessage();
    }
});

