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

    private PromotionCause cause;
    private Integer promoterId;
    private int expNumber;
    private String senderName;

    public int getExpNumber() {
        return expNumber;
    }

    public void setExpNumber(int expNumber) {
        this.expNumber = expNumber;
    }

    public Promotion() {
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
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

    public PromotionCause getCause() {
        return cause;
    }

    public void setCause(PromotionCause cause) {
        this.cause = cause;
    }

    public Promotion(int expNumber, String senderName, String cause, Group group, Integer promoterId) {
        this.cause = PromotionCause.valueOf(cause);
        this.senderName = senderName;
        this.expNumber = expNumber;
        this.group = group;
        this.promoterId = promoterId;
    }
}
