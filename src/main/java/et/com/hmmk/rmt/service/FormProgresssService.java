package et.com.hmmk.rmt.service;

import et.com.hmmk.rmt.service.dto.FormProgresssDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link et.com.hmmk.rmt.domain.FormProgresss}.
 */
public interface FormProgresssService {
    /**
     * Save a formProgresss.
     *
     * @param formProgresssDTO the entity to save.
     * @return the persisted entity.
     */
    FormProgresssDTO save(FormProgresssDTO formProgresssDTO);

    /**
     * Partially updates a formProgresss.
     *
     * @param formProgresssDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FormProgresssDTO> partialUpdate(FormProgresssDTO formProgresssDTO);

    /**
     * Get all the formProgressses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormProgresssDTO> findAll(Pageable pageable);

    /**
     * Get the "id" formProgresss.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormProgresssDTO> findOne(Long id);

    /**
     * Delete the "id" formProgresss.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
