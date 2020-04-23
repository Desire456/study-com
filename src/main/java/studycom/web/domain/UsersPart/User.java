package studycom.web.domain.UsersPart;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private Role role = Role.CASUAL;
    private String login;
    private String name;
    private String surname;
    private String password;
    private String urlPhoto;
    private Integer level=0;
    private Integer exp =0;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Group group;

    public User(String login , String name , String surname , String password , String group){
        this.name = name ;
        this.surname = surname;
        this.password = password;
        this.login = login;
        this.group = new Group();
        this.group.setName(group);
    }

    public User(String login , String name , String surname , String password ,Group group){
        this.name = name ;
        this.surname = surname;
        this.password = password;
        this.login = login;
        this.group = group;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getSurname() {
        return surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getLogin() {
        return login;
    }


    public void setLogin(String login) {
        this.login = login;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void lvlUp(){
        ++level;
    }

    public void lvlDown(){
        --level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int xp) {
        this.exp = xp;
        level = xp/100;
    }

    public void addExp (int addition){
        exp+=addition;
        level = exp/100;
    }

    public void removeExp( int toRem){
        exp-=toRem;
        level=exp/100;
    }


    public void makeStar(){
        this.role = Role.STAR;
    }
    public User(){}

}
