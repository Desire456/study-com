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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Set<Week> weekSet = user.getGroup().getTimetable().getWeeks();
        Map<String, List<Lesson>> lessonMapByTime = this.parseTimetable(weekSet);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.addObject("timetable", lessonMapByTime);
        modelAndView.setViewName("timetableNew");
        return modelAndView;
    }

    private Map<String, List<Lesson>> parseTimetable(Set<Week> weekSet) {
        Week firstWeek = weekSet.stream().filter(week -> Objects.equals(week.getWeekNumb(), 1)).findFirst().get();
        Set<Day> daySet = firstWeek.getDays();
        List<Lesson> lessonList = new ArrayList<>();
        for(Day day : daySet) {
            lessonList.addAll(day.getLessons());
        }
        Map<String, List<Lesson>> lessonMapByTime = new HashMap<>();
        for (int i = 1; i <= 8; ++i) {
            final String time;
            switch (i) {
                case 1 : time = "8.00-9.35"; break;
                case 2 : time = "9.45-11.20"; break;
                case 3 : time = "11.30-13.05"; break;
                case 4 : time = "13.30-15.05"; break;
                case 5 : time = "15.15-16.50"; break;
                case 6 : time = "17.00-18.35"; break;
                case 7 : time = "18.45-20.15"; break;
                case 8 : time = "20.25-21.55"; break;
                default:
                    throw new IllegalStateException("Unexpected value: " + i);
            }
            List<Lesson> lessons = lessonList.stream().filter(lesson -> Objects.equals(lesson.getTime(), time))
                    .collect(Collectors.toList());
            lessonMapByTime.put(time, lessons);
        }
        Map<String, List<Lesson>> sortedMap = new TreeMap<>(
                (time1, time2) -> {
                    Integer hourOfTime1 = Integer.parseInt(time1.split("\\.")[0]);
                    Integer hourOfTime2 = Integer.parseInt(time2.split("\\.")[0]);
                    return hourOfTime1.compareTo(hourOfTime2);
                }
        );
        sortedMap.putAll(lessonMapByTime);
        return sortedMap;
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
