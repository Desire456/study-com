package studycom.web.controllers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import studycom.web.domain.Lessons.Lesson;
import studycom.web.domain.Lessons.LessonResource;
import studycom.web.domain.UsersPart.Group;
import studycom.web.domain.UsersPart.User;
import studycom.web.domain.WeeksDays.Day;
import studycom.web.domain.WeeksDays.Timetable;
import studycom.web.domain.WeeksDays.Week;
import studycom.web.repos.*;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private LessonResourceRepository lessonResourceRepository;


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
        modelAndView.setViewName("timetable");
        if (user.getGroup().getTimetable() != null) {
            Set<Week> weekSet = user.getGroup().getTimetable().getWeeks();
            modelAndView.addObject("numberOfWeeks", weekSet.size());
            modelAndView.addObject("timetable", this.parseTimetable(weekSet));
            List<LessonResource> lessonResources = lessonResourceRepository.findByGroup(user.getGroup());
            modelAndView.addObject("lessonResources", this.parseLessonResources(lessonResources));
        }
        return modelAndView;
    }

    private JSONObject parseLessonResources(List<LessonResource> lessonResources) {
        JSONObject object = new JSONObject();
        JSONArray jLessonResources = new JSONArray();
        for (LessonResource lessonResource : lessonResources) {
            JSONObject jLessonResource = new JSONObject();
            jLessonResource.put("lessonName", lessonResource.getLesson());
            jLessonResource.put("content", lessonResource.getContent());
            jLessonResources.put(jLessonResource);
        }
        object.put("lessonResources", jLessonResources);
        return object;
    }

    private JSONObject parseTimetable(Set<Week> weekSet) {
        JSONObject fullTimetable = new JSONObject();
        JSONArray jArrayOfWeeks = new JSONArray();
        for (int weekNum = 1; weekNum <= 4; ++weekNum) {
            final int weekNumber = weekNum;
            JSONObject jWeek = new JSONObject();
            jWeek.put("numOfWeek", weekNum);
            Optional<Week> optWeek = weekSet.stream().filter(week1 -> Objects.equals(week1.getWeekNumb(),
                        weekNumber)).findFirst();
            if (optWeek.isPresent()) {
                JSONArray jLessonsByTime = new JSONArray();
                Week week = optWeek.get();
                Set<Day> daySet = week.getDays();
                List<Lesson> lessonList = new ArrayList<>();
                for (Day day : daySet) {
                    lessonList.addAll(day.getLessons());
                }
                Map<String, List<Lesson>> lessonMapByTime = new HashMap<>();
                for (int i = 1; i <= 8; ++i) {
                    final String time;
                    switch (i) {
                        case 1:
                            time = "8.00-9.35";
                            break;
                        case 2:
                            time = "9.45-11.20";
                            break;
                        case 3:
                            time = "11.30-13.05";
                            break;
                        case 4:
                            time = "13.30-15.05";
                            break;
                        case 5:
                            time = "15.15-16.50";
                            break;
                        case 6:
                            time = "17.00-18.35";
                            break;
                        case 7:
                            time = "18.45-20.15";
                            break;
                        case 8:
                            time = "20.25-21.55";
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + i);
                    }
                    List<Lesson> lessons = lessonList.stream().filter(lesson -> Objects.equals(lesson.getTime(), time))
                            .collect(Collectors.toList());
                    if (lessons.size() != 0) lessonMapByTime.put(time, lessons);
                }
                Map<String, List<Lesson>> sortedMap = new TreeMap<>(
                        (time1, time2) -> {
                            Integer hourOfTime1 = Integer.parseInt(time1.split("\\.")[0]);
                            Integer hourOfTime2 = Integer.parseInt(time2.split("\\.")[0]);
                            return hourOfTime1.compareTo(hourOfTime2);
                        }
                );
                sortedMap.putAll(lessonMapByTime);
                for (String key : sortedMap.keySet()) {
                    JSONObject jDay = new JSONObject();
                    jDay.put("time", key);
                    List<Lesson> lessons = sortedMap.get(key);
                    if (lessons.size() != 6) {
                        while (lessons.size() != 6) {
                            lessons.add(new Lesson("-", key, "PRACTICE", new Day()));
                        }
                    }
                    JSONArray jLessons = new JSONArray();
                    for(Lesson lesson : lessons) {
                        JSONObject jLesson = new JSONObject();
                        jLesson.put("name", lesson.getName());
                        jLesson.put("type", lesson.getLessonType().getNameType());
                        jLessons.put(jLesson);
                    }
                    jDay.put("day", jLessons);
                    jLessonsByTime.put(jDay);
                }
                jWeek.put("week", jLessonsByTime);
            }
            else jWeek.append("timetable", null);
            jArrayOfWeeks.put(jWeek);
        }
        fullTimetable.put("fullTimetable", jArrayOfWeeks);
        return fullTimetable;
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
