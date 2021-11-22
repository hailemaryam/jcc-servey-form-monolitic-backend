package et.com.hmmk.rmt.service;

import et.com.hmmk.rmt.domain.*; // for static metamodels
import et.com.hmmk.rmt.domain.TypeOfOrganization;
import et.com.hmmk.rmt.repository.TypeOfOrganizationRepository;
import et.com.hmmk.rmt.service.criteria.TypeOfOrganizationCriteria;
import et.com.hmmk.rmt.service.dto.TypeOfOrganizationDTO;
import et.com.hmmk.rmt.service.mapper.TypeOfOrganizationMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TypeOfOrganization} entities in the database.
 * The main input is a {@link TypeOfOrganizationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TypeOfOrganizationDTO} or a {@link Page} of {@link TypeOfOrganizationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TypeOfOrganizationQueryService extends QueryService<TypeOfOrganization> {

    private final Logger log = LoggerFactory.getLogger(TypeOfOrganizationQueryService.class);

    private final TypeOfOrganizationRepository typeOfOrganizationRepository;

    private final TypeOfOrganizationMapper typeOfOrganizationMapper;

    public TypeOfOrganizationQueryService(
        TypeOfOrganizationRepository typeOfOrganizationRepository,
        TypeOfOrganizationMapper typeOfOrganizationMapper
    ) {
        this.typeOfOrganizationRepository = typeOfOrganizationRepository;
        this.typeOfOrganizationMapper = typeOfOrganizationMapper;
    }

    /**
     * Return a {@link List} of {@link TypeOfOrganizationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TypeOfOrganizationDTO> findByCriteria(TypeOfOrganizationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TypeOfOrganization> specification = createSpecification(criteria);
        return typeOfOrganizationMapper.toDto(typeOfOrganizationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TypeOfOrganizationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeOfOrganizationDTO> findByCriteria(TypeOfOrganizationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TypeOfOrganization> specification = createSpecification(criteria);
        return typeOfOrganizationRepository.findAll(specification, page).map(typeOfOrganizationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TypeOfOrganizationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TypeOfOrganization> specification = createSpecification(criteria);
        return typeOfOrganizationRepository.count(specification);
    }

    /**
     * Function to convert {@link TypeOfOrganizationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TypeOfOrganization> createSpecification(TypeOfOrganizationCriteria criteria) {
        Specification<TypeOfOrganization> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TypeOfOrganization_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TypeOfOrganization_.name));
            }
            if (criteria.getCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompanyId(),
                            root -> root.join(TypeOfOrganization_.companies, JoinType.LEFT).get(Company_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
