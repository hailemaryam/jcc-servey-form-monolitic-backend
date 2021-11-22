package et.com.hmmk.rmt.repository;

import et.com.hmmk.rmt.domain.TypeOfOrganization;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TypeOfOrganization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeOfOrganizationRepository
    extends JpaRepository<TypeOfOrganization, Long>, JpaSpecificationExecutor<TypeOfOrganization> {}
