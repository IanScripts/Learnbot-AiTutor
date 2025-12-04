@param String currentUser

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>LearnBot for Kids!</title>

    <!-- Main CSS for this page (must live in src/main/resources/static/css/stylesforHomePage.css) -->
    <link rel="stylesheet" href="/css/stylesforHomePage.css">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;700;800&display=swap" rel="stylesheet">
</head>
<body>

<div class="main-container">
    <!-- LEFT SIDEBAR -->
    <aside class="sidebar d-none d-md-flex">
        <div class="sidebar-header">
            <div>LearnBot</div>

            @if (currentUser != null) {
            <div class="sidebar-user">
                <span class="sidebar-user-name">Hi, ${currentUser}!</span>
                <a href="/logout" class="sidebar-logout">Logout</a>
            </div>

            @endif}
        </div>

        <!-- MAIN NAV -->
        <nav class="nav flex-column" aria-label="Main">
            <a class="sidebar-link" href="/sessions">Sessions</a>
            <a class="sidebar-link" href="/tutor/topics">Practice &amp; Progress</a>
        </nav>


        <!-- BOTTOM NAV -->
        <div class="sidebar-footer">
            <nav class="nav flex-column" aria-label="Secondary">
                <a class="sidebar-link" href="/">Welcome</a>
                <a class="sidebar-link" href="/settings">Settings</a>
            </nav>
        </div>
    </aside>

    <!-- RIGHT MAIN AREA (CHAT) -->
    <main class="chat-area">
        <div class="chat-content">
            <div class="chat-session-container">

                <!-- Top actions row -->
                <div class="chat-header-actions">
                    <button id="new-session-btn" class="chat-new-btn">
                        + New Chat
                    </button>
                </div>

                <!-- Chat messages go here -->
                <div id="chat-window">
                    <!-- LearnBot will insert its welcome message via script.js -->
                </div>

                <!-- CHAT INPUT ROW -->
                <div class="chat-input-row">
                    <input
                            type="text"
                            id="user-message-input"
                            class="chat-input"
                            placeholder="Ask me a math question..." />
                    <button id="send-message-btn" class="chat-send-btn">Send</button>
                </div>

            </div>
        </div>

            <!-- Grade level selector -->
            <div class="grade-select-box">
                <label for="grade-level-select" class="grade-label">
                    Choose your grade:
                </label>
                <select id="grade-level-select" class="grade-select">
                    <option value="1st grade">1st Grade</option>
                    <option value="2nd grade">2nd Grade</option>
                    <option value="3rd grade">3rd Grade</option>
                    <option value="4th grade">4th Grade</option>
                    <option value="5th grade">5th Grade</option>
                </select>
                <small class="grade-hint">
                    LearnBot will explain math at this grade level.
                </small>
            </div>

            <!-- Step-by-step mode toggle -->
            <div class="step-mode-box">
                <label class="step-mode-label">
                    <input type="checkbox" id="step-mode-toggle" checked>
                    Explain answers step-by-step
                </label>
                <small class="step-mode-hint">
                    When this is on, LearnBot will show numbered steps like a real tutor.
                </small>
            </div>

            <!-- existing topic/mission button grid -->
            <div id="button-grid" class="button-grid">
                <!-- future: Addition / Subtraction buttons, etc. -->
            </div>

<!-- Dark-mode logic -->
<script src="/js/theme.js"></script>

<!-- LearnBot chat logic -->
<script src="/js/script.js"></script>

<!-- Bootstrap JS bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </main>
</div>
</body>
</html>
