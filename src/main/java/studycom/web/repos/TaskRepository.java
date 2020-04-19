package studycom.web.repos;
import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.UsersPart.Task;
import studycom.web.domain.UsersPart.User;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Integer> {
    List<Task> findByUser (User user);
}
