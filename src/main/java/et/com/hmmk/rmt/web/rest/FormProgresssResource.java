package et.com.hmmk.rmt.web.rest;

import et.com.hmmk.rmt.repository.FormProgresssRepository;
import et.com.hmmk.rmt.service.FormProgresssQueryService;
import et.com.hmmk.rmt.service.FormProgresssService;
import et.com.hmmk.rmt.service.criteria.FormProgresssCriteria;
import et.com.hmmk.rmt.service.dto.FormProgresssDTO;
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
 * REST controller for managing {@link et.com.hmmk.rmt.domain.FormProgresss}.
 */
@RestController
@RequestMapping("/api")
public class FormProgresssResource {

    private final Logger log = LoggerFactory.getLogger(FormProgresssResource.class);

    private static final String ENTITY_NAME = "formProgresss";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormProgresssService formProgresssService;

    private final FormProgresssRepository formProgresssRepository;

    private final FormProgresssQueryService formProgresssQueryService;

    public FormProgresssResource(
        FormProgresssService formProgresssService,
        FormProgresssRepository formProgresssRepository,
        FormProgresssQueryService formProgresssQueryService
    ) {
        this.formProgresssService = formProgresssService;
        this.formProgresssRepository = formProgresssRepository;
        this.formProgresssQueryService = formProgresssQueryService;
    }

    /**
     * {@code POST  /form-progressses} : Create a new formProgresss.
     *
     * @param formProgresssDTO the formProgresssDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formProgresssDTO, or with status {@code 400 (Bad Request)} if the formProgresss has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/form-progressses")
    public ResponseEntity<FormProgresssDTO> createFormProgresss(@RequestBody FormProgresssDTO formProgresssDTO) throws URISyntaxException {
        log.debug("REST request to save FormProgresss : {}", formProgresssDTO);
        if (formProgresssDTO.getId() != null) {
            throw new BadRequestAlertException("A new formProgresss cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormProgresssDTO result = formProgresssService.save(formProgresssDTO);
        return ResponseEntity
            .created(new URI("/api/form-progressses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /form-progressses/:id} : Updates an existing formProgresss.
     *
     * @param id the id of the formProgresssDTO to save.
     * @param formProgresssDTO the formProgresssDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formProgresssDTO,
     * or with status {@code 400 (Bad Request)} if the formProgresssDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formProgresssDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/form-progressses/{id}")
    public ResponseEntity<FormProgresssDTO> updateFormProgresss(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormProgresssDTO formProgresssDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FormProgresss : {}, {}", id, formProgresssDTO);
        if (formProgresssDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formProgresssDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formProgresssRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FormProgresssDTO result = formProgresssService.save(formProgresssDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formProgresssDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /form-progressses/:id} : Partial updates given fields of an existing formProgresss, field will ignore if it is null
     *
     * @param id the id of the formProgresssDTO to save.
     * @param formProgresssDTO the formProgresssDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formProgresssDTO,
     * or with status {@code 400 (Bad Request)} if the formProgresssDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formProgresssDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formProgresssDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/form-progressses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FormProgresssDTO> partialUpdateFormProgresss(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormProgresssDTO formProgresssDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FormProgresss partially : {}, {}", id, formProgresssDTO);
        if (formProgresssDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formProgresssDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formProgresssRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormProgresssDTO> result = formProgresssService.partialUpdate(formProgresssDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formProgresssDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /form-progressses} : get all the formProgressses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formProgressses in body.
     */
    @GetMapping("/form-progressses")
    public ResponseEntity<List<FormProgresssDTO>> getAllFormProgressses(FormProgresssCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FormProgressses by criteria: {}", criteria);
        Page<FormProgresssDTO> page = formProgresssQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /form-progressses/count} : count all the formProgressses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/form-progressses/count")
    public ResponseEntity<Long> countFormProgressses(FormProgresssCriteria criteria) {
        log.debug("REST request to count FormProgressses by criteria: {}", criteria);
        return ResponseEntity.ok().body(formProgresssQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /form-progressses/:id} : get the "id" formProgresss.
     *
     * @param id the id of the formProgresssDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formProgresssDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/form-progressses/{id}")
    public ResponseEntity<FormProgresssDTO> getFormProgresss(@PathVariable Long id) {
        log.debug("REST request to get FormProgresss : {}", id);
        Optional<FormProgresssDTO> formProgresssDTO = formProgresssService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formProgresssDTO);
    }

    /**
     * {@code DELETE  /form-progressses/:id} : delete the "id" formProgresss.
     *
     * @param id the id of the formProgresssDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/form-progressses/{id}")
    public ResponseEntity<Void> deleteFormProgresss(@PathVariable Long id) {
        log.debug("REST request to delete FormProgresss : {}", id);
        formProgresssService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
