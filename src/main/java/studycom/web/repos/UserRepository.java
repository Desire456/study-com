package studycom.web.repos;

import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.UsersPart.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findByNameAndSurnameAndPassword(String name, String surname, String password);
}
