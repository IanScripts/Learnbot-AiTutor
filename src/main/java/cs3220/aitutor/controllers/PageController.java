package cs3220.aitutor.controllers;

import cs3220.aitutor.services.UserContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private final UserContext userContext;

    public PageController(UserContext userContext) {
        this.userContext = userContext;
    }

    /**
     * Root landing route.
     * ALWAYS shows welcome.jte, even if the user is already logged in.
     * Access to tutor / practicebook still requires login.
     */
    @GetMapping("/")
    public String root() {
        return "welcome";              // welcome.jte
    }

    /**
     * Optional explicit /welcome route (same as "/").
     */
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";              // welcome.jte
    }

    /**
     * HOME screen – mode selector (Teacher vs Game).
     * Requires login.
     */
    @GetMapping("/home")
    public String home(Model model) {
        String username = userContext.getCurrentUsername();
        if (username == null) {
            return "redirect:/login";
        }
        model.addAttribute("currentUser", username);
        return "home";                 // home.jte (mode selector)
    }

    /**
     * Teacher Mode – AI chat tutor.
     * Requires login.
     */
    @GetMapping("/learn")
    public String teacherMode(Model model) {
        String username = userContext.getCurrentUsername();
        if (username == null) {
            return "redirect:/login";
        }
        model.addAttribute("currentUser", username);
        return "Homepage";             // Homepage.jte (chat UI)
    }

    /**
     * Game Mode – Practice Book.
     * Requires login.
     */
    @GetMapping("/practicebook")
    public String practiceBook(Model model) {
        String username = userContext.getCurrentUsername();
        if (username == null) {
            return "redirect:/login";
        }
        model.addAttribute("currentUser", username);
        return "practicebook";         // practicebook.jte
    }

    /**
     * Sessions list – requires login.
     */
    @GetMapping("/sessions")
    public String sessions(Model model) {
        String username = userContext.getCurrentUsername();
        if (username == null) {
            return "redirect:/login";
        }
        model.addAttribute("currentUser", username);
        return "sessions";             // sessions.jte
    }
    /**
     * Contact page – public.
     */
    @GetMapping("/contact")
    public String contact() {
        return "contact";              // contact.jte
    }
}




