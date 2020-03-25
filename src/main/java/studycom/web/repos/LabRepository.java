package studycom.web.repos;

import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.Lessons.Lab;

public interface LabRepository extends CrudRepository<Lab, Integer>{
}
