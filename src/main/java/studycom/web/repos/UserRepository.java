package studycom.web.repos;

import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.UsersPart.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByLogin(String login);
    List<User> findByLoginAndPassword(String name, String password);
    List<User> findByNameAndSurnameAndPassword(String name, String surname, String password);
}
