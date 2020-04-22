package studycom.web.domain.WeeksDays;
import studycom.web.domain.UsersPart.Group;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "timetables")
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "timetable", cascade = CascadeType.ALL)
    @OrderBy("weekNumb")
    private Set<Week> weeks;

    public Integer getId() {
        return id;
    }

    public Set<Week> getWeeks() {
        return weeks;
    }

    public void setId(Integer id) {
        this.id = id;
    }

   public void setWeeks(Set<Week> weeks) {
       this.weeks = weeks;
    }


}
