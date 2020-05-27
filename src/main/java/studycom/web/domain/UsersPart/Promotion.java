package studycom.web.domain.UsersPart;

import javax.persistence.*;

@Entity
@Table(name = "promotion")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "group_id")
    private Group group;

    public String cause;
    public Integer promoterId;
    public int expNumber;

    public String getCause() {
        return cause;
    }

    public int getExpNumber() {
        return expNumber;
    }


    public void setCause(String cause) {
        this.cause = cause;
    }


    public void setExpNumber(int expNumber) {
        this.expNumber = expNumber;
    }

    public Promotion() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public Integer getPromoterId() {
        return promoterId;
    }

    public void setPromoterId(Integer promoterId) {
        this.promoterId = promoterId;
    }

    public Promotion(int expNumber, String cause, Group group, Integer promoterId) {
        this.cause = cause;
        this.expNumber = expNumber;
        this.group = group;
        this.promoterId = promoterId;
    }
}
