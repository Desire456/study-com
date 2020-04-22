package studycom.web.repos;
import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.WeeksDays.Timetable;
import studycom.web.domain.WeeksDays.Week;

import java.util.List;


public interface WeekRepository extends CrudRepository<Week, Integer> {
    List<Week> findByWeekNumb(Integer weekNumb);

    List<Week> findByTimetableAndWeekNumb(Timetable timetable, Integer weekNumber);
}
