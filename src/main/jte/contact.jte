<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Contact – LearnBot</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Use the same styles  -->
    <link rel="stylesheet" href="/css/stylesModeHome.css">

    <!-- Google Font -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700;800&display=swap" rel="stylesheet" />
</head>
<body>
<div class="home-shell">
    <!-- Top bar copied from home.jte style -->
    <header class="home-topbar">
        <div class="home-logo">
            <div class="logo-bubble">LB</div>
            <div class="logo-text">
                <div class="logo-title">LearnBot</div>
                <div class="logo-subtitle">Math made fun.</div>
            </div>
        </div>

        <div class="home-user-area">
            <a href="/home" class="btn btn-sm btn-outline-dark">Home</a>
            <a href="/learn" class="btn btn-sm btn-outline-dark">Teacher Mode</a>
            <a href="/practicebook" class="btn btn-sm btn-outline-dark">Game Mode</a>
        </div>
    </header>

    <main class="home-main">
        <!-- Hero section-->
        <section class="home-hero">
            <p class="home-pill">Questions, ideas, or feedback?</p>
            <h1 class="home-heading">
                Contact us
            </h1>
            <p class="home-lead">
                If you have questions about LearnBot or suggestions to make it better,
                send us a message below.
            </p>
        </section>

        <!-- Single centered card using the same card styles as the mode cards -->
        <section class="home-modes">
            <article class="mode-card teacher-card" style="max-width: 720px; margin: 0 auto;">
                <h2 class="mode-title">Send us a message</h2>
                <p class="mode-text">
                    For this phase, the form is just for demo – we’ll wire it up later.
                </p>

                <form class="contact-form" action="#" method="post">
                    <div class="mb-3">
                        <label for="contactName" class="form-label">Your name</label>
                        <input type="text"
                               class="form-control"
                               id="contactName"
                               name="name"
                               placeholder="First and last name">
                    </div>

                    <div class="mb-3">
                        <label for="contactEmail" class="form-label">Your email</label>
                        <input type="email"
                               class="form-control"
                               id="contactEmail"
                               name="email"
                               placeholder="you@example.com">
                    </div>

                    <div class="mb-3">
                        <label for="contactMessage" class="form-label">Message</label>
                        <textarea class="form-control"
                                  id="contactMessage"
                                  name="message"
                                  rows="5"
                                  placeholder="How can we help?"></textarea>
                    </div>

                    <p class="text-muted small mb-3">
                        For this phase, the form is just for demo – we’ll wire it up later.
                    </p>

                    <button type="submit" class="btn btn-primary w-100">
                        Send
                    </button>
                </form>
            </article>
        </section>
    </main>
</div>

<!-- Bootstrap JS bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

