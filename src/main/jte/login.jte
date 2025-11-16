<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Login</title>

    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/styleforlogin.css">
</head>
<body>
<div class="main-container">
    <div class="login-box">
        <div class="header"><h1>Login</h1></div>

        <form class="login-form" action="/login" method="POST">
            <div class="form-group">
                <label for="username">Username:</label>
                <input id="username" name="username" type="text"
                       required class="form-control" placeholder="Username">
            </div>

            <div class="form-group">
                <label for="password">Password:</label>
                <input id="password" name="password" type="password"
                       required class="form-control" placeholder="Password">
            </div>

            <div class="buttons-container">
                <button type="submit" class="btn">Login</button>
                <a href="/register" class="btn">Sign Up</a>
            </div>
        </form>
    </div>
</div>

<div class="footer"><p>version 11.114</p></div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>


