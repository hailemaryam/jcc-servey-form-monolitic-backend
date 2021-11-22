package et.com.hmmk.rmt.service;

import et.com.hmmk.rmt.domain.*; // for static metamodels
import et.com.hmmk.rmt.domain.Form;
import et.com.hmmk.rmt.repository.FormRepository;
import et.com.hmmk.rmt.service.criteria.FormCriteria;
import et.com.hmmk.rmt.service.dto.FormDTO;
import et.com.hmmk.rmt.service.mapper.FormMapper;
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
 * Service for executing complex queries for {@link Form} entities in the database.
 * The main input is a {@link FormCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FormDTO} or a {@link Page} of {@link FormDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FormQueryService extends QueryService<Form> {

    private final Logger log = LoggerFactory.getLogger(FormQueryService.class);

    private final FormRepository formRepository;

    private final FormMapper formMapper;

    public FormQueryService(FormRepository formRepository, FormMapper formMapper) {
        this.formRepository = formRepository;
        this.formMapper = formMapper;
    }

    /**
     * Return a {@link List} of {@link FormDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FormDTO> findByCriteria(FormCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Form> specification = createSpecification(criteria);
        return formMapper.toDto(formRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FormDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FormDTO> findByCriteria(FormCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Form> specification = createSpecification(criteria);
        return formRepository.findAll(specification, page).map(formMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FormCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Form> specification = createSpecification(criteria);
        return formRepository.count(specification);
    }

    /**
     * Function to convert {@link FormCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Form> createSpecification(FormCriteria criteria) {
        Specification<Form> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Form_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Form_.name));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), Form_.createdOn));
            }
            if (criteria.getUpdatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedOn(), Form_.updatedOn));
            }
            if (criteria.getFormProgresssId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFormProgresssId(),
                            root -> root.join(Form_.formProgressses, JoinType.LEFT).get(FormProgresss_.id)
                        )
                    );
            }
            if (criteria.getQuestionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getQuestionId(), root -> root.join(Form_.questions, JoinType.LEFT).get(Question_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getUserId(), root -> root.join(Form_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
