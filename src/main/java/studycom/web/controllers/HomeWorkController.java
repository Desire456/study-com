package studycom.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import studycom.web.domain.UsersPart.Group;
import studycom.web.domain.UsersPart.Homework;
import studycom.web.domain.UsersPart.Task;
import studycom.web.domain.UsersPart.User;
import studycom.web.repos.GroupRepository;
import studycom.web.repos.UserRepository;

import java.util.ArrayList;
import java.util.List;

@SessionAttributes(value = "user")
@Controller
public class HomeWorkController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupRepository HomeworkRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/deleteWork")
    public String deleteWork(@ModelAttribute("user") User user, @RequestParam(value = "worksId") String ids) {
        ModelAndView model = new ModelAndView();
        List<Integer> listIds = this.parseStrIds(ids);
        for(Integer id : listIds) {
            HomeworkRepository.deleteById(id);
        }
        user = userRepository.findById(user.getId()).get();
        model.addObject("user", user);
        return "redirect:/home";
    }


    @GetMapping("/addHomework")
    public String addTask(@ModelAttribute("user") User user,
                          @RequestParam(value = "lesson") String lesson, @RequestParam(value = "task") String task) {
        Group currGroup = groupRepository.findById(user.getGroup().getId()).get();
        ArrayList<User> users = new ArrayList<>(currGroup.getUsers());
        for (User value : users) {
            Homework homework = new Homework(lesson, task, user);
            value.getHomeWorks().add(homework);
        }
        groupRepository.save(currGroup);
        return "redirect:/home";
    }

    private List<Integer> parseStrIds(String ids) {
        List<Integer> listIds = new ArrayList<>();
        String[] splitIds = ids.split(" ");
        for(String id : splitIds) {
            listIds.add(Integer.parseInt(id));
        }
        return listIds;
    }
}
