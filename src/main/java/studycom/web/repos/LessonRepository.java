package studycom.web.repos;
import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.Lessons.Lesson;


public interface LessonRepository extends CrudRepository<Lesson, Integer> {
}
