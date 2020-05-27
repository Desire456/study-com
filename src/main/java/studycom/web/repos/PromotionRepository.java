package studycom.web.repos;

import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.UsersPart.Promotion;


public interface PromotionRepository extends CrudRepository<Promotion, Integer> {
}
