package studycom.web.domain.UsersPart;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Role role = Role.CASUAL;
    private String rank = "Новичок";
    private String login;
    private String name;
    private String surname;
    private String password;
    private String urlPhoto;
    private Integer level = 1;
    private Integer exp = 100;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = {CascadeType.MERGE})
    @OrderBy("lessonName")
    private Set<Homework> homeWorks;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Group group;

    public User(String login, String name, String surname, String password, String group) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.login = login;
        this.group = new Group();
        this.group.setName(group);
    }

    public User(String login, String name, String surname, String password, Group group) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.login = login;
        this.group = group;
    }


    public Set<Homework> getHomeWorks() {
        return homeWorks;
    }


    public void setHomeWorks(Set<Homework> homeWorks) {
        this.homeWorks = homeWorks;
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

    public String getStringRole() {
        switch (this.role) {
            case CASUAL:
                return "Студент";
            case STAR:
                return "Староста";
            case ADMIN:
                return "Админ";
        }
        return null;
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

    public int getExp() {
        return exp;
    }

    public void setExp(int xp) {
        this.exp = xp;
        this.updateLvl(xp);
    }

    public void addExp(int addition) {
        this.exp += addition;
        this.updateLvl(this.exp);
    }

    public void removeExp(int toRem) {
        this.exp -= toRem;
        this.updateLvl(this.exp);
    }

    /**
     * Количество экспириенса на текущем уровне до следующего
     */

    public int getFullExpOfCurrentLvl() {
        switch (this.level) {
            case 1:
                return 150;
            case 2:
                return 250;
            case 3:
                return 400;
            case 4:
                return 600;
            case 5:
                return 850;
            case 6:
                return 1150;
            case 7:
                return 1500;
            case 8:
                return 1900;
            case 9:
            case 10:
                return 2350;
            default:
                return 0;
        }
    }

    /**
     * Количество экспириенса для получения текущего уровня
     */

    public int getPrevLvlExp() {
        switch (this.level) {
            case 1:
                return 100;
            case 2:
                return 150;
            case 3:
                return 250;
            case 4:
                return 400;
            case 5:
                return 600;
            case 6:
                return 850;
            case 7:
                return 1150;
            case 8:
                return 1500;
            case 9:
                return 1900;
            case 10:
                return 2350;
            default:
                return 0;
        }
    }


    private void updateLvl(int exp) {
        if (exp < 150) {
            this.level = 1;
            this.rank = "Новичок";
        } else if (exp < 250) {
            this.level = 2;
            this.rank = "Ученик";
        } else if (exp < 400) {
            this.level = 3;
            this.rank = "Знаток";
        } else if (exp < 600) {
            this.level = 4;
            this.rank = "Мастер";
        } else if (exp < 850) {
            this.level = 5;
            this.rank = "Гуру";
        } else if (exp < 1150) {
            this.level = 6;
            this.rank = "Мыслитель";
        } else if (exp < 1500) {
            this.level = 7;
            this.rank = "Мудрец";
        } else if (exp < 1900) {
            this.level = 8;
            this.rank = "Оракул";
        } else if (exp < 2350) {
            this.level = 9;
            this.rank = "Исскусственный интеллект";
        } else {
            this.level = 10;
            this.rank = "Высший разум";
        }
    }


    public void makeStar() {
        this.role = Role.STAR;
    }

    public User() {
    }

}
