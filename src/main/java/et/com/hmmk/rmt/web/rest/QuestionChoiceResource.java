package et.com.hmmk.rmt.web.rest;

import et.com.hmmk.rmt.repository.QuestionChoiceRepository;
import et.com.hmmk.rmt.service.QuestionChoiceQueryService;
import et.com.hmmk.rmt.service.QuestionChoiceService;
import et.com.hmmk.rmt.service.criteria.QuestionChoiceCriteria;
import et.com.hmmk.rmt.service.dto.QuestionChoiceDTO;
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
 * REST controller for managing {@link et.com.hmmk.rmt.domain.QuestionChoice}.
 */
@RestController
@RequestMapping("/api")
public class QuestionChoiceResource {

    private final Logger log = LoggerFactory.getLogger(QuestionChoiceResource.class);

    private static final String ENTITY_NAME = "questionChoice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionChoiceService questionChoiceService;

    private final QuestionChoiceRepository questionChoiceRepository;

    private final QuestionChoiceQueryService questionChoiceQueryService;

    public QuestionChoiceResource(
        QuestionChoiceService questionChoiceService,
        QuestionChoiceRepository questionChoiceRepository,
        QuestionChoiceQueryService questionChoiceQueryService
    ) {
        this.questionChoiceService = questionChoiceService;
        this.questionChoiceRepository = questionChoiceRepository;
        this.questionChoiceQueryService = questionChoiceQueryService;
    }

    /**
     * {@code POST  /question-choices} : Create a new questionChoice.
     *
     * @param questionChoiceDTO the questionChoiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionChoiceDTO, or with status {@code 400 (Bad Request)} if the questionChoice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/question-choices")
    public ResponseEntity<QuestionChoiceDTO> createQuestionChoice(@RequestBody QuestionChoiceDTO questionChoiceDTO)
        throws URISyntaxException {
        log.debug("REST request to save QuestionChoice : {}", questionChoiceDTO);
        if (questionChoiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new questionChoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestionChoiceDTO result = questionChoiceService.save(questionChoiceDTO);
        return ResponseEntity
            .created(new URI("/api/question-choices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /question-choices/:id} : Updates an existing questionChoice.
     *
     * @param id the id of the questionChoiceDTO to save.
     * @param questionChoiceDTO the questionChoiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionChoiceDTO,
     * or with status {@code 400 (Bad Request)} if the questionChoiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionChoiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/question-choices/{id}")
    public ResponseEntity<QuestionChoiceDTO> updateQuestionChoice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionChoiceDTO questionChoiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update QuestionChoice : {}, {}", id, questionChoiceDTO);
        if (questionChoiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionChoiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionChoiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuestionChoiceDTO result = questionChoiceService.save(questionChoiceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionChoiceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /question-choices/:id} : Partial updates given fields of an existing questionChoice, field will ignore if it is null
     *
     * @param id the id of the questionChoiceDTO to save.
     * @param questionChoiceDTO the questionChoiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionChoiceDTO,
     * or with status {@code 400 (Bad Request)} if the questionChoiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionChoiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionChoiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/question-choices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionChoiceDTO> partialUpdateQuestionChoice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionChoiceDTO questionChoiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuestionChoice partially : {}, {}", id, questionChoiceDTO);
        if (questionChoiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionChoiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionChoiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionChoiceDTO> result = questionChoiceService.partialUpdate(questionChoiceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionChoiceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /question-choices} : get all the questionChoices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionChoices in body.
     */
    @GetMapping("/question-choices")
    public ResponseEntity<List<QuestionChoiceDTO>> getAllQuestionChoices(QuestionChoiceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get QuestionChoices by criteria: {}", criteria);
        Page<QuestionChoiceDTO> page = questionChoiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /question-choices/count} : count all the questionChoices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/question-choices/count")
    public ResponseEntity<Long> countQuestionChoices(QuestionChoiceCriteria criteria) {
        log.debug("REST request to count QuestionChoices by criteria: {}", criteria);
        return ResponseEntity.ok().body(questionChoiceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /question-choices/:id} : get the "id" questionChoice.
     *
     * @param id the id of the questionChoiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionChoiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/question-choices/{id}")
    public ResponseEntity<QuestionChoiceDTO> getQuestionChoice(@PathVariable Long id) {
        log.debug("REST request to get QuestionChoice : {}", id);
        Optional<QuestionChoiceDTO> questionChoiceDTO = questionChoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionChoiceDTO);
    }

    /**
     * {@code DELETE  /question-choices/:id} : delete the "id" questionChoice.
     *
     * @param id the id of the questionChoiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/question-choices/{id}")
    public ResponseEntity<Void> deleteQuestionChoice(@PathVariable Long id) {
        log.debug("REST request to delete QuestionChoice : {}", id);
        questionChoiceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
