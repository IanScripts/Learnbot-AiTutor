<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Sign Up</title>

    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/styleforlogin.css">
</head>
<body>
<div class="main-container">
    <div class="login-box">
        <div class="header"><h1>Create Account</h1></div>

        <form class="login-form" action="/register" method="POST">
            <div class="form-group">
                <label for="username">Username:</label>
                <input id="username" name="username" type="text"
                       required class="form-control" placeholder="Choose a username">
            </div>

            <div class="form-group">
                <label for="password">Password:</label>
                <input id="password" name="password" type="password"
                       required class="form-control" placeholder="Choose a password">
            </div>

            <div class="buttons-container">
                <button type="submit" class="btn">Sign Up</button>
                <a href="/login" class="btn">Back to Login</a>
            </div>
        </form>
    </div>
</div>

<div class="footer"><p>version x.x</p></div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
