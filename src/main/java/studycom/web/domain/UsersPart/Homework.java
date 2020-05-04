package studycom.web.domain.UsersPart;


import studycom.web.domain.WeeksDays.Week;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "homeWorks")
public class Homework {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ElementCollection
    private List<String> content;
    private String lessonName;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public User getUser() {
        return user;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public List<String> getContent() {
        return content;
    }

    public Integer getId() {
        return id;
    }

    public String getLessonName() {
        return lessonName;
    }


    public Homework(String lessonName, String content) {
        this.lessonName = lessonName;
        this.content = new ArrayList<>();
        this.content.add(content);
    }

    public Homework(String lessonName, List<String> content) {
        this.lessonName = lessonName;
        this.content = content;
    }

    public Homework() {
    }
}
