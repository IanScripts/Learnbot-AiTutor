<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Settings – LearnBot</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- reuse main app styles -->
    <link rel="stylesheet" href="/css/stylesforHomePage.css">
</head>
<body>
<div class="main-container">
    <!-- reuse the same sidebar layout -->
    <aside class="sidebar d-none d-md-flex">
        <div class="sidebar-header">LearnBot</div>

        <nav class="nav flex-column" aria-label="Main">
            <a class="sidebar-link" href="/home">Home</a>
            <a class="sidebar-link" href="/sessions">Sessions</a>
        </nav>

        <div class="sidebar-footer">
            <nav class="nav flex-column" aria-label="Secondary">
                <a class="sidebar-link" href="/">Welcome</a>
                <a class="sidebar-link sidebar-link-active" href="/settings">Settings</a>
            </nav>
        </div>
    </aside>

    <main class="chat-area">
        <!-- center the settings content on the right side -->
        <div class="chat-content p-4 d-flex justify-content-center">
            <div style="width: 100%; max-width: 640px;">
                <h1 class="mb-1">Settings</h1>
                <p class="mb-4">
                    Choose how LearnBot looks. This setting applies to all pages except the welcome page.
                </p>

                <!-- Theme toggle card -->
                <div class="card p-3"
                     style="max-width: 400px; background-color: var(--bg-card); color: var(--text); border-color: var(--sidebar-border);">
                    <h2 class="h5 mb-3">Theme</h2>

                    <div class="form-check form-switch mb-2">
                        <input class="form-check-input" type="checkbox" id="themeSwitch">
                        <label class="form-check-label" for="themeSwitch">
                            Dark mode
                        </label>
                    </div>
                    <p class="mb-0" id="themeStatus" style="color: var(--text-muted);">
                        Current theme: Light
                    </p>
                </div>
            </div>
        </div>
    </main>
</div>

<!-- load theme.js first -->
<script src="/js/theme.js"></script>

<!-- wire up the switch -->
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const switchEl = document.getElementById('themeSwitch');
        const statusEl = document.getElementById('themeStatus');

        if (!switchEl || !statusEl) return;

        // Read current theme from <html data-theme="..."> (set by theme.js)
        const currentTheme =
            document.documentElement.dataset.theme ||
            'light';

        const isDark = currentTheme === 'dark';
        switchEl.checked = isDark;
        statusEl.textContent = 'Current theme: ' + (isDark ? 'Dark' : 'Light');

        switchEl.addEventListener('change', function () {
            const theme = switchEl.checked ? 'dark' : 'light';

            if (window.setTheme) {
                window.setTheme(theme);
            } else {
                document.documentElement.dataset.theme = theme;
                localStorage.setItem('learnbot-theme', theme);
            }

            statusEl.textContent = 'Current theme: ' + (theme === 'dark' ? 'Dark' : 'Light');
        });
    });
</script>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
