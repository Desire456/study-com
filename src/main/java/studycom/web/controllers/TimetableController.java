package studycom.web.controllers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import studycom.web.domain.Lessons.Lesson;
import studycom.web.domain.UsersPart.Group;
import studycom.web.domain.UsersPart.User;
import studycom.web.domain.WeeksDays.Day;
import studycom.web.domain.WeeksDays.Timetable;
import studycom.web.domain.WeeksDays.Week;
import studycom.web.repos.*;

import java.util.ArrayList;
import java.util.HashSet;

@SessionAttributes(value = "user")
@Controller
public class TimetableController {
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



    @GetMapping("/addTimetable")
    public String showAddTimetable(@ModelAttribute("user") User user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        return "addTimetable";
    }

    @GetMapping("/timetable")
    public ModelAndView showTimetable(@ModelAttribute("user") User user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("timetableNew");
        return modelAndView;
    }

    @PostMapping("/addTimetable")
    public String addTimetable(@ModelAttribute("user") User user, @RequestParam(value = "numOfWeek") Integer numOfWeek,
                               @RequestParam(value = "timetable") String timetable) {
        JSONObject jsonObject = new JSONObject(timetable);
        JSONArray jsonArray = jsonObject.getJSONArray("content");
        Group currGroup = user.getGroup();
        Timetable timetableToSave = currGroup.getTimetable();
        Week currWeek;
        if (timetableToSave != null) {
            ArrayList<Week> weeks = new ArrayList<>(weekRepository.findByTimetableAndWeekNumb(timetableToSave,
                    numOfWeek));
            if (weeks.isEmpty()) {
                currWeek = new Week(numOfWeek, new HashSet<>(), timetableToSave);
                timetableToSave.getWeeks().add(currWeek);
                parseAndFill(jsonArray, currWeek);
                timetableRepository.save(timetableToSave);
            } else {
                currWeek = weeks.get(0);
                currWeek.setDays(new HashSet<>());
                parseAndFill(jsonArray, currWeek);
                weekRepository.save(currWeek);
            }
        } else {
            timetableToSave = new Timetable(new HashSet<>());
            currWeek = new Week(numOfWeek, new HashSet<>(), timetableToSave);
            timetableToSave.getWeeks().add(currWeek);
            parseAndFill(jsonArray, currWeek);
            currGroup.setTimetable(timetableToSave);
            groupRepository.save(currGroup);
        }
        return "addTimetable";
    }


    private void parseAndFill(JSONArray jsonArray, Week currWeek) {
        Day currDay;
        Lesson currLesson;
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject timetableForDay = jsonArray.getJSONObject(i);
            String dayName = timetableForDay.getString("day"); //получаем день недели
            JSONArray arrayOfLessons = timetableForDay.getJSONArray("timetableForDay"); //получаем массив уроков на день
            currDay = new Day(dayName, new HashSet<>(), currWeek);
            currWeek.getDays().add(currDay);
            for (int j = 0; j < arrayOfLessons.length(); j++) {
                JSONObject lesson = arrayOfLessons.getJSONObject(j);
                String lessonTime = lesson.getString("time");//получаем время урока
                String lessonName = lesson.getString("lessonName"); //получаем имя урока
                String lessonType = lesson.getString("lessonType"); //получаем тип урока
                currLesson = new Lesson(lessonName, lessonTime, lessonType, currDay);
                currDay.getLessons().add(currLesson);
            }
        }
    }
}
