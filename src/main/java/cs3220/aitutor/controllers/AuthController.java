package cs3220.aitutor.controllers;

import cs3220.aitutor.services.UserContext;
import cs3220.aitutor.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;


@Controller
public class AuthController {

    private final UserService userService;
    private final UserContext userContext;

    public AuthController(UserService userService, UserContext userContext) {
        this.userService = userService;
        this.userContext = userContext;
    }

    // --- LOGIN ---

    @GetMapping("/login")
    public String showLogin(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          Model model) {

        if (userService.authenticate(username, password)) {
            userContext.login(username);
            return "redirect:/home";
        }

        model.addAttribute("error", "Invalid username or password.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Clear everything about the current user
        session.invalidate();

        // Send them back to the welcome page
        return "redirect:/";
    }


    // --- SIGN UP ---

    @GetMapping("/register")
    public String showRegister(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String username,
                             @RequestParam String password,
                             Model model) {

        if (!userService.register(username, password)) {
            model.addAttribute("error", "Username already exists or invalid input.");
            return "register";
        }

        // auto-login after successful signup
        userContext.login(username);
        return "redirect:/home";
    }

    // --- LOG OUT ---

    @PostMapping("/logout")
    public String logout() {
        userContext.logout();
        return "redirect:/login";
    }
}
