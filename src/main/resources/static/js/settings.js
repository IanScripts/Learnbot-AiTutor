document.addEventListener('DOMContentLoaded', () => {
    const toggle = document.getElementById('theme-toggle');
    const label = document.getElementById('current-theme-label');

    if (!toggle) return;

    const THEME_KEY = 'learnbot-theme';
    const currentTheme =
        document.documentElement.dataset.theme ||
        localStorage.getItem(THEME_KEY) ||
        'light';

    toggle.checked = currentTheme === 'dark';
    if (label) {
        label.textContent = 'Current theme: ' + (currentTheme === 'dark' ? 'Dark' : 'Light');
    }

    toggle.addEventListener('change', () => {
        const theme = toggle.checked ? 'dark' : 'light';

        if (window.setTheme) {
            window.setTheme(theme); // handed off to theme.js
        } else {
            document.documentElement.dataset.theme = theme;
            localStorage.setItem(THEME_KEY, theme);
        }

        if (label) {
            label.textContent = 'Current theme: ' + (theme === 'dark' ? 'Dark' : 'Light');
        }
    });
});
