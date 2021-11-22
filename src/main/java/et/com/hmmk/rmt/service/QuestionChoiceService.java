package et.com.hmmk.rmt.service;

import et.com.hmmk.rmt.service.dto.QuestionChoiceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link et.com.hmmk.rmt.domain.QuestionChoice}.
 */
public interface QuestionChoiceService {
    /**
     * Save a questionChoice.
     *
     * @param questionChoiceDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionChoiceDTO save(QuestionChoiceDTO questionChoiceDTO);

    /**
     * Partially updates a questionChoice.
     *
     * @param questionChoiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuestionChoiceDTO> partialUpdate(QuestionChoiceDTO questionChoiceDTO);

    /**
     * Get all the questionChoices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuestionChoiceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" questionChoice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionChoiceDTO> findOne(Long id);

    /**
     * Delete the "id" questionChoice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
