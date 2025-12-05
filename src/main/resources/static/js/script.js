let currentSessionId = null;
let currentTopic = null;
let currentGradeLevel = '1st grade';
let currentPersona = 'coach';
let stepByStepMode = true; // default: ON


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


function appendMessage(text, sender) {
    const chatWindow = document.getElementById('chat-window');
    if (!chatWindow) return;

    const bubble = document.createElement('div');
    bubble.classList.add('chat-message');
    bubble.classList.add(sender === 'user' ? 'user-message' : 'bot-message');

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


function appendLoadingBubble(text = 'Thinking...') {
    const chatWindow = document.getElementById('chat-window');
    if (!chatWindow) return null;

    const loadingBubble = document.createElement('div');
    loadingBubble.classList.add('chat-message', 'bot-message');
    loadingBubble.textContent = text;
    chatWindow.appendChild(loadingBubble);
    chatWindow.scrollTop = chatWindow.scrollHeight;
    return loadingBubble;
}


function getQueryParam(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}


async function requestMiniLectureForCurrentSelection(options = {}) {
    const { newSession = false, clearChat = false } = options;

    if (!currentTopic) {
        console.warn('No topic selected, skipping mini-lecture.');
        return;
    }

    const chatWindow = document.getElementById('chat-window');
    if (!chatWindow) return;

    if (clearChat) {
        chatWindow.innerHTML = '';
    }

    const loadingBubble = appendLoadingBubble('Let me prepare a short lesson...');

    try {
        const response = await fetch('/api/chat', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                message: "",
                gradeLevel: currentGradeLevel,
                topic: currentTopic || 'General math practice',
                sessionId: newSession ? null : currentSessionId,
                stepByStep: stepByStepMode,
                persona: currentPersona,
                miniLecture: true
            })
        });

        if (!response.ok) {
            throw new Error('Mini-lecture request failed: ' + response.status);
        }

        const data = await response.json();
        if (loadingBubble) loadingBubble.remove();

        if (data.sessionId != null) {
            currentSessionId = data.sessionId;
            const url = new URL(window.location.href);
            url.searchParams.set('sessionId', currentSessionId);
            url.searchParams.set('topic', encodeURIComponent(currentTopic));
            window.history.replaceState({}, '', url);
        }

        appendMessage(data.reply, 'bot');
    } catch (err) {
        console.error('Error requesting mini-lecture:', err);
        if (loadingBubble) loadingBubble.remove();
        appendMessage('Sorry, I could not get a mini-lecture right now.', 'bot');
    }
}

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

            // Start a fresh session & lecture for this topic
            currentSessionId = null;
            const url = new URL(window.location.href);
            url.searchParams.delete('sessionId');
            url.searchParams.set('topic', encodeURIComponent(currentTopic));
            window.history.replaceState({}, '', url);

            requestMiniLectureForCurrentSelection({
                newSession: true,
                clearChat: true
            });
        });

        container.appendChild(btn);
    });
}

async function loadWelcomeMessage() {
    const loadingBubble = appendLoadingBubble('Loading LearnBot...');

    try {
        const response = await fetch('/api/chat-welcome', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                gradeLevel: currentGradeLevel,
                topic: currentTopic || 'Welcome',
                stepByStep: stepByStepMode,
                persona: currentPersona
            })
        });

        if (!response.ok) {
            throw new Error('HTTP ' + response.status);
        }

        const data = await response.json();
        if (loadingBubble) loadingBubble.remove();

        if (data.sessionId != null) {
            currentSessionId = data.sessionId;
            const url = new URL(window.location.href);
            url.searchParams.set('sessionId', currentSessionId);
            window.history.replaceState({}, '', url);
        }

        appendMessage(data.reply, 'bot');
    } catch (err) {
        console.error('Error loading welcome message:', err);
        if (loadingBubble) loadingBubble.remove();
        appendMessage(
            "Hi! I'm LearnBot. Choose your grade and topic to get a mini-lesson!",
            'bot'
        );
    }
}
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

document.addEventListener('DOMContentLoaded', () => {
    const newSessionBtn = document.getElementById('new-session-btn');
    const gradeSelect = document.getElementById('grade-level-select');
    const personaSelect = document.getElementById('persona-select');

    if (personaSelect) {
        personaSelect.addEventListener('change', () => {
            currentPersona = personaSelect.value;
        });
    }

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

    renderTopicsForGrade();
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

