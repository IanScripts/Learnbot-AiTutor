@param String currentUser
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Math Missions</title>

    <!-- main CSS for sessions page -->
    <link rel="stylesheet" href="/css/stylesSession.css">

    <!-- Bootstrap-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<header class="topbar">
    <a class="home-btn" href="/home" aria-label="Go to Home">
        <svg viewBox="0 0 24 24" aria-hidden="true" class="icon">
            <path d="M12 3 2 12h3v8h6v-5h2v5h6v-8h3L12 3z"/>
        </svg>
        <span>Home</span>
    </a>
    <h1 class="page-title">
        <span class="title-math-letters" aria-hidden="true">
            <span>M</span><span>a</span><span>t</span><span>h</span>
        </span>
        <span class="visually-hidden">Math</span> Missions
    </h1>
</header>

<main class="page">
    <section class="card">

        <!-- Greeting for logged-in user -->
        <p class="helper-text" style="font-weight: 700; margin-bottom: 0.25rem;">
            Hi, ${currentUser}! Here are your past sessions:
        </p>
        <!-- jte: end -->

        <p class="helper-text">
            Tap a session to open your <strong>Practice Book</strong> and see your work.
        </p>

        <!-- IMPORTANT: start EMPTY; JS will fill this completely -->
        <ul class="session-list" id="past-sessions" aria-label="Your past sessions">
        </ul>
    </section>
</main>

<script src="/js/session.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
