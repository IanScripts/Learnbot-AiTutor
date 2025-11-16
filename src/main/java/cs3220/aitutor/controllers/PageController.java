package cs3220.aitutor.controllers;

import cs3220.aitutor.services.UserContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private final UserContext userContext;

    public PageController(UserContext userContext) {
        this.userContext = userContext;
    }

    // Public welcome page
    @GetMapping("/")
    public String welcome() {
        return "welcome"; // welcome.jte
    }

    @GetMapping("/home")
    public String home() {
        if (userContext.getCurrentUsername() == null) {
            return "redirect:/login";
        }
        return "Homepage"; // Homepage.jte
    }

    @GetMapping("/sessions")
    public String sessions() {
        if (userContext.getCurrentUsername() == null) {
            return "redirect:/login";
        }
        return "sessions"; // sessions.jte
    }

    @GetMapping("/practice-book")
    public String practiceBook() {
        if (userContext.getCurrentUsername() == null) {
            return "redirect:/login";
        }
        return "practicebook"; // practicebook.jte
    }

    @GetMapping("/settings")
    public String settings() {
        if (userContext.getCurrentUsername() == null) {
            return "redirect:/login";
        }
        return "settings"; // settings.jte
    }

    @GetMapping("/contact")
        public String contact() {
            return "contact";  // contact.jte
        }
    }



