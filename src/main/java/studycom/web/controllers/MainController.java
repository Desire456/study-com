package studycom.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import studycom.web.domain.Lessons.Lesson;
import studycom.web.domain.UsersPart.Group;
import studycom.web.domain.UsersPart.User;
import studycom.web.domain.WeeksDays.Day;
import studycom.web.domain.WeeksDays.Week;
import studycom.web.repos.*;
import studycom.web.util.HashingClass;
import studycom.web.util.NumberOfStudyWeekCalculator;

import java.util.*;

@SessionAttributes(value = {"user", "timetableToday"})
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

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private HomeworkRepository homeworkRepository;


    @GetMapping("/inputLesson")
    public String showInput() {
        return "inputLesson";
    }

    @GetMapping("/registration")
    public String showRegistration() {
        return "registration";
    }

    @GetMapping("/group")
    public ModelAndView showGroup(@ModelAttribute("user") User user) {
        Set<User> groupUsers = user.getGroup().getUsers();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("group");
        modelAndView.addObject("groupUsers", groupUsers);
        return modelAndView;
    }


    @GetMapping("/coin")
    public String showCoin() {
        return "coin";
    }


    @GetMapping("/profile")
    public String showProfile(@ModelAttribute("user") User user) {
        return "profile";
    }

    @GetMapping("/home")
    public ModelAndView showHome(@ModelAttribute("user") User user, @ModelAttribute("timetableToday")
            Set<Lesson> lessons) {
        ModelAndView model = new ModelAndView();
        user = userRepository.findById(user.getId()).get();
        model.addObject("user", user);
        model.addObject("tasks", taskRepository.findByUser(user));
        model.addObject("homeworks", user.getHomeWorks());
        if (!lessons.isEmpty())
        model.addObject("timetableToday", lessons);
        model.setViewName("home");
        return model;
    }

    @GetMapping("/addUser")
    public ModelAndView home(@RequestParam(value = "name") String name, @RequestParam(value = "surname") String surname,
                             @RequestParam(value = "login") String login, @RequestParam(value = "password") String password,
                             @RequestParam(value = "group") String group, @RequestParam(value = "starostaCheckbox", required = false) String starostaCheck) {

        ModelAndView model = new ModelAndView();

        if (!userRepository.findByLogin(login).isEmpty()) {
            model.addObject("error", "Пользователь с таким логином уже существует");
            model.setViewName("registration");
            return model;
        }
        String hashPassword = HashingClass.hashPassword(password);
        User user;
        Group thisUsersGroup;
        List<Group> thisUserGr = groupRepository.findByName(group);
        if (!thisUserGr.isEmpty()) {
            thisUsersGroup = thisUserGr.get(0);
            user = new User(login, name, surname, hashPassword, thisUsersGroup);
            if (starostaCheck != null && thisUsersGroup.getStar() != null) {
                model.addObject("error", "В этой группе уже есть староста");
                model.setViewName("registration");
                return model;
            } else if (starostaCheck != null) {
                user.makeStar();
                thisUsersGroup.setStar(user);
                thisUsersGroup.getUsers().add(user);
                groupRepository.save(thisUsersGroup); //user сохранится при сохранении группы в котором есть староста
            } else {
                thisUsersGroup.getUsers().add(user);
                groupRepository.save(thisUsersGroup);
            }
        } else {
            user = new User(login, name, surname, hashPassword, group);
            thisUsersGroup = new Group();
            thisUsersGroup.setUsers(new HashSet<>());
            if (starostaCheck != null) {
                user.makeStar();
                thisUsersGroup.setStar(user);
                thisUsersGroup.setName(group);
                user.setGroup(thisUsersGroup);
                thisUsersGroup.getUsers().add(user);
                groupRepository.save(thisUsersGroup);
            } else {
                thisUsersGroup.getUsers().add(user);
                groupRepository.save(thisUsersGroup);
            }
        }
        model.addObject("user", user);
        Calendar calendar = Calendar.getInstance();
        List<Week> weeks = weekRepository.findByTimetable(user.getGroup().getTimetable());
        int numOfWeek = NumberOfStudyWeekCalculator.getNumberOfStudyWeek(calendar.get(Calendar.WEEK_OF_YEAR),
                weeks.size());
        Optional<Week> weekOpt = weeks.stream().filter(week1 -> week1.getWeekNumb() == numOfWeek).findFirst();
        if (weekOpt.isPresent()) {
            Week week = weekOpt.get();
            Set<Lesson> lessons = null;
            for (Day day : week.getDays()) {
                if (day.getNumberOfCurrentDay() == calendar.get(Calendar.DAY_OF_WEEK)) {
                    lessons = day.getLessons();
                }
            }
            Set<Lesson> sortedLessons = null;
            if (lessons != null) {
                sortedLessons = new TreeSet<>(
                        (lesson1, lesson2) -> {
                            Integer hourOfTime1 = Integer.parseInt(lesson1.getTime().split("\\.")[0]);
                            Integer hourOfTime2 = Integer.parseInt(lesson2.getTime().split("\\.")[0]);
                            return hourOfTime1.compareTo(hourOfTime2);
                        }
                );
                sortedLessons.addAll(lessons);
            }
            model.addObject("timetableToday", sortedLessons);
        } else model.addObject("timetableToday", new HashSet<>());
        model.setViewName("home");
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
    public ModelAndView enter(@RequestParam(value = "login") String login,
                              @RequestParam(value = "password") String password) {
        ModelAndView model = new ModelAndView();
        List<User> userList = userRepository.findByLogin(login);
        if (userList.isEmpty()) {
            model.addObject("error", "Неверный логин или пароль");
            model.setViewName("enter");
            return model;
        }
        User user = userRepository.findByLogin(login).get(0);
        if (!HashingClass.validatePassword(password, user.getPassword())) {
            model.addObject("error", "Неверный логин или пароль");
            model.setViewName("enter");
            return model;
        }
        model.addObject("user", user);
        model.addObject("tasks", taskRepository.findByUser(user));
        model.addObject("homeworks", user.getHomeWorks());
        Calendar calendar = Calendar.getInstance();
        List<Week> weeks = weekRepository.findByTimetable(user.getGroup().getTimetable());
        int numOfWeek = NumberOfStudyWeekCalculator.getNumberOfStudyWeek(calendar.get(Calendar.WEEK_OF_YEAR),
                weeks.size());
        Optional<Week> weekOpt = weeks.stream().filter(week1 -> week1.getWeekNumb() == numOfWeek).findFirst();
        if (weekOpt.isPresent()) {
            Week week = weekOpt.get();
            Set<Lesson> lessons = null;
            for (Day day : week.getDays()) {
                if (day.getNumberOfCurrentDay() == calendar.get(Calendar.DAY_OF_WEEK)) {
                    lessons = day.getLessons();
                }
            }
            Set<Lesson> sortedLessons = null;
            if (lessons != null) {
                sortedLessons = new TreeSet<>(
                        (lesson1, lesson2) -> {
                            Integer hourOfTime1 = Integer.parseInt(lesson1.getTime().split("\\.")[0]);
                            Integer hourOfTime2 = Integer.parseInt(lesson2.getTime().split("\\.")[0]);
                            return hourOfTime1.compareTo(hourOfTime2);
                        }
                );
                sortedLessons.addAll(lessons);
            }
            model.addObject("timetableToday", sortedLessons);
        }  else model.addObject("timetableToday", new HashSet<>());
        model.setViewName("home");
        return model;
    }

}
