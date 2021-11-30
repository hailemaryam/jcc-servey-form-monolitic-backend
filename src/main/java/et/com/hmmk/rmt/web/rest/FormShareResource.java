package et.com.hmmk.rmt.web.rest;

import et.com.hmmk.rmt.service.CompanyQueryService;
import et.com.hmmk.rmt.service.FormProgresssQueryService;
import et.com.hmmk.rmt.service.FormProgresssService;
import et.com.hmmk.rmt.service.criteria.CompanyCriteria;
import et.com.hmmk.rmt.service.criteria.FormProgresssCriteria;
import et.com.hmmk.rmt.service.dto.*;
import et.com.hmmk.rmt.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link et.com.hmmk.rmt.domain.FormProgresss}.
 */
@RestController
@RequestMapping("/api")
public class FormShareResource {

    private final Logger log = LoggerFactory.getLogger(FormShareResource.class);

    private static final String ENTITY_NAME = "formProgresss";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormProgresssService formProgresssService;

    private final FormProgresssQueryService formProgresssQueryService;

    private final CompanyQueryService companyQueryService;

    public FormShareResource(
        FormProgresssService formProgresssService,
        FormProgresssQueryService formProgresssQueryService,
        CompanyQueryService companyQueryService
    ) {
        this.formProgresssService = formProgresssService;
        this.formProgresssQueryService = formProgresssQueryService;
        this.companyQueryService = companyQueryService;
    }

    /**
     * {@code POST  /form-progressses} : Create a new formProgresss.
     *
     * @param formProgresssDTO the formProgresssDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formProgresssDTO, or with status {@code 400 (Bad Request)} if the formProgresss has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/form-share")
    public ResponseEntity<FormProgresssDTO> createIndividualFormProgresss(@RequestBody FormProgresssDTO formProgresssDTO)
        throws URISyntaxException {
        log.debug("REST request to save FormProgresss of individual : {}", formProgresssDTO);
        if (formProgresssDTO.getId() != null) {
            throw new BadRequestAlertException("A new formProgresss cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormProgresssDTO result;
        FormProgresssCriteria formProgresssCriteria = new FormProgresssCriteria();
        LongFilter userFilter = new LongFilter();
        userFilter.setEquals(formProgresssDTO.getUser().getId());
        LongFilter formFilter = new LongFilter();
        formFilter.setEquals(formProgresssDTO.getForm().getId());
        formProgresssCriteria.setUserId(userFilter);
        formProgresssCriteria.setFormId(formFilter);
        List<FormProgresssDTO> formProgressList = formProgresssQueryService.findByCriteria(formProgresssCriteria);
        if (formProgressList.isEmpty()) {
            formProgresssDTO.setSubmited(false);
            formProgresssDTO.setSentedOn(Instant.now());
            result = formProgresssService.save(formProgresssDTO);
        } else {
            result = formProgressList.get(0);
        }
        return ResponseEntity
            .created(new URI("/api/form-progressses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/form-share/{id}")
    public ResponseEntity<Void> createFormProgresForTypeOfOrganization(
        @PathVariable(value = "id") final Long id,
        @RequestBody TypeOfOrganizationDTO typeOfOrganizationDTO
    ) throws URISyntaxException {
        log.debug("REST request to create form progres by TypeOfOrganization : {}, {}", id, typeOfOrganizationDTO);
        CompanyCriteria companyCriteria = new CompanyCriteria();
        LongFilter organizationTypeFilter = new LongFilter();
        organizationTypeFilter.setEquals(typeOfOrganizationDTO.getId());
        companyCriteria.setTypeOfOrganationId(organizationTypeFilter);
        List<CompanyDTO> companyDTOList = companyQueryService.findByCriteria(companyCriteria);
        companyDTOList.forEach(companyDTO -> {
            FormProgresssCriteria formProgresssCriteria = new FormProgresssCriteria();
            LongFilter userFilter = new LongFilter();
            userFilter.setEquals(companyDTO.getId());
            LongFilter formFilter = new LongFilter();
            formFilter.setEquals(id);
            formProgresssCriteria.setUserId(userFilter);
            formProgresssCriteria.setFormId(formFilter);
            List<FormProgresssDTO> formProgressList = formProgresssQueryService.findByCriteria(formProgresssCriteria);
            if (formProgressList.isEmpty()) {
                FormProgresssDTO formProgresssDTO = new FormProgresssDTO();
                formProgresssDTO.setSubmited(false);
                formProgresssDTO.setSentedOn(Instant.now());
                formProgresssDTO.setUser(companyDTO.getUser());
                FormDTO formDTO = new FormDTO();
                formDTO.setId(id);
                formProgresssDTO.setForm(formDTO);
                formProgresssService.save(formProgresssDTO);
            }
        });
        return ResponseEntity
            .noContent()
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    companyDTOList.size() + " company has shared the form"
                )
            )
            .build();
    }
}
