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
import studycom.web.repos.HomeworkContentRepository;
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
    private HomeworkContentRepository homeworkContentRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/deleteWork")
    public String deleteHomework(@ModelAttribute("user") User user, @RequestParam(value = "works") String ids) {
        List<Integer> listIds = this.parseStrIds(ids);
        for (Integer id : listIds) {
            HomeworkContent currHomeWorkContent = homeworkContentRepository.findById(id).get();
            Homework currHomework = homeworkRepository.findById(currHomeWorkContent.getHomework().getId()).get();
            homeworkContentRepository.deleteById(id);
            if (currHomework.getContent().size() == 1) {
                homeworkRepository.deleteById(currHomework.getId());
            }
        }
        return "redirect:/home";    
    }


    @GetMapping("/addHomework")
    public String addHomework(@ModelAttribute("user") User user, @RequestParam(value = "lesson") String lesson,
                              @RequestParam(value = "task") String task) {
        List<Homework> homeworks = homeworkRepository.findByLessonNameAndUser(lesson, user);
        Group currGroup = groupRepository.findById(user.getGroup().getId()).get();
        ArrayList<User> users = new ArrayList<>(currGroup.getUsers());
        if (homeworks.isEmpty()) {
            for (User iterUser : users) {
                Homework homework = new Homework(lesson, iterUser);
                HomeworkContent content = new HomeworkContent(task);
                content.setHomework(homework);
                homework.getContent().add(content);
                iterUser.getHomeWorks().add(homework);
            }
        } else {

            for (User iterUser : users) {
                List<Homework> homeworkList = homeworkRepository.findByLessonNameAndUser(lesson, iterUser);
                if (homeworkList.isEmpty()) {
                    Homework homework = new Homework(lesson, iterUser);
                    HomeworkContent content = new HomeworkContent(task);
                    content.setHomework(homework);
                    homework.getContent().add(content);
                    iterUser.getHomeWorks().add(homework);
                } else {
                    Homework homework = homeworkList.get(0);
                    HomeworkContent content = new HomeworkContent(task);
                    content.setHomework(homework);
                    homework.getContent().add(content);
                }
            }
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
