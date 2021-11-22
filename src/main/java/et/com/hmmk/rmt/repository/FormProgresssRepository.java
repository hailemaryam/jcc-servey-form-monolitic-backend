package et.com.hmmk.rmt.repository;

import et.com.hmmk.rmt.domain.FormProgresss;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FormProgresss entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormProgresssRepository extends JpaRepository<FormProgresss, Long>, JpaSpecificationExecutor<FormProgresss> {
    @Query("select formProgresss from FormProgresss formProgresss where formProgresss.user.login = ?#{principal.username}")
    List<FormProgresss> findByUserIsCurrentUser();
}
