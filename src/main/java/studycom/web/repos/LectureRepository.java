package studycom.web.repos;

import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.Lessons.Lecture;

public interface LectureRepository extends CrudRepository<Lecture, Integer> {
}
