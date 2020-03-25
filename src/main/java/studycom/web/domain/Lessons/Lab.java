package studycom.web.domain.Lessons;

import javax.persistence.*;

@Entity
@Table(name="labs")
public class Lab  implements  Lesson{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
