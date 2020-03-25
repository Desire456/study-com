package studycom.web.repos;

import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.UsersPart.User;

public interface UserRepository extends CrudRepository<User, Integer> {



}
