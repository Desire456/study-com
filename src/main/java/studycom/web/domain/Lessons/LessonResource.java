package studycom.web.domain.Lessons;

import studycom.web.domain.UsersPart.Group;

import javax.persistence.*;

@Entity
@Table(name = "lesson_resources")
public class LessonResource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "group_id")
    private Group group;


    private String lesson;

    public LessonResource(String content, Group group, String lesson) {
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

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

}
