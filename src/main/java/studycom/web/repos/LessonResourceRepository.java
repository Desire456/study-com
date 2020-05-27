package studycom.web.repos;

import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.Lessons.Lesson;
import studycom.web.domain.Lessons.LessonResource;
import studycom.web.domain.UsersPart.Group;

import java.util.List;

public interface LessonResourceRepository extends CrudRepository<LessonResource, Integer> {
    List<LessonResource> findByLessonAndGroup(Lesson lesson, Group group);
}
