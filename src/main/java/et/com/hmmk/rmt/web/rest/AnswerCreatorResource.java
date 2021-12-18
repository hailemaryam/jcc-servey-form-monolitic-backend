package et.com.hmmk.rmt.web.rest;

import et.com.hmmk.rmt.repository.AnswerRepository;
import et.com.hmmk.rmt.repository.MultipleChoiceAnsewerRepository;
import et.com.hmmk.rmt.service.AnswerQueryService;
import et.com.hmmk.rmt.service.AnswerService;
import et.com.hmmk.rmt.service.MultipleChoiceAnsewerQueryService;
import et.com.hmmk.rmt.service.MultipleChoiceAnsewerService;
import et.com.hmmk.rmt.service.criteria.AnswerCriteria;
import et.com.hmmk.rmt.service.criteria.MultipleChoiceAnsewerCriteria;
import et.com.hmmk.rmt.service.dto.AnswerCreatorDTO;
import et.com.hmmk.rmt.service.dto.AnswerDTO;
import et.com.hmmk.rmt.service.dto.MultipleChoiceAnsewerDTO;
import et.com.hmmk.rmt.service.mapper.MultipleChoiceAnsewerMapper;
import et.com.hmmk.rmt.service.utility.FileStoreService;
import et.com.hmmk.rmt.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

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

    private final AnswerQueryService answerQueryService;

    private final FileStoreService fileStoreService;

    public AnswerCreatorResource(
        AnswerService answerService,
        AnswerRepository answerRepository,
        MultipleChoiceAnsewerRepository multipleChoiceAnsewerRepository,
        MultipleChoiceAnsewerService multipleChoiceAnsewerService,
        MultipleChoiceAnsewerQueryService multipleChoiceAnsewerQueryService,
        MultipleChoiceAnsewerMapper multipleChoiceAnsewerMapper,
        AnswerQueryService answerQueryService,
        FileStoreService fileStoreService
    ) {
        this.answerService = answerService;
        this.answerRepository = answerRepository;
        this.multipleChoiceAnsewerRepository = multipleChoiceAnsewerRepository;
        this.multipleChoiceAnsewerService = multipleChoiceAnsewerService;
        this.multipleChoiceAnsewerQueryService = multipleChoiceAnsewerQueryService;
        this.multipleChoiceAnsewerMapper = multipleChoiceAnsewerMapper;
        this.answerQueryService = answerQueryService;
        this.fileStoreService = fileStoreService;
    }

    /**
     * {@code POST  /answers} : Create a new answer.
     *
     * @param answerCreatorDTO the answerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new answerDTO, or with status {@code 400 (Bad Request)} if the answer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/answer-creator")
    public ResponseEntity<AnswerCreatorDTO> createAnswer(@RequestBody AnswerCreatorDTO answerCreatorDTO) throws URISyntaxException {
        log.debug("REST request to save Answer : {}", answerCreatorDTO);
        if (answerCreatorDTO.getId() != null) {
            throw new BadRequestAlertException("A new answer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnswerDTO answerDTO = toAnswerDto(answerCreatorDTO);
        answerDTO.setUser(answerCreatorDTO.getFormProgresss().getUser());
        answerDTO.setFileName(
            fileStoreService.store(answerDTO.getFileUploaded(), answerDTO.getFileUploadedContentType(), answerCreatorDTO.getFileName())
        );
        answerDTO.setFileUploadedContentType(null);
        answerDTO.setFileUploadedContentType(null);
        AnswerDTO result = answerService.save(answerDTO);
        if (answerCreatorDTO.getMultipleChoiceAnsewers() != null) {
            for (MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO : answerCreatorDTO.getMultipleChoiceAnsewers()) {
                multipleChoiceAnsewerDTO.setAnswer(result);
                MultipleChoiceAnsewerDTO savedMultipleChoice = multipleChoiceAnsewerService.save(multipleChoiceAnsewerDTO);
                multipleChoiceAnsewerDTO.setId(savedMultipleChoice.getId());
            }
        }
        return ResponseEntity
            .created(new URI("/api/answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(answerCreatorDTO);
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
    public ResponseEntity<AnswerCreatorDTO> updateAnswer(
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
        answerDTO.setUser(answerCreatorDTO.getFormProgresss().getUser());
        if (answerDTO.getFileUploadedContentType() != null && answerDTO.getFileUploaded() != null) {
            answerDTO.setFileName(
                fileStoreService.store(answerDTO.getFileUploaded(), answerDTO.getFileUploadedContentType(), answerCreatorDTO.getFileName())
            );
            answerDTO.setFileUploadedContentType(null);
            answerDTO.setFileUploadedContentType(null);
        }
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
        if (answerCreatorDTO.getMultipleChoiceAnsewers() != null) {
            for (MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO : answerCreatorDTO.getMultipleChoiceAnsewers()) {
                multipleChoiceAnsewerDTO.setAnswer(result);
                MultipleChoiceAnsewerDTO savedMultipleChoice = multipleChoiceAnsewerService.save(multipleChoiceAnsewerDTO);
                multipleChoiceAnsewerDTO.setId(savedMultipleChoice.getId());
            }
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(answerCreatorDTO);
    }

    @GetMapping("/answer-creator")
    public ResponseEntity<List<AnswerCreatorDTO>> getAllAnswers(AnswerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Answers by criteria: {}", criteria);
        Page<AnswerDTO> pageAnswerDTO = answerQueryService.findByCriteria(criteria, pageable);
        Page<AnswerCreatorDTO> page = pageAnswerDTO.map(answerDTO -> toAnswerCreatorDto(answerDTO));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
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

    public AnswerCreatorDTO toAnswerCreatorDto(AnswerDTO answerDto) {
        AnswerCreatorDTO answerCreatorDto = new AnswerCreatorDTO();
        answerCreatorDto.setId(answerDto.getId());
        answerCreatorDto.setBooleanAnswer(answerDto.getBooleanAnswer());
        answerCreatorDto.setDataType(answerDto.getDataType());
        answerCreatorDto.setDate(answerDto.getDate());
        answerCreatorDto.setDropDown(answerDto.getDropDown());
        answerCreatorDto.setFileName(answerDto.getFileName());
        answerCreatorDto.setFileUploaded(answerDto.getFileUploaded());
        answerCreatorDto.setFileUploadedContentType(answerDto.getFileUploadedContentType());
        answerCreatorDto.setFormProgresss(answerDto.getFormProgresss());
        answerCreatorDto.setMultipleChoice(answerDto.getMultipleChoice());
        answerCreatorDto.setNumber(answerDto.getNumber());
        answerCreatorDto.setParagraph(answerDto.getParagraph());
        answerCreatorDto.setQuestion(answerDto.getQuestion());
        answerCreatorDto.setShortAnswer(answerDto.getShortAnswer());
        answerCreatorDto.setSubmitedOn(answerDto.getSubmitedOn());
        answerCreatorDto.setTime(answerDto.getTime());
        answerCreatorDto.setUser(answerDto.getUser());
        MultipleChoiceAnsewerCriteria multipleChoiceAnsewerCriteria = new MultipleChoiceAnsewerCriteria();
        LongFilter answerFilter = new LongFilter();
        answerFilter.setEquals(answerDto.getId());
        multipleChoiceAnsewerCriteria.setAnswerId(answerFilter);
        answerCreatorDto.setMultipleChoiceAnsewers(
            multipleChoiceAnsewerQueryService.findByCriteria(multipleChoiceAnsewerCriteria).stream().collect(Collectors.toSet())
        );
        return answerCreatorDto;
    }
}
