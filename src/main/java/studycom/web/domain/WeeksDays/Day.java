package studycom.web.domain.WeeksDays;

import javax.persistence.*;
import studycom.web.domain.Lessons.Lesson;

import java.util.Set;

@Entity
@Table(name="days")
public class Day {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer Id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Week week;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "day", cascade = CascadeType.ALL)
    @OrderBy("time")
    private Set<Lesson> lessons;

    private DayType currentDay;

    public Day(String currentDay, Set<Lesson> lessons, Week week) {
        this.currentDay = DayType.valueOf(currentDay);
        this.week = week;
        this.lessons = lessons;
    }

    public Day(){}


    public Integer getId() {
        return Id;
    }

    public Set<Lesson> getLessons() {
        return lessons;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void setWeek(Week week) {
        this.week = week;
    }

    public void setCurrentDay(DayType currentDay) {
        this.currentDay = currentDay;
    }

    public DayType getCurrentDay() {
        return currentDay;
    }

    public Week getWeek() {
        return week;
    }
}
