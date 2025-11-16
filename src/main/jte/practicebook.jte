<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>My Practice Book</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/stylesPracticeBook.css">
</head>
<body>
<header class="topbar">
    <a class="home-btn" href="#" aria-label="Back to Missions">
        <svg viewBox="0 0 24 24" aria-hidden="true" class="icon">
            <path d="M12 3 2 12h3v8h6v-5h2v5h6v-8h3L12 3z"/>
        </svg>
        <span>Home</span>
    </a>
    <h1 class="page-title"><span class="title-math-letters"><span>M</span><span>a</span><span>t</span><span>h</span></span> Practice Book</h1>
</header>

<main class="page">
    <section class="card p-3 p-sm-4">
        <header class="mb-3">
            <h2 class="h4 m-0">Mission: Add within 20</h2>
            <div class="chip-row">
                <span class="chip">Today</span><span class="chip">10 problems</span><span class="chip">12 min</span>
            </div>
            <p class="helper-text m-0">Click a problem to reveal the answer.</p>
        </header>

        <div class="problem-grid">
            <article class="problem-card" tabindex="0"><div class="problem-text">7 + 5 = ?</div><div class="answer" hidden>12</div></article>
            <article class="problem-card" tabindex="0"><div class="problem-text">13 − 4 = ?</div><div class="answer" hidden>9</div></article>
            <article class="problem-card" tabindex="0"><div class="problem-text">9 + 8 = ?</div><div class="answer" hidden>17</div></article>
            <article class="problem-card" tabindex="0"><div class="problem-text">16 − 9 = ?</div><div class="answer" hidden>7</div></article>
            <article class="problem-card" tabindex="0"><div class="problem-text">3 + 12 = ?</div><div class="answer" hidden>15</div></article>
            <article class="problem-card" tabindex="0"><div class="problem-text">20 − 7 = ?</div><div class="answer" hidden>13</div></article>
        </div>

        <div class="practice-actions">
            <a class="btn-ghost" href="#">← Back to Missions</a>
            <button class="btn-solid" type="button">Start New Mission</button>
        </div>
    </section>
</main>

<script>
    document.addEventListener('click', e => {
        const card = e.target.closest('.problem-card'); if (!card) return;
        const a = card.querySelector('.answer'); const hide = a.hasAttribute('hidden');
        a.toggleAttribute('hidden', !hide); card.classList.toggle('showing-answer', hide);
    });
    document.addEventListener('keydown', e => {
        if (!['Enter',' '].includes(e.key)) return;
        const card = document.activeElement.closest?.('.problem-card'); if (!card) return;
        e.preventDefault(); card.click();
    });
</script>

<!-- Bootstrap JS bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- Top-left house icon -->
<a href="/home" class="home-button" aria-label="Back to LearnBot">
    <!-- your SVG icon here -->
</a>
<a href="/sessions">Sessions</a>
<a href="/practice-book">Practice Book</a>
<a href="/contact">Contact</a>

</body>
</html>

