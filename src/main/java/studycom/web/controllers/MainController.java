package studycom.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import studycom.web.domain.User;
import studycom.web.repos.UserRepository;

import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/home")
    public String home(Map<String, Object> model , @ModelAttribute("user") User user) {
        if(user.getName()==null||user.getSurname() == null||user.getPassword()==null){
            return "home";
        }
        userRepository.save(user);
        return "lk";
    }

}
