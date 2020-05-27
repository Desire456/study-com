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

    public Integer promoterId;
    public String cause;
    public String senderName;
    public int expNumber;

    public String getCause() {
        return cause;
    }

    public int getExpNumber() {
        return expNumber;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
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

    public Promotion(int expNumber, String senderName, String cause, Group group, Integer promoterId) {
        this.cause = cause;
        this.expNumber = expNumber;
        this.senderName = senderName;
        this.group = group;
        this.promoterId = promoterId;
    }
}
