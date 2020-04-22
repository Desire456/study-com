package studycom.web.repos;
import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.Lessons.Lesson;
import studycom.web.domain.WeeksDays.Day;

import java.util.List;


public interface LessonRepository extends CrudRepository<Lesson, Integer> {
    List<Lesson> findByDayAndTime(Day day, String time);
}
