package et.com.hmmk.rmt.repository;

import et.com.hmmk.rmt.domain.QuestionChoice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the QuestionChoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionChoiceRepository extends JpaRepository<QuestionChoice, Long>, JpaSpecificationExecutor<QuestionChoice> {}
