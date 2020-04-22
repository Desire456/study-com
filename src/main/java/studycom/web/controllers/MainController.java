package studycom.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import studycom.web.domain.Lessons.Lesson;
import studycom.web.domain.UsersPart.Group;
import studycom.web.domain.UsersPart.Task;
import studycom.web.domain.UsersPart.User;
import studycom.web.domain.WeeksDays.Day;
import studycom.web.domain.WeeksDays.DayType;
import studycom.web.domain.WeeksDays.Week;
import studycom.web.repos.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SessionAttributes(value = "user")
@Controller
public class MainController {

    @Autowired
    private TaskRepository taskRepository;

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
    public String showWeek(Map<String, Object> model) {
        model.put("WEEK1", new ArrayList<Day>(weekRepository.findByWeekNumb(1).get(0).getDays()));
        return "board";
    }


    @GetMapping("/registration")
    public String showRegistration() {
        return "registration";
    }

    @GetMapping("/group")
    public String showGroup() {
        return "group";
    }


    @GetMapping("/coin")
    public String showCoin() {
        return "coin";
    }

    @GetMapping("/timetable")
    public String showTimeTable() {
        return "timetable";
    }

    @GetMapping("/addLesson")
    public String showAddLess() {
        return "addLesson";
    }

    @GetMapping("/home")
    public ModelAndView showHome(@ModelAttribute("user") User user) {
        ModelAndView model = new ModelAndView();
        model.addObject("tasks", taskRepository.findByUser(user));
        model.setViewName("home");
        return model;
    }

    @GetMapping("/addUser")
    public ModelAndView home(@RequestParam(value = "name") String name, @RequestParam(value = "surname") String surname,
                             @RequestParam(value = "login") String login, @RequestParam(value = "password") String password,
                             @RequestParam(value = "group") String group,@RequestParam(value = "starostaCheckbox", required = false) String starostaCheck) {

        ModelAndView model = new ModelAndView();

        if (!userRepository.findByLogin(login).isEmpty()) {
            model.addObject("error", "Пользователь с таким логином уже существует");
            model.setViewName("registration");
            return model;
        }
        User user;
        Group thisUsersGroup;
        List<Group> thisUserGr = groupRepository.findByName(group);
        if (!thisUserGr.isEmpty()) {
            thisUsersGroup = thisUserGr.get(0);
            user = new User(login, name, surname, password, thisUsersGroup);
            if(starostaCheck!= null && thisUsersGroup.getStar() != null){
                    model.addObject("error", "В этой группе уже есть староста");
                    model.setViewName("registration");
                    return model;
            } else if (starostaCheck != null) {
                user.makeStar();
                thisUsersGroup.setStar(user);
                groupRepository.save(thisUsersGroup);
            } else {
                userRepository.save(user);
            }
        } else {
            user = new User(login, name, surname, password, group);
            if (starostaCheck != null) {
                thisUsersGroup = new Group();
                user.makeStar();
                thisUsersGroup.setStar(user);
                thisUsersGroup.setName(group);
                user.setGroup(thisUsersGroup);
                groupRepository.save(thisUsersGroup);
            } else {
                userRepository.save(user);
            }
        }
        model.addObject("user", user);
        model.setViewName("home");
        return model;
    }

    @GetMapping("/profile")
    public String showProfile(@ModelAttribute("user") User user) {
        return "profile";
    }

    @GetMapping("/enter")
    public ModelAndView showEnt() {
        ModelAndView model = new ModelAndView();
        model.addObject("user", new User());
        model.setViewName("enter");
        return model;
    }

    @GetMapping("/enterAction")
    public ModelAndView enter(@RequestParam(value = "login") String login, @RequestParam(value = "password") String password) {
        ModelAndView model = new ModelAndView();
        if (login == null || password == null) {
            model.addObject("error", "Exp");
            model.setViewName("enter");
            return model;
        }
        if (userRepository.findByLoginAndPassword(login, password).isEmpty()) {
            model.addObject("error", "Неверный логин или пароль");
            model.setViewName("enter");
            return model;
        }
        User user =  userRepository.findByLoginAndPassword(login, password).get(0);
        model.addObject("user", user);
        model.addObject("tasks", taskRepository.findByUser(user));
        model.setViewName("home");
        return model;
    }

}
