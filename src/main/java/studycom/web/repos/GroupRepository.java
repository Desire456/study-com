package studycom.web.repos;

import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.UsersPart.Group;

public interface GroupRepository extends CrudRepository<Group, Integer> {
}
