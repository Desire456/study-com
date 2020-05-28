package studycom.web.repos;

import org.springframework.data.repository.CrudRepository;
import studycom.web.domain.UsersPart.Group;
import studycom.web.domain.UsersPart.Promotion;

import java.util.Set;


public interface PromotionRepository extends CrudRepository<Promotion, Integer> {
    Set<Promotion> getByGroup(Group group);
}
