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

    public Promotion(int expNumber, String senderName, String cause) {
        this.cause = cause;
        this.expNumber = expNumber;
        this.senderName = senderName;
    }
}
