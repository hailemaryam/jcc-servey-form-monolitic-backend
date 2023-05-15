package et.com.hmmk.rmt.service;

import et.com.hmmk.rmt.service.dto.NewsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link et.com.hmmk.rmt.domain.News}.
 */
public interface NewsService {
    /**
     * Save a news.
     *
     * @param newsDTO the entity to save.
     * @return the persisted entity.
     */
    NewsDTO save(NewsDTO newsDTO);

    /**
     * Partially updates a news.
     *
     * @param newsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NewsDTO> partialUpdate(NewsDTO newsDTO);

    /**
     * Get all the news.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NewsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" news.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NewsDTO> findOne(Long id);

    /**
     * Delete the "id" news.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
