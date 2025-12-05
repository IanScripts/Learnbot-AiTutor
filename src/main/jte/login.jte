@param String error
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Login – LearnBot</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">


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
            <p class="home-pill">Welcome back</p>
            <h1 class="home-heading">Log in to LearnBot</h1>
            <p class="home-lead">
                Sign in to see your saved sessions and keep track of your learner’s progress.
            </p>
        </section>

        <!-- Centered login card -->
        <section class="home-modes">
            <article class="mode-card teacher-card" style="max-width: 480px; margin: 0 auto;">
                <h2 class="mode-title mb-2">Login</h2>
                <p class="mode-text mb-3">
                    Enter your account details to continue.
                </p>

                <div class="mb-2 text-danger small">
                    ${error}
                </div>

                <form method="post" action="/login" class="mt-2">
                    <div class="mb-3">
                        <label for="login-username" class="form-label">Username</label>
                        <input type="text"
                               class="form-control"
                               id="login-username"
                               name="username"
                               placeholder="Enter your username"
                               required>
                    </div>

                    <div class="mb-3">
                        <label for="login-password" class="form-label">Password</label>
                        <input type="password"
                               class="form-control"
                               id="login-password"
                               name="password"
                               placeholder="Enter your password"
                               required>
                    </div>

                    <div class="d-grid gap-2 mt-3">
                        <button type="submit" class="btn btn-primary">
                            Login
                        </button>
                        <a href="/register" class="btn btn-outline-secondary">
                            Create a new account
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


