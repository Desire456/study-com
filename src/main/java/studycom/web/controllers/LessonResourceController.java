package studycom.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import studycom.web.domain.Lessons.LessonResource;
import studycom.web.domain.UsersPart.User;
import studycom.web.repos.LessonRepository;
import studycom.web.repos.LessonResourceRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@SessionAttributes(value = "user")
@Controller
public class LessonResourceController {
    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    LessonResourceRepository lessonResourceRepository;

    @GetMapping("/addLessonResource")
    public ModelAndView addLessonResource(@ModelAttribute("user") User user, @RequestParam(value = "content") String
            content, @RequestParam(value = "lessonName") String lessonName, HttpServletResponse response) {
        LessonResource toSave = new LessonResource(content, user.getGroup(), lessonName);
        toSave.setId(user.getId());
        LessonResource lessonResource = lessonResourceRepository.save(toSave);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("timetable");
        response.addHeader("idLessonResource", String.valueOf(lessonResource.getId()));
        return modelAndView;
    }

    @GetMapping("/deleteLessonResource")
    public ModelAndView deleteLessonResource(@ModelAttribute("user") User user, @RequestParam(value =
    "ids") String ids) {
        List<Integer> listIds = this.parseStrToList(ids);
        for(Integer id : listIds) {
            lessonResourceRepository.deleteById(id);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/timetable");
        return modelAndView;
    }

    private List<Integer> parseStrToList(String ids) {
        String[] arrIds = ids.split(" ");
        List<Integer> listIds = new ArrayList<>();
        for (String id : arrIds) {
            listIds.add(Integer.parseInt(id));
        }
        return listIds;
    }
}
