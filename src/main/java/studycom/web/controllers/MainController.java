package studycom.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import studycom.web.domain.Lessons.Lesson;
import studycom.web.domain.UsersPart.User;
import studycom.web.domain.WeeksDays.Day;
import studycom.web.domain.WeeksDays.DayType;
import studycom.web.domain.WeeksDays.Week;
import studycom.web.repos.DayRepository;
import studycom.web.repos.LessonRepository;
import studycom.web.repos.UserRepository;
import studycom.web.repos.WeekRepository;

import java.util.ArrayList;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private WeekRepository weekRepository;

    @Autowired
    private DayRepository dayRepository;


    @GetMapping("/addLessonAction")
    public String addLesson(@RequestParam(value = "name") String name, @RequestParam(value = "lessonType") String lessonType
            , @RequestParam(value = "time") String time, @RequestParam(value = "day") String day,
                            @RequestParam(value = "week") String week) {

        if (weekRepository.findByWeekNumb(Integer.parseInt(week)).isEmpty()) {
            lessonRepository.save(new Lesson(name, time, lessonType, new Day(day, new Week(Integer.parseInt(week)))));
        } else {
            Week currentWeek = weekRepository.findByWeekNumb(Integer.parseInt(week)).get(0);
            if (dayRepository.findByCurrentDayAndWeek(DayType.valueOf(day), currentWeek).isEmpty()) {
                lessonRepository.save(new Lesson(name, time, lessonType,
                        new Day(day, currentWeek)));
            } else {
                lessonRepository.save(new Lesson(name, time, lessonType,
                        dayRepository.findByCurrentDayAndWeek(DayType.valueOf(day), currentWeek).get(0)));
            }
        }
        return "redirect:/addLesson";
    }


    @GetMapping("/board")
    public String showWeek(Map<String, Object> model){
        model.put("WEEK1", new ArrayList<Day>(weekRepository.findByWeekNumb(1).get(0).getDays()));
        return "board";
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

    @GetMapping("/enter")
    public String enter(Map<String, Object> model, @ModelAttribute("user") User user) {
        if (user.getName() == null || user.getSurname() == null || user.getPassword() == null) {
            return "enter";
        }
        if (userRepository.findByNameAndSurnameAndPassword(user.getName(), user.getSurname(), user.getPassword()).isEmpty()) {
            return "enter";
        }
        return "lk";
    }

}
