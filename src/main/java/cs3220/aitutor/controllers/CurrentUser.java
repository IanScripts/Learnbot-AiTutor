package cs3220.aitutor.controllers;

import cs3220.aitutor.services.UserContext;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CurrentUser {

    private final UserContext userContext;

    public CurrentUser(UserContext userContext) {
        this.userContext = userContext;
    }

    // This will add "currentUser" to the model for ALL controllers/views
    @ModelAttribute("currentUser")
    public String currentUser() {
        return userContext.getCurrentUsername();
    }
}
