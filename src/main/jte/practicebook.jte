@param String currentUser
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>My Practice Book</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <!-- Practice Book CSS -->
    <link rel="stylesheet" href="/css/stylesPracticeBook.css">
</head>
<body>

<header class="topbar d-flex align-items-center justify-content-between px-3">
    <div class="d-flex align-items-center gap-2">
        <a class="home-btn" href="/home" aria-label="Back to LearnBot">
            <svg viewBox="0 0 24 24" aria-hidden="true" class="icon" style="width: 24px; height: 24px;">
                <path d="M12 3 2 12h3v8h6v-5h2v5h6v-8h3L12 3z"/>
            </svg>
            <span>Home</span>
        </a>
    </div>

    <h1 class="page-title text-center m-0">
        <span class="title-math-letters" aria-hidden="true">
            <span>M</span><span>a</span><span>t</span><span>h</span>
        </span>
        <span class="visually-hidden">Math</span>
        Practice Book
    </h1>

    <div class="d-flex flex-column flex-sm-row align-items-sm-center gap-2">
        <nav class="topbar-nav d-flex gap-2">
            <a href="/home">Chat</a>
            <a href="/sessions">Missions</a>
            <a href="/practicebook" class="active">Practice Book</a>
            <a href="/settings">Settings</a>
            <a href="/contact">Contact</a>
        </nav>
        <span class="small fw-semibold text-nowrap">
            Hi, ${currentUser}!
        </span>
    </div>
</header>

<main class="page container py-3">

    <!-- Mission + Stats -->
    <section class="card p-3 p-sm-4 mb-3">
        <header class="mb-3">
            <h2 class="h4 m-0">🚀 Mission: Add within 20</h2>
            <p class="text-muted mb-0">
                Type your answer and tap <strong>Check</strong>.
                You can also tap <strong>Need a hint?</strong> to get guidance from LearnBot.
            </p>
        </header>

        <div class="row g-2" id="stats-row">
            <div class="col-6 col-md-3">
                <div class="stat-chip">
                    <div class="label">📘 Questions answered</div>
                    <div class="value" id="stats-total">0</div>
                </div>
            </div>

            <div class="col-6 col-md-3">
                <div class="stat-chip">
                    <div class="label">✅ Correct</div>
                    <div class="value" id="stats-correct">0</div>
                </div>
            </div>

            <div class="col-6 col-md-3">
                <div class="stat-chip">
                    <div class="label">🎯 Accuracy</div>
                    <div class="value" id="stats-accuracy">—</div>
                </div>
            </div>

            <div class="col-6 col-md-3">
                <div class="stat-chip">
                    <div class="label">🔥 Best streak</div>
                    <div class="value" id="stats-best-streak">0</div>
                </div>
            </div>
        </div>
    </section>

    <!-- Practice Problems Section -->
    <section class="card p-3 p-sm-4">
        <header class="d-flex justify-content-between align-items-center mb-3">
            <h2 class="h5 m-0">📚 Practice Problems</h2>
            <div class="d-flex gap-2">
                <button class="btn btn-sm btn-outline-primary" id="btn-new-set">
                    New Set 🔄
                </button>
                <button class="btn btn-sm btn-success" onclick="startMC('Addition', '1st grade')">
                    🧠 Multiple-Choice Mode
                </button>

                <button class="btn btn-sm btn-info" onclick="startGuidedLesson('Addition', '1st grade')">
                    👣 Step-by-Step Mode
                </button>

            </div>
        </header>

        <div id="problems-container" class="row g-3">
            <!-- Filled by JS -->
        </div>
    </section>

    <!-- Multiple-Choice MODE -->
    <section id="mc-section" class="card p-3 p-sm-4 mt-4" style="display: none;">
        <header>
            <h2 class="h5 m-0 mb-3">🧠 Multiple-Choice Mode</h2>
        </header>

        <div id="mc-question-box" class="mb-3"></div>

        <div id="mc-choices-box" class="d-flex flex-column gap-2"></div>

        <button class="btn btn-sm btn-secondary mt-3" onclick="exitMC()">
            ↩️ Exit Multiple-Choice Mode
        </button>
    </section>

</main>

<script>
    function showMCSection() {
        document.getElementById("mc-section").style.display = "block";
        document.getElementById("problems-container").style.display = "none";
    }

    function exitMC() {
        document.getElementById("mc-section").style.display = "none";
        document.getElementById("problems-container").style.display = "flex";
    }
</script>

<script src="/js/practicebook.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
