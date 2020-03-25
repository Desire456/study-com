package studycom.web.repos;

import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.Lessons.Practice;

public interface PracticeRepository extends CrudRepository<Practice, Integer> {
}
