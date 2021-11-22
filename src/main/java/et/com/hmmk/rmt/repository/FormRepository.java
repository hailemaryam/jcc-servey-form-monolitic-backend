package et.com.hmmk.rmt.repository;

import et.com.hmmk.rmt.domain.Form;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Form entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormRepository extends JpaRepository<Form, Long>, JpaSpecificationExecutor<Form> {
    @Query("select form from Form form where form.user.login = ?#{principal.username}")
    List<Form> findByUserIsCurrentUser();
}
