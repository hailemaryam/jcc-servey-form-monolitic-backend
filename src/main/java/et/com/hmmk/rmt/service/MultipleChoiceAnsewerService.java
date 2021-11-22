package et.com.hmmk.rmt.service;

import et.com.hmmk.rmt.service.dto.MultipleChoiceAnsewerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link et.com.hmmk.rmt.domain.MultipleChoiceAnsewer}.
 */
public interface MultipleChoiceAnsewerService {
    /**
     * Save a multipleChoiceAnsewer.
     *
     * @param multipleChoiceAnsewerDTO the entity to save.
     * @return the persisted entity.
     */
    MultipleChoiceAnsewerDTO save(MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO);

    /**
     * Partially updates a multipleChoiceAnsewer.
     *
     * @param multipleChoiceAnsewerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MultipleChoiceAnsewerDTO> partialUpdate(MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO);

    /**
     * Get all the multipleChoiceAnsewers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MultipleChoiceAnsewerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" multipleChoiceAnsewer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MultipleChoiceAnsewerDTO> findOne(Long id);

    /**
     * Delete the "id" multipleChoiceAnsewer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
