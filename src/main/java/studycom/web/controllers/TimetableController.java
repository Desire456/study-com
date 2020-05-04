package studycom.web.controllers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TimetableController {
    @GetMapping("/addTimetable")
    public String showTimetable() {
        return "addTimetable";
    }

    @PostMapping("/addTimetable")
    public String addTimetable(@RequestParam(value = "numOfWeek") int numOfWeek, @RequestParam(value = "timetable") String timetable) {
        JSONObject jsonObject = new JSONObject(timetable);
        JSONArray jsonArray = jsonObject.getJSONArray("content");
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject timetableForDay = jsonArray.getJSONObject(i);
            timetableForDay.getString("day"); //получаем день недели
            JSONArray arrayOfLessons = timetableForDay.getJSONArray("timetableForDay"); //получаем массив уроков на день
            for (int j = 0; j < arrayOfLessons.length(); j++) {
                JSONObject lesson = arrayOfLessons.getJSONObject(j);
                lesson.getString("time");//получаем время урока
                lesson.getString("lessonName"); //получаем имя урока
                lesson.getString("lessonType"); //получаем тип урока
            }
        }
        return "addTimetable";
    }
}
