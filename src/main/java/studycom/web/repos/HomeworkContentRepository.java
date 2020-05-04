package studycom.web.repos;

import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.UsersPart.Homework;
import studycom.web.domain.UsersPart.HomeworkContent;

public interface HomeworkContentRepository extends CrudRepository<HomeworkContent, Integer> {
}
