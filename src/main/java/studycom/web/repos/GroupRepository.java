package studycom.web.repos;

import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.UsersPart.Group;

import java.util.List;

public interface GroupRepository extends CrudRepository<Group, Integer> {
    List<Group> findByName(String name);
}
