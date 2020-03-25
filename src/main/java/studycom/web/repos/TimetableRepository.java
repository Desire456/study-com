package studycom.web.repos;

import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.WeeksDays.Timetable;

public interface TimetableRepository extends CrudRepository<Timetable, Integer> {
}
