package et.com.hmmk.rmt.web.rest;

import et.com.hmmk.rmt.domain.User;
import et.com.hmmk.rmt.security.AuthoritiesConstants;
import et.com.hmmk.rmt.service.FormService;
import et.com.hmmk.rmt.service.QuestionChoiceService;
import et.com.hmmk.rmt.service.QuestionService;
import et.com.hmmk.rmt.service.UserService;
import et.com.hmmk.rmt.service.dto.*;
import et.com.hmmk.rmt.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link et.com.hmmk.rmt.domain.Form}.
 */
@RestController
@RequestMapping("/api")
public class FormCreatorResource {

    private final Logger log = LoggerFactory.getLogger(FormCreatorResource.class);

    private static final String ENTITY_NAME = "form";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormService formService;

    private final QuestionService questionService;

    private final QuestionChoiceService questionChoiceService;

    private final UserService userService;

    public FormCreatorResource(
        FormService formService,
        QuestionService questionService,
        QuestionChoiceService questionChoiceService,
        UserService userService
    ) {
        this.formService = formService;
        this.questionService = questionService;
        this.questionChoiceService = questionChoiceService;
        this.userService = userService;
    }

    /**
     * {@code POST  /forms} : Create a new form.
     *
     * @param formCreatorDTO the formDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formDTO, or with status {@code 400 (Bad Request)} if the form has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/formCreator")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<FormCreatorDTO> createForm(@RequestBody FormCreatorDTO formCreatorDTO) throws URISyntaxException {
        log.debug("REST request to save Form : {}", formCreatorDTO);
        if (formCreatorDTO.getId() != null) {
            throw new BadRequestAlertException("A new form cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<User> userWithAuthorities = userService.getUserWithAuthorities();
        formCreatorDTO.setUser(new UserDTO());
        formCreatorDTO.getUser().setId(userWithAuthorities.get().getId());
        formCreatorDTO.getUser().setLogin(userWithAuthorities.get().getLogin());
        FormDTO result = formService.save(fromFormCreator(formCreatorDTO));
        formCreatorDTO.setId(result.getId());
        if (formCreatorDTO.getQuestions() != null) {
            for (QuestionCreatorDTO questionCreatorDTO : formCreatorDTO.getQuestions()) {
                questionCreatorDTO.setForm(result);
                QuestionDTO questionDTO = questionService.save(fromQuestionCreator(questionCreatorDTO));
                questionCreatorDTO.setId(questionDTO.getId());
                if (questionCreatorDTO.getQuestionChoices() != null) {
                    for (QuestionChoiceDTO questionChoice : questionCreatorDTO.getQuestionChoices()) {
                        questionChoice.setQuestion(questionDTO);
                        QuestionChoiceDTO questionChoiceDTOreturned = questionChoiceService.save(questionChoice);
                        questionChoice.setId(questionChoiceDTOreturned.getId());
                    }
                }
            }
        }
        return ResponseEntity
            .created(new URI("/api/forms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(formCreatorDTO);
    }

    FormDTO fromFormCreator(FormCreatorDTO formCreatorDTO) {
        FormDTO formDTO = new FormDTO();
        formDTO.setId(formCreatorDTO.getId());
        formDTO.setName(formCreatorDTO.getName());
        formDTO.setDescription(formCreatorDTO.getDescription());
        formDTO.setCreatedOn(formCreatorDTO.getCreatedOn());
        formDTO.setUpdatedOn(formCreatorDTO.getUpdatedOn());
        formDTO.setUser(formCreatorDTO.getUser());
        return formDTO;
    }

    QuestionDTO fromQuestionCreator(QuestionCreatorDTO questionCreatorDTO) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(questionCreatorDTO.getId());
        questionDTO.setTitle(questionCreatorDTO.getTitle());
        questionDTO.setDataType(questionCreatorDTO.getDataType());
        questionDTO.setForm(questionCreatorDTO.getForm());
        questionDTO.setMandatory(questionCreatorDTO.getMandatory());
        return questionDTO;
    }
}
