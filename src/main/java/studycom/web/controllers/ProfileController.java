package studycom.web.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import studycom.web.domain.UsersPart.*;
import studycom.web.repos.GroupRepository;
import studycom.web.repos.UserRepository;

import java.util.List;

@SessionAttributes(value = "user")
@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @PostMapping("/editProfile")
    public String editProfile(@ModelAttribute("user") User user, @RequestParam(value = "login") String login,
                              @RequestParam(value = "surname") String surname, @RequestParam(value = "name") String name,
                              @RequestParam(value = "password") String password, @RequestParam(value = "group") String group,
                              @RequestParam(value = "avatar") String avatar) {
        ModelAndView model = new ModelAndView();
        if (!login.equals("")) {
            user.setLogin(login);
        }
        if (!surname.equals("")) {
            user.setSurname(surname);
        }
        if (!name.equals("")) {
            user.setName(name);
        }
        if (!password.equals("")) {
            user.setPassword(password);
        }
        if (!group.equals("")) {
            Group thisUsersGr = groupRepository.findByName(user.getGroup().getName()).get(0);
            List<Group> groups = groupRepository.findByName(group);
            if (!groups.isEmpty()) {
                Group newGroup = groups.get(0);
                if (thisUsersGr.getStar().getId().equals(user.getId())) {
                    thisUsersGr.setStar(null);
                    if (newGroup.getStar() != null) {
                        model = new ModelAndView();
                        model.addObject("error", "В данный группе уже существует староста");
                        return "redirect:/profile";
                    } else {
                        newGroup.setStar(user);
                    }
                }
                thisUsersGr.getUsers().remove(user);
                groupRepository.save(thisUsersGr);
                user.setGroup(newGroup);
            } else {
                model = new ModelAndView();
                model.addObject("error", "Такой группы не существует");
                return "redirect:/profile";
            }
        }
        if (!avatar.equals("")) {
            user.setUrlPhoto(avatar);
        }
        userRepository.save(user);
        model.addObject("user", user);
        return "redirect:/profile";
    }
}
