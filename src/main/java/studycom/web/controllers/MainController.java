package studycom.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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



    @GetMapping("/deleteTask")
    public String deleteTask (@RequestParam( value = "taskId") Integer id){
        taskRepository.deleteById(id);
        return "redirect:/home";
    }


    @GetMapping("/addTask")
    public String addTask(@ModelAttribute("user") User user,
                          @RequestParam(value = "taskName") String taskName) {
        ModelAndView model = new ModelAndView();
        Task task = new Task(taskName, user);
        task.setId(user.getId());
        taskRepository.save(task);
        return "redirect:/home";
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
                             @RequestParam(value = "group") String group,@RequestParam(value = "starostaCheckbox") String starostaCheck) {

        ModelAndView model = new ModelAndView();
        if (name == null || surname == null ||
                password == null || group == null || login == null || !userRepository.findByLogin(login).isEmpty()) {
            model.setViewName("registration");
            return model;
        }
        User user;
        List<Group> thisUserGr = groupRepository.findByName(group);
        if (!thisUserGr.isEmpty()) {
            Group thisUsersGroup = thisUserGr.get(0);
            user = new User(login, name, surname, password, thisUsersGroup);
            if(starostaCheck!= null){
                user.makeStar();
                if(thisUsersGroup.getStar()==null) {
                    thisUsersGroup.setStar(user);
                }
            }
        } else {
            user = new User(login, name, surname, password, group);
            if(starostaCheck!= null){
                user.makeStar();
            }
        }
        model.addObject("user", user);
        model.setViewName("home");
        userRepository.save(user);
        return model;
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
            model.setViewName("enter");
            return model;
        }
        if (userRepository.findByLoginAndPassword(login, password).isEmpty()) {
            model.setViewName("enter");
            return model;
        }
        User user =  userRepository.findByLoginAndPassword(login, password).get(0);
        model.addObject("user", user);
        model.addObject("tasks", taskRepository.findByUser(user));
        model.setViewName("home");
        model.setViewName("home");
        return model;
    }

}
