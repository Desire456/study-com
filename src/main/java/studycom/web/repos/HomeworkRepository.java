package studycom.web.repos;

import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.UsersPart.Homework;

public interface HomeworkRepository extends CrudRepository<Homework, Integer> {
}
