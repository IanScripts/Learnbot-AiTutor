@param String currentUser
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>LearnBot – Sessions</title>

    <!-- Main CSS for sessions page (themed to match Home/Teacher Mode) -->
    <link rel="stylesheet" href="/css/stylesSession.css">

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Google Font to match Teacher Mode -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700;800&display=swap" rel="stylesheet">
</head>

<body>
<header class="topbar">
    <div class="topbar-inner">
        <div class="topbar-title">
            <span class="topbar-logo" aria-hidden="true">LB</span>
            <div>
                <h1 class="app-name">LearnBot</h1>
                <p class="app-subtitle">Math Missions</p>
            </div>
        </div>

        <nav class="topbar-nav">
            <a href="/learn" class="topbar-link">Teacher Mode</a>
            <a href="/practicebook" class="topbar-link">Game Mode</a>
            <a href="/home" class="topbar-link">Home</a>
        </nav>
    </div>
</header>

<main class="page">
    <section class="card">
        <!-- Greeting for logged-in user -->
        <p class="helper-text" style="font-weight: 700; margin-bottom: 0.25rem;">
            Hi, ${currentUser}! Here are your past sessions:
        </p>

        <p class="helper-text">
            Tap a session to reopen your <strong>Teacher Mode</strong> chat for that topic.
        </p>

        <!-- JS will fill this completely -->
        <ul class="session-list" id="past-sessions" aria-label="Your past sessions">
        </ul>
    </section>
</main>

<script src="/js/session.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
