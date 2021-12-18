package et.com.hmmk.rmt.service;

import et.com.hmmk.rmt.domain.*; // for static metamodels
import et.com.hmmk.rmt.domain.FormProgresss;
import et.com.hmmk.rmt.repository.FormProgresssRepository;
import et.com.hmmk.rmt.service.criteria.FormProgresssCriteria;
import et.com.hmmk.rmt.service.dto.FormProgresssDTO;
import et.com.hmmk.rmt.service.mapper.FormProgresssMapper;
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
 * Service for executing complex queries for {@link FormProgresss} entities in the database.
 * The main input is a {@link FormProgresssCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FormProgresssDTO} or a {@link Page} of {@link FormProgresssDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FormProgresssQueryService extends QueryService<FormProgresss> {

    private final Logger log = LoggerFactory.getLogger(FormProgresssQueryService.class);

    private final FormProgresssRepository formProgresssRepository;

    private final FormProgresssMapper formProgresssMapper;

    public FormProgresssQueryService(FormProgresssRepository formProgresssRepository, FormProgresssMapper formProgresssMapper) {
        this.formProgresssRepository = formProgresssRepository;
        this.formProgresssMapper = formProgresssMapper;
    }

    /**
     * Return a {@link List} of {@link FormProgresssDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FormProgresssDTO> findByCriteria(FormProgresssCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FormProgresss> specification = createSpecification(criteria);
        return formProgresssMapper.toDto(formProgresssRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FormProgresssDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FormProgresssDTO> findByCriteria(FormProgresssCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FormProgresss> specification = createSpecification(criteria);
        return formProgresssRepository.findAll(specification, page).map(formProgresssMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FormProgresssCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FormProgresss> specification = createSpecification(criteria);
        return formProgresssRepository.count(specification);
    }

    /**
     * Function to convert {@link FormProgresssCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FormProgresss> createSpecification(FormProgresssCriteria criteria) {
        Specification<FormProgresss> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FormProgresss_.id));
            }
            if (criteria.getSubmited() != null) {
                specification = specification.and(buildSpecification(criteria.getSubmited(), FormProgresss_.submited));
            }
            if (criteria.getStartedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartedOn(), FormProgresss_.startedOn));
            }
            if (criteria.getSubmitedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubmitedOn(), FormProgresss_.submitedOn));
            }
            if (criteria.getSentedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSentedOn(), FormProgresss_.sentedOn));
            }
            if (criteria.getAnswerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAnswerId(), root -> root.join(FormProgresss_.answers, JoinType.LEFT).get(Answer_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(FormProgresss_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getFormId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFormId(), root -> root.join(FormProgresss_.form, JoinType.LEFT).get(Form_.id))
                    );
            }
            if (criteria.getProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProjectId(),
                            root -> root.join(FormProgresss_.project, JoinType.LEFT).get(Project_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
