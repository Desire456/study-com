package studycom.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import studycom.web.domain.Lessons.Lesson;
import studycom.web.domain.Lessons.LessonType;
import studycom.web.domain.UsersPart.Group;
import studycom.web.domain.UsersPart.User;
import studycom.web.domain.WeeksDays.Day;
import studycom.web.domain.WeeksDays.DayType;
import studycom.web.domain.WeeksDays.Timetable;
import studycom.web.domain.WeeksDays.Week;
import studycom.web.repos.*;
import studycom.web.util.HashingClass;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @GetMapping("/timetable")
    public String showTimeTable(@ModelAttribute("user") User user) {
        return "timetable";
    }


    @GetMapping("/profile")
    public String showProfile(@ModelAttribute("user") User user) {
        return "profile";
    }

    @GetMapping("/home")
    public ModelAndView showHome(@ModelAttribute("user") User user) {
        ModelAndView model = new ModelAndView();
        user = userRepository.findById(user.getId()).get();
        model.addObject("user", user);
        model.addObject("tasks", taskRepository.findByUser(user));
        model.addObject("homeworks", user.getHomeWorks());
        model.setViewName("home");
        return model;
    }

    /*private Map<String, Set<HomeworkContent>> getMapFromSet(Set<Homework> homeworkSet) {
        Map<String, Set<HomeworkContent>> homeworkMap = new HashMap<>();
        for (Homework homework : homeworkSet) {
            homeworkMap.put(homework.getLessonName(), homework.getContent());
        }
        return homeworkMap;
    }*/


    @GetMapping("/addLesson")
    public String addLesson(@ModelAttribute("user") User user, @RequestParam(value = "weekNumber") Integer weekNumber,
                            @RequestParam(value = "day") String day,
                            @RequestParam(value = "time") String time, @RequestParam(value = "lessonType") String lessonType,
                            @RequestParam(value = "lessonName") String lessonName) {
        Timetable thisUserTimetable;
        Week currWeek;
        Day currDay;
        Lesson currLesson;
        if (user.getGroup().getTimetable() != null) {
            thisUserTimetable = user.getGroup().getTimetable();
            List<Week> currentWeek = weekRepository.findByTimetableAndWeekNumb(thisUserTimetable, weekNumber);
            if (!currentWeek.isEmpty()) {
                currWeek = currentWeek.get(0);
                List<Day> currentDay = dayRepository.findByCurrentDayAndWeek(DayType.valueOf(day), currWeek);
                if (!currentDay.isEmpty()) {
                    currDay = currentDay.get(0);
                    List<Lesson> currentLesson = lessonRepository.findByDayAndTime(currDay, time);
                    if (!currentLesson.isEmpty()) {
                        currLesson = currentLesson.get(0);
                        currLesson.setName(lessonName);
                        currLesson.setLessonType(LessonType.valueOf(lessonType));
                        lessonRepository.save(currLesson);
                    } else {
                        currDay.getLessons().add(new Lesson(lessonName, time, lessonType, currDay));
                        dayRepository.save(currDay);
                    }
                } else {
                    currDay = new Day(day, new HashSet<Lesson>(), currWeek);
                    currLesson = new Lesson(lessonName, time, lessonType, currDay);
                    currDay.getLessons().add(currLesson);
                    currWeek.getDays().add(currDay);
                    weekRepository.save(currWeek);
                }
            } else {
                currWeek = new Week(weekNumber, new HashSet<Day>(), thisUserTimetable);
                currDay = new Day(day, new HashSet<Lesson>(), currWeek);
                currLesson = new Lesson(lessonName, time, lessonType, currDay);
                currDay.getLessons().add(currLesson);
                currWeek.getDays().add(currDay);
                weekRepository.save(currWeek);
            }
        } else {
            currWeek = new Week(weekNumber, new HashSet<Day>());
            currDay = new Day(day, new HashSet<Lesson>(), currWeek);
            currLesson = new Lesson(lessonName, time, lessonType, currDay);
            currDay.getLessons().add(currLesson);
            currWeek.getDays().add(currDay);
            thisUserTimetable = new Timetable(new HashSet<Week>());
            currWeek.setTimetable(thisUserTimetable);
            thisUserTimetable.getWeeks().add(currWeek);
            user.getGroup().setTimetable(thisUserTimetable);
            userRepository.save(user);
        }
        return "redirect:/timetable";
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
        model.setViewName("home");
        return model;
    }

}
