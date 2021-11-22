package et.com.hmmk.rmt.web.rest;

import et.com.hmmk.rmt.repository.MultipleChoiceAnsewerRepository;
import et.com.hmmk.rmt.service.MultipleChoiceAnsewerQueryService;
import et.com.hmmk.rmt.service.MultipleChoiceAnsewerService;
import et.com.hmmk.rmt.service.criteria.MultipleChoiceAnsewerCriteria;
import et.com.hmmk.rmt.service.dto.MultipleChoiceAnsewerDTO;
import et.com.hmmk.rmt.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link et.com.hmmk.rmt.domain.MultipleChoiceAnsewer}.
 */
@RestController
@RequestMapping("/api")
public class MultipleChoiceAnsewerResource {

    private final Logger log = LoggerFactory.getLogger(MultipleChoiceAnsewerResource.class);

    private static final String ENTITY_NAME = "multipleChoiceAnsewer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MultipleChoiceAnsewerService multipleChoiceAnsewerService;

    private final MultipleChoiceAnsewerRepository multipleChoiceAnsewerRepository;

    private final MultipleChoiceAnsewerQueryService multipleChoiceAnsewerQueryService;

    public MultipleChoiceAnsewerResource(
        MultipleChoiceAnsewerService multipleChoiceAnsewerService,
        MultipleChoiceAnsewerRepository multipleChoiceAnsewerRepository,
        MultipleChoiceAnsewerQueryService multipleChoiceAnsewerQueryService
    ) {
        this.multipleChoiceAnsewerService = multipleChoiceAnsewerService;
        this.multipleChoiceAnsewerRepository = multipleChoiceAnsewerRepository;
        this.multipleChoiceAnsewerQueryService = multipleChoiceAnsewerQueryService;
    }

    /**
     * {@code POST  /multiple-choice-ansewers} : Create a new multipleChoiceAnsewer.
     *
     * @param multipleChoiceAnsewerDTO the multipleChoiceAnsewerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new multipleChoiceAnsewerDTO, or with status {@code 400 (Bad Request)} if the multipleChoiceAnsewer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/multiple-choice-ansewers")
    public ResponseEntity<MultipleChoiceAnsewerDTO> createMultipleChoiceAnsewer(
        @RequestBody MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO
    ) throws URISyntaxException {
        log.debug("REST request to save MultipleChoiceAnsewer : {}", multipleChoiceAnsewerDTO);
        if (multipleChoiceAnsewerDTO.getId() != null) {
            throw new BadRequestAlertException("A new multipleChoiceAnsewer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MultipleChoiceAnsewerDTO result = multipleChoiceAnsewerService.save(multipleChoiceAnsewerDTO);
        return ResponseEntity
            .created(new URI("/api/multiple-choice-ansewers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /multiple-choice-ansewers/:id} : Updates an existing multipleChoiceAnsewer.
     *
     * @param id the id of the multipleChoiceAnsewerDTO to save.
     * @param multipleChoiceAnsewerDTO the multipleChoiceAnsewerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated multipleChoiceAnsewerDTO,
     * or with status {@code 400 (Bad Request)} if the multipleChoiceAnsewerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the multipleChoiceAnsewerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/multiple-choice-ansewers/{id}")
    public ResponseEntity<MultipleChoiceAnsewerDTO> updateMultipleChoiceAnsewer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MultipleChoiceAnsewer : {}, {}", id, multipleChoiceAnsewerDTO);
        if (multipleChoiceAnsewerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, multipleChoiceAnsewerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!multipleChoiceAnsewerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MultipleChoiceAnsewerDTO result = multipleChoiceAnsewerService.save(multipleChoiceAnsewerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, multipleChoiceAnsewerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /multiple-choice-ansewers/:id} : Partial updates given fields of an existing multipleChoiceAnsewer, field will ignore if it is null
     *
     * @param id the id of the multipleChoiceAnsewerDTO to save.
     * @param multipleChoiceAnsewerDTO the multipleChoiceAnsewerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated multipleChoiceAnsewerDTO,
     * or with status {@code 400 (Bad Request)} if the multipleChoiceAnsewerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the multipleChoiceAnsewerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the multipleChoiceAnsewerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/multiple-choice-ansewers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MultipleChoiceAnsewerDTO> partialUpdateMultipleChoiceAnsewer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MultipleChoiceAnsewer partially : {}, {}", id, multipleChoiceAnsewerDTO);
        if (multipleChoiceAnsewerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, multipleChoiceAnsewerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!multipleChoiceAnsewerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MultipleChoiceAnsewerDTO> result = multipleChoiceAnsewerService.partialUpdate(multipleChoiceAnsewerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, multipleChoiceAnsewerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /multiple-choice-ansewers} : get all the multipleChoiceAnsewers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of multipleChoiceAnsewers in body.
     */
    @GetMapping("/multiple-choice-ansewers")
    public ResponseEntity<List<MultipleChoiceAnsewerDTO>> getAllMultipleChoiceAnsewers(
        MultipleChoiceAnsewerCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get MultipleChoiceAnsewers by criteria: {}", criteria);
        Page<MultipleChoiceAnsewerDTO> page = multipleChoiceAnsewerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /multiple-choice-ansewers/count} : count all the multipleChoiceAnsewers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/multiple-choice-ansewers/count")
    public ResponseEntity<Long> countMultipleChoiceAnsewers(MultipleChoiceAnsewerCriteria criteria) {
        log.debug("REST request to count MultipleChoiceAnsewers by criteria: {}", criteria);
        return ResponseEntity.ok().body(multipleChoiceAnsewerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /multiple-choice-ansewers/:id} : get the "id" multipleChoiceAnsewer.
     *
     * @param id the id of the multipleChoiceAnsewerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the multipleChoiceAnsewerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/multiple-choice-ansewers/{id}")
    public ResponseEntity<MultipleChoiceAnsewerDTO> getMultipleChoiceAnsewer(@PathVariable Long id) {
        log.debug("REST request to get MultipleChoiceAnsewer : {}", id);
        Optional<MultipleChoiceAnsewerDTO> multipleChoiceAnsewerDTO = multipleChoiceAnsewerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(multipleChoiceAnsewerDTO);
    }

    /**
     * {@code DELETE  /multiple-choice-ansewers/:id} : delete the "id" multipleChoiceAnsewer.
     *
     * @param id the id of the multipleChoiceAnsewerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/multiple-choice-ansewers/{id}")
    public ResponseEntity<Void> deleteMultipleChoiceAnsewer(@PathVariable Long id) {
        log.debug("REST request to delete MultipleChoiceAnsewer : {}", id);
        multipleChoiceAnsewerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
