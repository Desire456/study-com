package studycom.web.domain.UsersPart;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import studycom.web.domain.WeeksDays.Week;

import javax.persistence.*;

@Entity
@Table(name = "homeWorkContents")
public class HomeworkContent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Homework homework;


    public void setHomework(Homework homework) {
        this.homework = homework;
    }

    public Homework getHomework() {
        return homework;
    }

    public Integer getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public HomeworkContent() {
    }

    public HomeworkContent(String content) {
        this.content = content;
    }
}
