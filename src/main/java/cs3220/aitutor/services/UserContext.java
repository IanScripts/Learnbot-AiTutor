package cs3220.aitutor.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class UserContext {

    private final HttpServletRequest request;

    public UserContext(HttpServletRequest request) {
        this.request = request;
    }

    // Get the current username, or null if not logged in
    public String getCurrentUsername() {
        HttpSession session = request.getSession(false);
        if (session == null) return null;

        Object username = session.getAttribute("username");
        return username != null ? username.toString() : null;
    }

    // Call this after successful login
    public void login(String username) {
        HttpSession session = request.getSession(true);
        session.setAttribute("username", username);
    }

    // Call this on logout
    public void logout() {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}

