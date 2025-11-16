(function () {
    function applyTheme(theme) {
        document.documentElement.dataset.theme = theme;
    }

    // Run once when page loads
    document.addEventListener('DOMContentLoaded', function () {
        const saved = localStorage.getItem('learnbot-theme');
        const prefersDark = window.matchMedia &&
            window.matchMedia('(prefers-color-scheme: dark)').matches;

        const theme = saved || (prefersDark ? 'dark' : 'light');
        applyTheme(theme);
    });

    // Allow other scripts to change theme
    window.setTheme = function (theme) {
        localStorage.setItem('learnbot-theme', theme);
        applyTheme(theme);
    };
})();
