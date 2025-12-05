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


    @GetMapping("/")
    public String root() {
        return "welcome";              // welcome.jte
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";              // welcome.jte
    }


    @GetMapping("/home")
    public String home(Model model) {
        String username = userContext.getCurrentUsername();
        if (username == null) {
            return "redirect:/login";
        }
        model.addAttribute("currentUser", username);
        return "home";                 // home.jte (mode selector)
    }


    @GetMapping("/learn")
    public String teacherMode(Model model) {
        String username = userContext.getCurrentUsername();
        if (username == null) {
            return "redirect:/login";
        }
        model.addAttribute("currentUser", username);
        return "Homepage";             // Homepage.jte (chat UI)
    }


    @GetMapping("/practicebook")
    public String practiceBook(Model model) {
        String username = userContext.getCurrentUsername();
        if (username == null) {
            return "redirect:/login";
        }
        model.addAttribute("currentUser", username);
        return "practicebook";         // practicebook.jte
    }

    @GetMapping("/sessions")
    public String sessions(Model model) {
        String username = userContext.getCurrentUsername();
        if (username == null) {
            return "redirect:/login";
        }
        model.addAttribute("currentUser", username);
        return "sessions";             // sessions.jte
    }
    @GetMapping("/contact")
    public String contact() {
        return "contact";              // contact.jte
    }
}




