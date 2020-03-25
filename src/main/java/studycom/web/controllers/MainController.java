package studycom.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import studycom.web.domain.Lessons.Lesson;
import studycom.web.domain.UsersPart.User;
import studycom.web.repos.LessonRepository;
import studycom.web.repos.UserRepository;

import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;


    @GetMapping("/addLessonAction")
    public String addLesson(@RequestParam(value = "name") String name, @RequestParam(value = "lessonType") String lessonType
            , @RequestParam(value = "time") String time, @RequestParam(value = "day") String day,
                            @RequestParam(value = "week") String week) {
        //проверить добавить еее круто
        return "redirect:/addLesson";
    }

    @GetMapping("/addLesson")
    public String showAddLess() {
        return "addLesson";
    }

    @GetMapping("/home")
    public String home(Map<String, Object> model, @ModelAttribute("user") User user) {
        if (user.getName() == null || user.getSurname() == null || user.getPassword() == null) {
            return "home";
        }
        userRepository.save(user);
        return "lk";
    }

}
