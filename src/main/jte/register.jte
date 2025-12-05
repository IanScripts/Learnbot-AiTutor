@param String error
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Sign Up – LearnBot</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Same visual style as home.jte -->
    <link rel="stylesheet" href="/css/stylesModeHome.css">

    <!-- Google Font -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700;800&display=swap" rel="stylesheet" />
</head>
<body>
<div class="home-shell">
    <!-- Top bar -->
    <header class="home-topbar">
        <div class="home-logo">
            <div class="logo-bubble">LB</div>
            <div class="logo-text">
                <div class="logo-title">LearnBot</div>
            </div>
        </div>
    </header>

    <main class="home-main">
        <!-- Hero text -->
        <section class="home-hero">
            <p class="home-pill">New here?</p>
            <h1 class="home-heading">Create your LearnBot account</h1>
            <p class="home-lead">
                Set up an account so we can save your learner’s sessions and progress.
            </p>
        </section>

        <!-- Centered sign-up card -->
        <section class="home-modes">
            <article class="mode-card game-card" style="max-width: 480px; margin: 0 auto;">
                <h2 class="mode-title mb-2">Create account</h2>
                <p class="mode-text mb-3">
                    Choose a simple username and password. You’ll use these to log in next time.
                </p>
                <div class="mb-2 text-danger small">
                    ${error}
                </div>

                <form method="post" action="/register" class="mt-2">
                    <div class="mb-3">
                        <label for="signup-username" class="form-label">Username</label>
                        <input type="text"
                               class="form-control"
                               id="signup-username"
                               name="username"
                               placeholder="Choose a username"
                               required>
                    </div>

                    <div class="mb-3">
                        <label for="signup-password" class="form-label">Password</label>
                        <input type="password"
                               class="form-control"
                               id="signup-password"
                               name="password"
                               placeholder="Choose a password"
                               required>
                    </div>

                    <div class="d-grid gap-2 mt-3">
                        <button type="submit" class="btn btn-primary">
                            Sign Up
                        </button>
                        <a href="/login" class="btn btn-outline-secondary">
                            Back to Login
                        </a>
                    </div>
                </form>
            </article>
        </section>
    </main>

</div>

<!-- Bootstrap JS bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

