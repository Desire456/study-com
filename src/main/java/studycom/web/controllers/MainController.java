package studycom.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import studycom.web.domain.Lessons.Lesson;
import studycom.web.domain.UsersPart.Group;
import studycom.web.domain.UsersPart.User;
import studycom.web.domain.WeeksDays.Day;
import studycom.web.domain.WeeksDays.DayType;
import studycom.web.domain.WeeksDays.Week;
import studycom.web.repos.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SessionAttributes(value = "user")
@Controller
public class MainController {

    @Autowired
    private GroupRepository groupRepository;

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


    @GetMapping("/registration")
    public String showRegistration() {
        return "registration";
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
            model.setViewName("registration");
           return model;
        }
        User user;
        List<Group> thisUserGr = groupRepository.findByName(group);
        if(!thisUserGr.isEmpty()){
            user = new User(login, name , surname, password, thisUserGr.get(0));
        }
        else{
            user = new User(login, name, surname,password,group);
        }
        model.addObject("user",user);
        model.setViewName("home");
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
    public ModelAndView  enter(@RequestParam(value = "login") String login,@RequestParam(value = "password") String password) {
        ModelAndView model = new ModelAndView();
        if (login == null || password == null ) {
            model.setViewName("enter");
            return model;
        }
        if (userRepository.findByLoginAndPassword(login,password).isEmpty()) {
            model.setViewName("enter");
            return model;
        }
        model.addObject("user", userRepository.findByLoginAndPassword(login,password).get(0));
        model.setViewName("home");
        return model;
    }

}
