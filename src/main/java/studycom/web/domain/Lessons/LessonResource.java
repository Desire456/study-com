package studycom.web.domain.Lessons;

import studycom.web.domain.UsersPart.Group;

import javax.persistence.*;

@Entity
@Table(name = "lesson_resources")
public class LessonResource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Group group;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Lesson lesson;

    public LessonResource(String content, Group group, Lesson lesson) {
        this.content = content;
        this.group = group;
        this.lesson = lesson;
    }

    public LessonResource() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

}