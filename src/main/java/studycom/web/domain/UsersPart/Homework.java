package studycom.web.domain.UsersPart;

import javax.persistence.*;

@Entity
@Table(name = "homeworks")
public class Homework {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String content;
    private String lessonName;

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getContent() {
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
        this.content = content;
    }

    public Homework() {
    }


}
