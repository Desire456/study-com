package studycom.web.repos;
import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.WeeksDays.Day;
import studycom.web.domain.WeeksDays.DayType;
import studycom.web.domain.WeeksDays.Week;

import java.util.List;


public interface DayRepository  extends CrudRepository<Day, Integer>{
    List<Day> findByCurrentDayAndWeek(DayType currentDay, Week week);
}
