package studycom.web.domain.WeeksDays;

import studycom.web.domain.UsersPart.Group;

import javax.persistence.*;
import java.sql.Time;
import java.util.Set;

@Entity
@Table(name = "weeks")
public class Week {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    private Integer weekNumb;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "week", cascade = CascadeType.ALL)
    @OrderBy("currentDay")
    private Set<Day> days;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Timetable timetable;

    public Week(Integer weekNumb) {
        this.weekNumb = weekNumb;
    }

    public Week() {
    }

    public Week(Integer weekNumb, Set<Day> days, Timetable timetable) {
        this.weekNumb = weekNumb;
        this.days = days;
        this.timetable = timetable;
    }

    public Week(Integer weekNumb, Set<Day> days) {
        this.weekNumb = weekNumb;
        this.days = days;
    }

    public Integer getId() {
        return id;
    }


    public Set<Day> getDays() {
        return days;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDays(Set<Day> days) {
        this.days = days;
    }

    public Integer getWeekNumb() {
        return weekNumb;
    }

    public void setWeekNumb(Integer weekNumb) {
        this.weekNumb = weekNumb;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    public Timetable getTimetable() {
        return timetable;
    }
}
