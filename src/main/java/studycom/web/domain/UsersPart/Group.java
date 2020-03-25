package studycom.web.domain.UsersPart;

import studycom.web.domain.WeeksDays.Timetable;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "groups")
public class Group  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Timetable timetable;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "group", cascade = CascadeType.ALL)
    private Set<User> users;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    public void setUsers(Set<User> users) {
       this.users = users;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Integer getId() {
        return id;
    }

    public Timetable getTimetable() {
        return timetable;
    }
}
