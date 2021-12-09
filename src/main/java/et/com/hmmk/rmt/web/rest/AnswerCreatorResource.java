package et.com.hmmk.rmt.web.rest;

import et.com.hmmk.rmt.repository.AnswerRepository;
import et.com.hmmk.rmt.repository.MultipleChoiceAnsewerRepository;
import et.com.hmmk.rmt.service.AnswerService;
import et.com.hmmk.rmt.service.MultipleChoiceAnsewerQueryService;
import et.com.hmmk.rmt.service.MultipleChoiceAnsewerService;
import et.com.hmmk.rmt.service.criteria.MultipleChoiceAnsewerCriteria;
import et.com.hmmk.rmt.service.dto.AnswerCreatorDTO;
import et.com.hmmk.rmt.service.dto.AnswerDTO;
import et.com.hmmk.rmt.service.dto.MultipleChoiceAnsewerDTO;
import et.com.hmmk.rmt.service.mapper.MultipleChoiceAnsewerMapper;
import et.com.hmmk.rmt.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link et.com.hmmk.rmt.domain.Answer}.
 */
@RestController
@RequestMapping("/api")
public class AnswerCreatorResource {

    private final Logger log = LoggerFactory.getLogger(AnswerCreatorResource.class);

    private static final String ENTITY_NAME = "answer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnswerService answerService;

    private final AnswerRepository answerRepository;

    private final MultipleChoiceAnsewerRepository multipleChoiceAnsewerRepository;

    private final MultipleChoiceAnsewerService multipleChoiceAnsewerService;

    private final MultipleChoiceAnsewerQueryService multipleChoiceAnsewerQueryService;

    private final MultipleChoiceAnsewerMapper multipleChoiceAnsewerMapper;

    public AnswerCreatorResource(
        AnswerService answerService,
        AnswerRepository answerRepository,
        MultipleChoiceAnsewerRepository multipleChoiceAnsewerRepository,
        MultipleChoiceAnsewerService multipleChoiceAnsewerService,
        MultipleChoiceAnsewerQueryService multipleChoiceAnsewerQueryService,
        MultipleChoiceAnsewerMapper multipleChoiceAnsewerMapper
    ) {
        this.answerService = answerService;
        this.answerRepository = answerRepository;
        this.multipleChoiceAnsewerRepository = multipleChoiceAnsewerRepository;
        this.multipleChoiceAnsewerService = multipleChoiceAnsewerService;
        this.multipleChoiceAnsewerQueryService = multipleChoiceAnsewerQueryService;
        this.multipleChoiceAnsewerMapper = multipleChoiceAnsewerMapper;
    }

    /**
     * {@code POST  /answers} : Create a new answer.
     *
     * @param answerCreatorDTO the answerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new answerDTO, or with status {@code 400 (Bad Request)} if the answer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/answer-creator")
    public ResponseEntity<AnswerDTO> createAnswer(@RequestBody AnswerCreatorDTO answerCreatorDTO) throws URISyntaxException {
        log.debug("REST request to save Answer : {}", answerCreatorDTO);
        if (answerCreatorDTO.getId() != null) {
            throw new BadRequestAlertException("A new answer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnswerDTO answerDTO = toAnswerDto(answerCreatorDTO);
        AnswerDTO result = answerService.save(answerDTO);
        answerCreatorDTO
            .getMultipleChoiceAnsewerDTOS()
            .forEach(multipleChoiceAnsewerDTO -> {
                multipleChoiceAnsewerDTO.setAnswer(result);
                multipleChoiceAnsewerService.save(multipleChoiceAnsewerDTO);
            });
        return ResponseEntity
            .created(new URI("/api/answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /answers/:id} : Updates an existing answer.
     *
     * @param id the id of the answerDTO to save.
     * @param answerCreatorDTO the answerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated answerDTO,
     * or with status {@code 400 (Bad Request)} if the answerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the answerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/answer-creator/{id}")
    public ResponseEntity<AnswerDTO> updateAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnswerCreatorDTO answerCreatorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Answer : {}, {}", id, answerCreatorDTO);
        if (answerCreatorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, answerCreatorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!answerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        AnswerDTO answerDTO = toAnswerDto(answerCreatorDTO);
        AnswerDTO result = answerService.save(answerDTO);
        MultipleChoiceAnsewerCriteria multipleChoiceAnsewerCriteria = new MultipleChoiceAnsewerCriteria();
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(result.getId());
        multipleChoiceAnsewerCriteria.setAnswerId(longFilter);
        List<MultipleChoiceAnsewerDTO> multipleChoiceAnsewerDTOList = multipleChoiceAnsewerQueryService.findByCriteria(
            multipleChoiceAnsewerCriteria
        );
        multipleChoiceAnsewerDTOList.forEach(multipleChoiceAnsewerDTO -> {
            multipleChoiceAnsewerRepository.delete(multipleChoiceAnsewerMapper.toEntity(multipleChoiceAnsewerDTO));
        });
        answerCreatorDTO
            .getMultipleChoiceAnsewerDTOS()
            .forEach(multipleChoiceAnsewerDTO -> {
                multipleChoiceAnsewerDTO.setAnswer(result);
                multipleChoiceAnsewerService.save(multipleChoiceAnsewerDTO);
            });
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, answerDTO.getId().toString()))
            .body(result);
    }

    public AnswerDTO toAnswerDto(AnswerCreatorDTO answerCreatorDTO) {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setId(answerCreatorDTO.getId());
        answerDTO.setBooleanAnswer(answerCreatorDTO.getBooleanAnswer());
        answerDTO.setDataType(answerCreatorDTO.getDataType());
        answerDTO.setDate(answerCreatorDTO.getDate());
        answerDTO.setDropDown(answerCreatorDTO.getDropDown());
        answerDTO.setFileName(answerCreatorDTO.getFileName());
        answerDTO.setFileUploaded(answerCreatorDTO.getFileUploaded());
        answerDTO.setFileUploadedContentType(answerCreatorDTO.getFileUploadedContentType());
        answerDTO.setFormProgresss(answerCreatorDTO.getFormProgresss());
        answerDTO.setMultipleChoice(answerCreatorDTO.getMultipleChoice());
        answerDTO.setNumber(answerCreatorDTO.getNumber());
        answerDTO.setParagraph(answerCreatorDTO.getParagraph());
        answerDTO.setQuestion(answerCreatorDTO.getQuestion());
        answerDTO.setShortAnswer(answerCreatorDTO.getShortAnswer());
        answerDTO.setSubmitedOn(answerCreatorDTO.getSubmitedOn());
        answerDTO.setTime(answerCreatorDTO.getTime());
        answerDTO.setUser(answerCreatorDTO.getUser());
        return answerDTO;
    }
}
