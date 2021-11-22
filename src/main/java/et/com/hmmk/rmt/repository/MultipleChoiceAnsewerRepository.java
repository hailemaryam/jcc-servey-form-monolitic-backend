package et.com.hmmk.rmt.repository;

import et.com.hmmk.rmt.domain.MultipleChoiceAnsewer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MultipleChoiceAnsewer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MultipleChoiceAnsewerRepository
    extends JpaRepository<MultipleChoiceAnsewer, Long>, JpaSpecificationExecutor<MultipleChoiceAnsewer> {}
