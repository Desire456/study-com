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
import studycom.web.domain.UsersPart.HomeworkContent;
import studycom.web.domain.UsersPart.User;
import studycom.web.repos.GroupRepository;
import studycom.web.repos.HomeworkRepository;
import studycom.web.repos.UserRepository;

import java.util.ArrayList;
import java.util.List;

@SessionAttributes(value = "user")
@Controller
public class HomeWorkController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private HomeworkRepository homeworkRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/deleteWork")
    public String deleteHomework(@ModelAttribute("user") User user, @RequestParam(value = "worksId") String ids) {
        ModelAndView model = new ModelAndView();
        List<Integer> listIds = this.parseStrIds(ids);
        for (Integer id : listIds) {
            homeworkRepository.deleteById(id);
        }
        user = userRepository.findById(user.getId()).get();
        model.addObject("user", user);
        return "redirect:/home";
    }


    @GetMapping("/addHomework")
    public String addHomework(@ModelAttribute("user") User user,
                              @RequestParam(value = "lesson") String lesson, @RequestParam(value = "task") String task) {
        ModelAndView model = new ModelAndView();
        List<Homework> homeworks = homeworkRepository.findByLessonNameAndUser(lesson, user);
        if (homeworks.isEmpty()) {
            Group currGroup = groupRepository.findById(user.getGroup().getId()).get();
            ArrayList<User> users = new ArrayList<>(currGroup.getUsers());
            for (User iterUser : users) {
                Homework homework = new Homework(lesson, iterUser);
                HomeworkContent content = new HomeworkContent(task);
                content.setHomework(homework);
                homework.getContent().add(content);
                iterUser.getHomeWorks().add(homework);
            }
            groupRepository.save(currGroup);
        } else {
            Homework homework = homeworks.get(0);
            HomeworkContent content = new HomeworkContent(task);
            content.setHomework(homework);
            homework.getContent().add(content);
            homeworkRepository.save(homework);
        }
        user = userRepository.findById(user.getId()).get();
        model.addObject("user", user);
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
