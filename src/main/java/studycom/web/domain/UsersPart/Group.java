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
    @JoinColumn(name="timetable_id")
    private Timetable timetable;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="star_id")
    private User star;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "group", cascade = CascadeType.ALL)
    @OrderBy("name")
    private Set<User> users;

    public Group(String name){
        this.name = name;
    }

    public Group(){}

    private String name;

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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStar(User star) {
        this.star = star;
    }

    public User getStar() {
        return star;
    }
}
