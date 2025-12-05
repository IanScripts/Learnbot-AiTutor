@param String currentUser

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>LearnBot – Teacher Mode</title>

    <!-- Main CSS for this page -->
    <link rel="stylesheet" href="/css/stylesforHomePage.css">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Google Font to match the rest of the app -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700;800&display=swap" rel="stylesheet">
</head>
<body class="teacher-page">

<div class="d-flex" style="min-height: 100vh;">

    <aside class="sidebar d-none d-md-flex flex-column">
        <div class="sidebar-header">
            <div class="sidebar-logo-pill">
                <span>LearnBot</span>
            </div>
            <div class="sidebar-subtitle">
                Your friendly AI math teacher.
            </div>
        </div>

        <div class="mb-3">
            <div class="sidebar-user-label">Signed in as</div>
            <div class="sidebar-user-name">Hi, ${currentUser}!</div>
            <form action="/logout" method="post" class="mt-2">
                <button type="submit" class="btn btn-sm btn-outline-secondary">
                    Logout
                </button>
            </form>
        </div>

        <!-- MAIN NAV -->
        <nav class="nav flex-column mt-4" aria-label="Main navigation">
            <div class="sidebar-section-title">LEARN &amp; PRACTICE</div>
            <a class="sidebar-link active" href="/learn">Teacher Mode</a>
            <a class="sidebar-link" href="/practicebook">Game Mode (Practice Book)</a>

            <div class="sidebar-section-title">YOUR STUFF</div>
            <a class="sidebar-link" href="/sessions">Sessions</a>
            <!-- 🔹 New parent dashboard link -->
            <a class="sidebar-link" href="/parent">Parent / Teacher View</a>
        </nav>

        <!-- BOTTOM NAV -->
        <div class="mt-auto sidebar-footer">
            <nav class="nav flex-column" aria-label="Secondary navigation">
                <a class="sidebar-link" href="/home">Mode Selector</a>
                <a class="sidebar-link" href="/welcome">Welcome</a>
            </nav>
        </div>
    </aside>

    <main class="chat-area flex-grow-1">
        <div class="teacher-main container-fluid py-4">

            <!-- TOP TITLE AREA -->
            <div class="row mb-4">
                <div class="col-12">
                    <div class="chat-title-group">
                        <div class="chat-mode-pill">
                            👩‍🏫 Teacher Mode
                        </div>
                        <h1 class="chat-title">Get a mini math lesson</h1>
                        <p class="chat-subtitle">
                            Choose your grade and topic, and LearnBot will teach a short lesson
                            with an example and a practice question.
                        </p>
                    </div>
                </div>
            </div>
            <div class="row g-4 align-items-start">
                <!-- CHAT COLUMN -->
                <div class="col-lg-8">
                    <div class="chat-session-container">
                        <!-- Header row with New Chat -->
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <div class="d-flex align-items-center gap-2">
                                <span class="chat-session-label">Teacher Mode</span>
                            </div>
                            <button id="new-session-btn" class="new-chat-btn">
                                + New Chat
                            </button>
                        </div>

                        <!-- Chat messages go here -->
                        <div id="chat-window" class="chat-window">
                            <!-- LearnBot will insert its welcome message via script.js -->
                        </div>
                    </div>
                </div>

                <div class="col-lg-4">
                    <!-- Teacher style selector -->
                    <div class="teacher-style-box mb-3">
                        <label for="persona-select" class="teacher-style-label">
                            Teacher Style
                        </label>
                        <select id="persona-select" class="teacher-style-select form-select">
                            <option value="coach">Friendly Coach</option>
                            <option value="wizard">Math Wizard</option>
                            <option value="space">Space Explorer</option>
                        </select>
                        <small class="teacher-style-hint">
                            Choose how LearnBot explains things.
                        </small>
                    </div>

                    <!-- Grade level selector -->
                    <div class="grade-select-box mb-3">
                        <label for="grade-level-select" class="grade-label">
                            Choose your grade:
                        </label>
                        <select id="grade-level-select" class="grade-select form-select">
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


                    <div class="step-mode-box d-none">
                        <label class="step-mode-label">
                            <input type="checkbox" id="step-mode-toggle" checked>
                            Explain answers step-by-step
                        </label>
                        <small class="step-mode-hint">
                            When this is on, LearnBot will show numbered steps like a tutor.
                        </small>
                    </div>

                    <!-- Topic / mission buttons-->
                    <div id="button-grid" class="button-grid">
                        <!-- JS will render topic buttons for the selected grade -->
                    </div>
                </div>
            </div>

        </div>

        <!-- Scripts -->
        <script src="/js/theme.js"></script>
        <script src="/js/script.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </main>
</div>
</body>
</html>


