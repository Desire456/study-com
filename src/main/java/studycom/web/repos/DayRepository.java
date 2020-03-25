package studycom.web.repos;
import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.WeeksDays.Day;


public interface DayRepository  extends CrudRepository<Day, Integer>{
}
