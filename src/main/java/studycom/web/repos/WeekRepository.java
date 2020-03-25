package studycom.web.repos;
import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.WeeksDays.Week;


public interface WeekRepository  extends CrudRepository<Week, Integer> {
}
