@param String currentUser

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>LearnBot — Game Mode</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <!-- Shared LearnBot UI (same as homepage/sessions) -->
    <link rel="stylesheet" href="/css/stylesPracticeBook.css">

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>

<header class="topbar">
    <div class="topbar-title">
        <span class="topbar-logo">LB</span>
        <div>
            <h1 class="app-name">LearnBot</h1>
            <p class="app-subtitle">Math Missions</p>
        </div>
    </div>

    <nav class="topbar-nav">
        <a href="/home" class="topbar-link">Home</a>
        <a href="/learn" class="topbar-link">Teacher Mode</a>
        <a href="/practicebook" class="topbar-link">Game Mode</a>

    </nav>
</header>

<main class="container my-4">
    <div class="row justify-content-center">
        <div class="col-lg-8">

            <div class="card shadow-sm border-0">
                <div class="card-body">

                    <h1 class="h4 fw-bold mb-2">Game Mode (Practice Book)</h1>
                    <p class="text-muted mb-4">
                        Answer quick multiple-choice questions to train your math skills.
                        Pick a grade and topic to start practicing instantly!
                    </p>

                    <!-- LearnBot chat bubble -->
                    <div class="d-flex align-items-center mb-4"
                         style="background: #eef2ff; border-radius: 12px; padding: 0.75rem 1rem;">
                        <div class="me-3" style="font-size: 1.8rem;">🤖</div>
                        <div>
                            <div class="fw-bold mb-1">Hi there! I’m LearnBot!</div>
                            <div class="small text-muted">
                                Choose a topic, and I’ll send you fun math questions! ⭐
                            </div>
                        </div>
                    </div>

                    <!-- Grade + Topic Controls -->
                    <div class="row g-3 align-items-end mb-4">

                        <!-- Grade -->
                        <div class="col-md-4">
                            <label for="grade-select" class="form-label">Grade level</label>
                            <select id="grade-select" class="form-select">
                                <option value="1st grade">1st grade</option>
                                <option value="2nd grade">2nd grade</option>
                                <option value="3rd grade">3rd grade</option>
                                <option value="4th grade">4th grade</option>
                                <option value="5th grade">5th grade</option>
                            </select>
                        </div>

                        <!-- Topics -->
                        <div class="col-md-8">
                            <label class="form-label">Topic (click one)</label>

                            <div id="topic-grid" class="topic-grid mt-1"></div>

                            <input type="hidden" id="topic-input" name="topic">
                        </div>

                    </div>

                    <!-- Question area -->
                    <div id="question-section" class="mb-4 text-center">
                        <h2 id="question-text" class="fw-bold mb-3">
                            Pick a topic to get your first question!
                        </h2>
                        <p id="feedback-text" class="text-muted small"></p>
                    </div>

                    <!-- Choices -->
                    <div id="choices" class="d-grid gap-3 my-4"></div>

                    <!-- Next question -->
                    <div class="text-center mt-3">
                        <button id="next-btn" class="btn btn-primary px-4 py-2 fw-bold">
                            Next question
                        </button>
                    </div>

                </div>
            </div>

        </div>
    </div>
</main>

<script src="/js/practicebook.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

