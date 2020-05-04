package studycom.web.domain.UsersPart;


import studycom.web.domain.WeeksDays.Day;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "homeWorks")
public class Homework {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "homework", cascade = {CascadeType.MERGE}, orphanRemoval = true)
    @OrderBy("content")
    private Set<HomeworkContent> content;


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

    public Integer getId() {
        return id;
    }

    public String getLessonName() {
        return lessonName;
    }


    public Homework(String lessonName, User user) {
        this.lessonName = lessonName;
        this.content = new HashSet<HomeworkContent>();
        this.user = user;
    }

    public Set<HomeworkContent> getContent() {
        return content;
    }

    public void setContent(Set<HomeworkContent> content) {
        this.content = content;
    }

    public Homework() {
    }
}
