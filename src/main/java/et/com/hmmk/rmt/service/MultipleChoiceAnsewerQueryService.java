package et.com.hmmk.rmt.service;

import et.com.hmmk.rmt.domain.*; // for static metamodels
import et.com.hmmk.rmt.domain.MultipleChoiceAnsewer;
import et.com.hmmk.rmt.repository.MultipleChoiceAnsewerRepository;
import et.com.hmmk.rmt.service.criteria.MultipleChoiceAnsewerCriteria;
import et.com.hmmk.rmt.service.dto.MultipleChoiceAnsewerDTO;
import et.com.hmmk.rmt.service.mapper.MultipleChoiceAnsewerMapper;
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
 * Service for executing complex queries for {@link MultipleChoiceAnsewer} entities in the database.
 * The main input is a {@link MultipleChoiceAnsewerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MultipleChoiceAnsewerDTO} or a {@link Page} of {@link MultipleChoiceAnsewerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MultipleChoiceAnsewerQueryService extends QueryService<MultipleChoiceAnsewer> {

    private final Logger log = LoggerFactory.getLogger(MultipleChoiceAnsewerQueryService.class);

    private final MultipleChoiceAnsewerRepository multipleChoiceAnsewerRepository;

    private final MultipleChoiceAnsewerMapper multipleChoiceAnsewerMapper;

    public MultipleChoiceAnsewerQueryService(
        MultipleChoiceAnsewerRepository multipleChoiceAnsewerRepository,
        MultipleChoiceAnsewerMapper multipleChoiceAnsewerMapper
    ) {
        this.multipleChoiceAnsewerRepository = multipleChoiceAnsewerRepository;
        this.multipleChoiceAnsewerMapper = multipleChoiceAnsewerMapper;
    }

    /**
     * Return a {@link List} of {@link MultipleChoiceAnsewerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MultipleChoiceAnsewerDTO> findByCriteria(MultipleChoiceAnsewerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MultipleChoiceAnsewer> specification = createSpecification(criteria);
        return multipleChoiceAnsewerMapper.toDto(multipleChoiceAnsewerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MultipleChoiceAnsewerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MultipleChoiceAnsewerDTO> findByCriteria(MultipleChoiceAnsewerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MultipleChoiceAnsewer> specification = createSpecification(criteria);
        return multipleChoiceAnsewerRepository.findAll(specification, page).map(multipleChoiceAnsewerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MultipleChoiceAnsewerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MultipleChoiceAnsewer> specification = createSpecification(criteria);
        return multipleChoiceAnsewerRepository.count(specification);
    }

    /**
     * Function to convert {@link MultipleChoiceAnsewerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MultipleChoiceAnsewer> createSpecification(MultipleChoiceAnsewerCriteria criteria) {
        Specification<MultipleChoiceAnsewer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MultipleChoiceAnsewer_.id));
            }
            if (criteria.getChoice() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChoice(), MultipleChoiceAnsewer_.choice));
            }
            if (criteria.getAnswerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAnswerId(),
                            root -> root.join(MultipleChoiceAnsewer_.answer, JoinType.LEFT).get(Answer_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
