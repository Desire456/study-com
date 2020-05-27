package studycom.web.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import studycom.web.domain.Lessons.Lesson;
import studycom.web.domain.Lessons.LessonResource;
import studycom.web.domain.UsersPart.User;
import studycom.web.repos.LessonRepository;
import studycom.web.repos.LessonResourceRepository;

@SessionAttributes(value = "user")
@Controller
public class LessonResourceController {
    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    LessonResourceRepository lessonResourceRepository;

    @GetMapping("/addLessonResource")
    public ModelAndView addLessonResource(@ModelAttribute("user") User user, @RequestParam(value = "content") String
            content, @RequestParam(value = "lessonName") String lessonName) {
        Lesson lesson = lessonRepository.findByName(lessonName).get(0);
        lessonResourceRepository.save(new LessonResource(content, user.getGroup(), lesson));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("timetable");
        return modelAndView;
    }
}
