package studycom.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {

    @PostMapping("/editProfile")
    public String editProfile() {
        return "redirect:/profile";
    }
}
