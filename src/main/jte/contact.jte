<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Contact – LearnBot</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- reuse main app styles -->
    <link rel="stylesheet" href="/css/stylesforHomePage.css">
</head>
<body>
<div class="main-container">
    <!-- Left sidebar-->
    <aside class="sidebar d-none d-md-flex">
        <div class="sidebar-header">LearnBot</div>

        <nav class="nav flex-column" aria-label="Main">
            <a class="sidebar-link" href="/home">Home</a>
            <a class="sidebar-link" href="/sessions">Sessions</a>
        </nav>

        <div class="sidebar-footer">
            <nav class="nav flex-column" aria-label="Secondary">
                <a class="sidebar-link" href="/">Welcome</a>
                <a class="sidebar-link" href="/settings">Settings</a>
                <a class="sidebar-link sidebar-link-active" href="/contact">Contact</a>
            </nav>
        </div>
    </aside>

    <!-- Right side -->
    <main class="chat-area">
        <div class="chat-content p-4 d-flex justify-content-center">
            <div style="width: 100%; max-width: 640px;">

                <h1 class="mb-1">Contact Us</h1>
                <p class="mb-4">
                    If you have questions or feedback about LearnBot, send us a message!
                </p>

                <!-- Contact form card -->
                <form class="card p-3"
                      style="background-color: var(--bg-card); color: var(--text); border-color: var(--sidebar-border);"
                      action="#"
                      method="post">
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
                                  rows="4"
                                  placeholder="How can we help?"></textarea>
                    </div>

                    <p class="text-muted mb-3" style="font-size: 0.9rem;">
                        For this phase, the form is just for demo – we’ll wire it up later.
                    </p>

                    <button type="submit" class="btn btn-primary">
                        Send
                    </button>
                </form>
            </div>
        </div>
    </main>
</div>
<script src="/js/theme.js"></script>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
