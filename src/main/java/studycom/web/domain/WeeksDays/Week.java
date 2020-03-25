package studycom.web.domain.WeeksDays;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "weeks")
public class Week {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    private Integer weekNumb;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "week", cascade = CascadeType.ALL)
    private Set<Day> days;

    public Week(Integer weekNumb) {
        this.weekNumb = weekNumb;
    }

    public Week() {
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
}
