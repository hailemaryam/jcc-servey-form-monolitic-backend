package et.com.hmmk.rmt.web.rest;

import et.com.hmmk.rmt.repository.TypeOfOrganizationRepository;
import et.com.hmmk.rmt.service.TypeOfOrganizationQueryService;
import et.com.hmmk.rmt.service.TypeOfOrganizationService;
import et.com.hmmk.rmt.service.criteria.TypeOfOrganizationCriteria;
import et.com.hmmk.rmt.service.dto.TypeOfOrganizationDTO;
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
 * REST controller for managing {@link et.com.hmmk.rmt.domain.TypeOfOrganization}.
 */
@RestController
@RequestMapping("/api")
public class TypeOfOrganizationResource {

    private final Logger log = LoggerFactory.getLogger(TypeOfOrganizationResource.class);

    private static final String ENTITY_NAME = "typeOfOrganization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeOfOrganizationService typeOfOrganizationService;

    private final TypeOfOrganizationRepository typeOfOrganizationRepository;

    private final TypeOfOrganizationQueryService typeOfOrganizationQueryService;

    public TypeOfOrganizationResource(
        TypeOfOrganizationService typeOfOrganizationService,
        TypeOfOrganizationRepository typeOfOrganizationRepository,
        TypeOfOrganizationQueryService typeOfOrganizationQueryService
    ) {
        this.typeOfOrganizationService = typeOfOrganizationService;
        this.typeOfOrganizationRepository = typeOfOrganizationRepository;
        this.typeOfOrganizationQueryService = typeOfOrganizationQueryService;
    }

    /**
     * {@code POST  /type-of-organizations} : Create a new typeOfOrganization.
     *
     * @param typeOfOrganizationDTO the typeOfOrganizationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeOfOrganizationDTO, or with status {@code 400 (Bad Request)} if the typeOfOrganization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-of-organizations")
    public ResponseEntity<TypeOfOrganizationDTO> createTypeOfOrganization(@RequestBody TypeOfOrganizationDTO typeOfOrganizationDTO)
        throws URISyntaxException {
        log.debug("REST request to save TypeOfOrganization : {}", typeOfOrganizationDTO);
        if (typeOfOrganizationDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeOfOrganization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeOfOrganizationDTO result = typeOfOrganizationService.save(typeOfOrganizationDTO);
        return ResponseEntity
            .created(new URI("/api/type-of-organizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-of-organizations/:id} : Updates an existing typeOfOrganization.
     *
     * @param id the id of the typeOfOrganizationDTO to save.
     * @param typeOfOrganizationDTO the typeOfOrganizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeOfOrganizationDTO,
     * or with status {@code 400 (Bad Request)} if the typeOfOrganizationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeOfOrganizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-of-organizations/{id}")
    public ResponseEntity<TypeOfOrganizationDTO> updateTypeOfOrganization(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeOfOrganizationDTO typeOfOrganizationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeOfOrganization : {}, {}", id, typeOfOrganizationDTO);
        if (typeOfOrganizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeOfOrganizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeOfOrganizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeOfOrganizationDTO result = typeOfOrganizationService.save(typeOfOrganizationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeOfOrganizationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-of-organizations/:id} : Partial updates given fields of an existing typeOfOrganization, field will ignore if it is null
     *
     * @param id the id of the typeOfOrganizationDTO to save.
     * @param typeOfOrganizationDTO the typeOfOrganizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeOfOrganizationDTO,
     * or with status {@code 400 (Bad Request)} if the typeOfOrganizationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeOfOrganizationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeOfOrganizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-of-organizations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeOfOrganizationDTO> partialUpdateTypeOfOrganization(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeOfOrganizationDTO typeOfOrganizationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeOfOrganization partially : {}, {}", id, typeOfOrganizationDTO);
        if (typeOfOrganizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeOfOrganizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeOfOrganizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeOfOrganizationDTO> result = typeOfOrganizationService.partialUpdate(typeOfOrganizationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeOfOrganizationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-of-organizations} : get all the typeOfOrganizations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeOfOrganizations in body.
     */
    @GetMapping("/type-of-organizations")
    public ResponseEntity<List<TypeOfOrganizationDTO>> getAllTypeOfOrganizations(TypeOfOrganizationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TypeOfOrganizations by criteria: {}", criteria);
        Page<TypeOfOrganizationDTO> page = typeOfOrganizationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-of-organizations/count} : count all the typeOfOrganizations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/type-of-organizations/count")
    public ResponseEntity<Long> countTypeOfOrganizations(TypeOfOrganizationCriteria criteria) {
        log.debug("REST request to count TypeOfOrganizations by criteria: {}", criteria);
        return ResponseEntity.ok().body(typeOfOrganizationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /type-of-organizations/:id} : get the "id" typeOfOrganization.
     *
     * @param id the id of the typeOfOrganizationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeOfOrganizationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-of-organizations/{id}")
    public ResponseEntity<TypeOfOrganizationDTO> getTypeOfOrganization(@PathVariable Long id) {
        log.debug("REST request to get TypeOfOrganization : {}", id);
        Optional<TypeOfOrganizationDTO> typeOfOrganizationDTO = typeOfOrganizationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeOfOrganizationDTO);
    }

    /**
     * {@code DELETE  /type-of-organizations/:id} : delete the "id" typeOfOrganization.
     *
     * @param id the id of the typeOfOrganizationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-of-organizations/{id}")
    public ResponseEntity<Void> deleteTypeOfOrganization(@PathVariable Long id) {
        log.debug("REST request to delete TypeOfOrganization : {}", id);
        typeOfOrganizationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
