package studycom.web.repos;

import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.UsersPart.Homework;
import studycom.web.domain.UsersPart.User;

import java.util.List;

public interface HomeworkRepository extends CrudRepository<Homework, Integer> {
    List<Homework> findByLessonNameAndUser(String lessonName, User user);
}
