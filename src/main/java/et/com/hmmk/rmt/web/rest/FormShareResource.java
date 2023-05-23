package et.com.hmmk.rmt.web.rest;

import et.com.hmmk.rmt.domain.User;
import et.com.hmmk.rmt.repository.UserRepository;
import et.com.hmmk.rmt.service.*;
import et.com.hmmk.rmt.service.criteria.CompanyCriteria;
import et.com.hmmk.rmt.service.criteria.FormProgresssCriteria;
import et.com.hmmk.rmt.service.criteria.ProjectCriteria;
import et.com.hmmk.rmt.service.dto.*;
import et.com.hmmk.rmt.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    private final ProjectQueryService projectQueryService;

    private final MailService mailService;

    private final UserRepository userRepository;

    public FormShareResource(
        FormProgresssService formProgresssService,
        FormProgresssQueryService formProgresssQueryService,
        CompanyQueryService companyQueryService,
        ProjectQueryService projectQueryService,
        MailService mailService,
        UserRepository userRepository
    ) {
        this.formProgresssService = formProgresssService;
        this.formProgresssQueryService = formProgresssQueryService;
        this.companyQueryService = companyQueryService;
        this.projectQueryService = projectQueryService;
        this.mailService = mailService;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /form-progressses} : Create a new formProgresss.
     *
     * @param formProgresssDTO the formProgresssDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formProgresssDTO, or with status {@code 400 (Bad Request)} if the formProgresss has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/form-share")
    public ResponseEntity<List<FormProgresssDTO>> createIndividualFormProgresss(@RequestBody FormProgresssDTO formProgresssDTO)
        throws URISyntaxException {
        log.debug("REST request to save FormProgresss of individual : {}", formProgresssDTO);
        if (formProgresssDTO.getId() != null) {
            throw new BadRequestAlertException("A new formProgresss cannot already have an ID", ENTITY_NAME, "idexists");
        }
        List<FormProgresssDTO> result = new ArrayList<>();
        FormProgresssCriteria formProgresssCriteria = new FormProgresssCriteria();
        LongFilter userFilter = new LongFilter();
        userFilter.setEquals(formProgresssDTO.getUser().getId());
        LongFilter formFilter = new LongFilter();
        formFilter.setEquals(formProgresssDTO.getForm().getId());
        formProgresssCriteria.setUserId(userFilter);
        formProgresssCriteria.setFormId(formFilter);
        List<FormProgresssDTO> formProgressList = formProgresssQueryService.findByCriteria(formProgresssCriteria);
        if (formProgressList.isEmpty()) {
            Optional<User> userById = userRepository.findById(formProgresssDTO.getUser().getId());
            mailService.sendNotificationEmail(userById.get());
            for (ProjectDTO projectDTO : getUsersProjectByCompanyId(formProgresssDTO.getUser().getId())) {
                FormProgresssDTO formProgresssDTOTobeCreated = formProgresssDTO;
                formProgresssDTOTobeCreated.setSubmited(false);
                formProgresssDTOTobeCreated.setSentedOn(Instant.now());
                formProgresssDTOTobeCreated.setProject(projectDTO);
                result.add(formProgresssService.save(formProgresssDTOTobeCreated));
            }
        } else {
            result = formProgressList;
        }
        return ResponseEntity
            .created(new URI("/api/form-progressses/"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, "form progresses created"))
            .body(result);
    }

    @PutMapping("/form-share/{id}")
    public ResponseEntity<List<FormProgresssDTO>> createFormProgresForTypeOfOrganization(
        @PathVariable(value = "id") final Long id,
        @RequestBody TypeOfOrganizationDTO typeOfOrganizationDTO
    ) throws URISyntaxException {
        log.debug("REST request to create form progres by TypeOfOrganization : {}, {}", id, typeOfOrganizationDTO);
        List<FormProgresssDTO> result = new ArrayList<>();
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
                Optional<User> userById = userRepository.findById(companyDTO.getId());
                mailService.sendNotificationEmail(userById.get());
                getUsersProjectByCompanyId(companyDTO.getId())
                    .forEach(projectDTO -> {
                        FormProgresssDTO formProgresssDTO = new FormProgresssDTO();
                        formProgresssDTO.setSubmited(false);
                        formProgresssDTO.setSentedOn(Instant.now());
                        formProgresssDTO.setUser(companyDTO.getUser());
                        FormDTO formDTO = new FormDTO();
                        formDTO.setId(id);
                        formProgresssDTO.setForm(formDTO);
                        formProgresssDTO.setProject(projectDTO);
                        result.add(formProgresssService.save(formProgresssDTO));
                    });
            }
        });
        return ResponseEntity
            .created(new URI("/api/form-progressses/" + id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, "form progresses shared for company type"))
            .body(result);
    }

    private List<ProjectDTO> getUsersProjectByCompanyId(Long userId) {
        ProjectCriteria projectCriteria = new ProjectCriteria();
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(userId);
        projectCriteria.setCompanyId(longFilter);
        return projectQueryService.findByCriteria(projectCriteria);
    }
}
