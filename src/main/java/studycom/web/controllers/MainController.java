package studycom.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
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

@SessionAttributes(value = "user")
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
    public String showHome(){
        return "home";
    }

    @GetMapping("/addUser")
    public ModelAndView home(ModelAndView model, @RequestParam(value = "name") String name , @RequestParam(value = "surname") String surname,
                       @RequestParam(value = "login") String login, @RequestParam(value = "password") String password, @RequestParam(value = "group") String group) {

        if (name == null || surname == null ||
               password == null|| group == null|| login==null|| !userRepository.findByLogin(login).isEmpty()) {
            model.setViewName("home");
           return model;
        }

        User user = new User(login, name, surname,password,group);
        model.addObject("user",user);
        model.setViewName("enter");
        userRepository.save(user);
        return model;
    }


    @GetMapping("/enter")
    public ModelAndView showEnt(){
        ModelAndView model = new ModelAndView();
        model.addObject("user", new User());
        model.setViewName("enter");
        return model;
    }

    @GetMapping("/enterAction")
    public String enter(ModelAndView model, @RequestParam(value = "login") String login,@RequestParam(value = "password") String password) {
        if (login == null || password == null ) {
            return "enter";
        }
        if (userRepository.findByLoginAndPassword(login,password).isEmpty()) {
            return "enter";
        }
        return "lk";
    }

}
