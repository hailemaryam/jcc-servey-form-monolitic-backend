package et.com.hmmk.rmt.service;

import et.com.hmmk.rmt.domain.*; // for static metamodels
import et.com.hmmk.rmt.domain.QuestionChoice;
import et.com.hmmk.rmt.repository.QuestionChoiceRepository;
import et.com.hmmk.rmt.service.criteria.QuestionChoiceCriteria;
import et.com.hmmk.rmt.service.dto.QuestionChoiceDTO;
import et.com.hmmk.rmt.service.mapper.QuestionChoiceMapper;
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
 * Service for executing complex queries for {@link QuestionChoice} entities in the database.
 * The main input is a {@link QuestionChoiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuestionChoiceDTO} or a {@link Page} of {@link QuestionChoiceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuestionChoiceQueryService extends QueryService<QuestionChoice> {

    private final Logger log = LoggerFactory.getLogger(QuestionChoiceQueryService.class);

    private final QuestionChoiceRepository questionChoiceRepository;

    private final QuestionChoiceMapper questionChoiceMapper;

    public QuestionChoiceQueryService(QuestionChoiceRepository questionChoiceRepository, QuestionChoiceMapper questionChoiceMapper) {
        this.questionChoiceRepository = questionChoiceRepository;
        this.questionChoiceMapper = questionChoiceMapper;
    }

    /**
     * Return a {@link List} of {@link QuestionChoiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuestionChoiceDTO> findByCriteria(QuestionChoiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<QuestionChoice> specification = createSpecification(criteria);
        return questionChoiceMapper.toDto(questionChoiceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QuestionChoiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionChoiceDTO> findByCriteria(QuestionChoiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QuestionChoice> specification = createSpecification(criteria);
        return questionChoiceRepository.findAll(specification, page).map(questionChoiceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuestionChoiceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<QuestionChoice> specification = createSpecification(criteria);
        return questionChoiceRepository.count(specification);
    }

    /**
     * Function to convert {@link QuestionChoiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QuestionChoice> createSpecification(QuestionChoiceCriteria criteria) {
        Specification<QuestionChoice> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), QuestionChoice_.id));
            }
            if (criteria.getOption() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOption(), QuestionChoice_.option));
            }
            if (criteria.getQuestionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getQuestionId(),
                            root -> root.join(QuestionChoice_.question, JoinType.LEFT).get(Question_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
