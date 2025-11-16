<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Child’s Play</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Spring serves static files -->
    <link rel="stylesheet" href="/css/styleforwelcomepage.css">
</head>
<body>
<div class="site-sky" aria-hidden="true">
    <div class="sun"></div>
</div>

<!-- Top bar with nav -->
<header class="topbar">
    <div class="inner top-nav">
        <div class="logo-text">LearnBot</div>
        <nav class="nav-buttons" aria-label="Site">
            <!-- Spring MVC routes -->
            <a href="/login" class="nav-pill">Login</a>
            <a href="/register" class="nav-pill">Sign Up</a>
            <a href="/home" class="nav-pill">Home</a>
            <a href="/sessions" class="nav-pill">Sessions</a>
            <a href="/contact" class="nav-pill">Contact</a>
        </nav>
    </div>
</header>

<section class="welcome">
    <div>
        <h1>Welcome!</h1>
        <p>
            Our names are Brian Gonzalez, Darien Amador, Ian Pimentel, Taiten McKiver.
            We built a site that targets young children to help them develop math skills
            early, using A.I. tools.
        </p>
    </div>
    <div class="skybox" aria-hidden="true">
        <div class="cloud one"></div>
        <div class="cloud two"></div>
        <div class="cloud three"></div>
    </div>
</section>

<section class="hero" aria-labelledby="lets-start">
    <div class="sky">
        <div class="inner">
            <h2 id="lets-start">Let’s get started!</h2>
            <p>
                <!-- Go to login / signup first; /home & /sessions are guarded -->
                <a class="btn btn-primary"
                   href="/login"
                   role="button"
                   aria-label="Continue to lessons (login required)">
                    Continue to lessons
                </a>
            </p>

            <section class="bulletin" aria-labelledby="news">
                <h3 id="news">What’s new?</h3>
                <ul>
                    <li><time>2025-09-26</time> Initial layout complete.</li>
                    <li><time>2025-09-26</time> Added CTA button &amp; developer bulletin.</li>
                </ul>
                <p class="note" style="margin:.6rem 0 0; color:var(--muted); font-size:.9rem">
                    Developers: edit this list in the HTML to post site update notes.
                </p>
            </section>
        </div>
    </div>

    <div class="hills" aria-hidden="true">
        <svg viewBox="0 0 1200 200" preserveAspectRatio="none"
             xmlns="http://www.w3.org/2000/svg">
            <path d="M0,120 C200,60 380,160 600,110 C820,60 1000,140 1200,90 L1200,200 L0,200 Z"
                  fill="var(--hill-2)"></path>
            <path d="M0,160 C220,120 450,200 700,150 C900,110 1080,170 1200,150 L1200,200 L0,200 Z"
                  fill="var(--hill)"></path>
        </svg>
    </div>
</section>

<footer></footer>

<!-- Bootstrap JS bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

