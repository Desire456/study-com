package studycom.web.domain.Lessons;

import studycom.web.domain.WeeksDays.Day;

import javax.persistence.*;

@Entity
@Table(name="lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String name;

    private LessonType lessonType;
    private String time;


    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Day day;

    public Lesson(String name, String time , String lessonType, Day day){
        this.name = name;
        this.lessonType = LessonType.valueOf(lessonType);
        this.day = day;
    }

    public Lesson(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Day getDay() {
        return day;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public void setLessonType(LessonType lessonType) {
        this.lessonType = lessonType;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
