package et.com.hmmk.rmt.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import et.com.hmmk.rmt.IntegrationTest;
import et.com.hmmk.rmt.domain.Answer;
import et.com.hmmk.rmt.domain.FormProgresss;
import et.com.hmmk.rmt.domain.MultipleChoiceAnsewer;
import et.com.hmmk.rmt.domain.Question;
import et.com.hmmk.rmt.domain.User;
import et.com.hmmk.rmt.domain.enumeration.DataType;
import et.com.hmmk.rmt.repository.AnswerRepository;
import et.com.hmmk.rmt.service.criteria.AnswerCriteria;
import et.com.hmmk.rmt.service.dto.AnswerDTO;
import et.com.hmmk.rmt.service.mapper.AnswerMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link AnswerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnswerResourceIT {

    private static final Double DEFAULT_NUMBER = 1D;
    private static final Double UPDATED_NUMBER = 2D;
    private static final Double SMALLER_NUMBER = 1D - 1D;

    private static final Boolean DEFAULT_BOOLEAN_ANSWER = false;
    private static final Boolean UPDATED_BOOLEAN_ANSWER = true;

    private static final String DEFAULT_SHORT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_ANSWER = "BBBBBBBBBB";

    private static final String DEFAULT_PARAGRAPH = "AAAAAAAAAA";
    private static final String UPDATED_PARAGRAPH = "BBBBBBBBBB";

    private static final String DEFAULT_MULTIPLE_CHOICE = "AAAAAAAAAA";
    private static final String UPDATED_MULTIPLE_CHOICE = "BBBBBBBBBB";

    private static final String DEFAULT_DROP_DOWN = "AAAAAAAAAA";
    private static final String UPDATED_DROP_DOWN = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE_UPLOADED = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_UPLOADED = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_UPLOADED_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_UPLOADED_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Duration DEFAULT_TIME = Duration.ofHours(6);
    private static final Duration UPDATED_TIME = Duration.ofHours(12);
    private static final Duration SMALLER_TIME = Duration.ofHours(5);

    private static final Instant DEFAULT_SUBMITED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBMITED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final DataType DEFAULT_DATA_TYPE = DataType.NUMBER;
    private static final DataType UPDATED_DATA_TYPE = DataType.BOOLEAN;

    private static final String ENTITY_API_URL = "/api/answers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnswerMockMvc;

    private Answer answer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Answer createEntity(EntityManager em) {
        Answer answer = new Answer()
            .number(DEFAULT_NUMBER)
            .booleanAnswer(DEFAULT_BOOLEAN_ANSWER)
            .shortAnswer(DEFAULT_SHORT_ANSWER)
            .paragraph(DEFAULT_PARAGRAPH)
            .multipleChoice(DEFAULT_MULTIPLE_CHOICE)
            .dropDown(DEFAULT_DROP_DOWN)
            .fileUploaded(DEFAULT_FILE_UPLOADED)
            .fileUploadedContentType(DEFAULT_FILE_UPLOADED_CONTENT_TYPE)
            .fileName(DEFAULT_FILE_NAME)
            .date(DEFAULT_DATE)
            .time(DEFAULT_TIME)
            .submitedOn(DEFAULT_SUBMITED_ON)
            .dataType(DEFAULT_DATA_TYPE);
        return answer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Answer createUpdatedEntity(EntityManager em) {
        Answer answer = new Answer()
            .number(UPDATED_NUMBER)
            .booleanAnswer(UPDATED_BOOLEAN_ANSWER)
            .shortAnswer(UPDATED_SHORT_ANSWER)
            .paragraph(UPDATED_PARAGRAPH)
            .multipleChoice(UPDATED_MULTIPLE_CHOICE)
            .dropDown(UPDATED_DROP_DOWN)
            .fileUploaded(UPDATED_FILE_UPLOADED)
            .fileUploadedContentType(UPDATED_FILE_UPLOADED_CONTENT_TYPE)
            .fileName(UPDATED_FILE_NAME)
            .date(UPDATED_DATE)
            .time(UPDATED_TIME)
            .submitedOn(UPDATED_SUBMITED_ON)
            .dataType(UPDATED_DATA_TYPE);
        return answer;
    }

    @BeforeEach
    public void initTest() {
        answer = createEntity(em);
    }

    @Test
    @Transactional
    void createAnswer() throws Exception {
        int databaseSizeBeforeCreate = answerRepository.findAll().size();
        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);
        restAnswerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isCreated());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeCreate + 1);
        Answer testAnswer = answerList.get(answerList.size() - 1);
        assertThat(testAnswer.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testAnswer.getBooleanAnswer()).isEqualTo(DEFAULT_BOOLEAN_ANSWER);
        assertThat(testAnswer.getShortAnswer()).isEqualTo(DEFAULT_SHORT_ANSWER);
        assertThat(testAnswer.getParagraph()).isEqualTo(DEFAULT_PARAGRAPH);
        assertThat(testAnswer.getMultipleChoice()).isEqualTo(DEFAULT_MULTIPLE_CHOICE);
        assertThat(testAnswer.getDropDown()).isEqualTo(DEFAULT_DROP_DOWN);
        assertThat(testAnswer.getFileUploaded()).isEqualTo(DEFAULT_FILE_UPLOADED);
        assertThat(testAnswer.getFileUploadedContentType()).isEqualTo(DEFAULT_FILE_UPLOADED_CONTENT_TYPE);
        assertThat(testAnswer.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testAnswer.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAnswer.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testAnswer.getSubmitedOn()).isEqualTo(DEFAULT_SUBMITED_ON);
        assertThat(testAnswer.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
    }

    @Test
    @Transactional
    void createAnswerWithExistingId() throws Exception {
        // Create the Answer with an existing ID
        answer.setId(1L);
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        int databaseSizeBeforeCreate = answerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnswerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAnswers() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList
        restAnswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(answer.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].booleanAnswer").value(hasItem(DEFAULT_BOOLEAN_ANSWER.booleanValue())))
            .andExpect(jsonPath("$.[*].shortAnswer").value(hasItem(DEFAULT_SHORT_ANSWER)))
            .andExpect(jsonPath("$.[*].paragraph").value(hasItem(DEFAULT_PARAGRAPH.toString())))
            .andExpect(jsonPath("$.[*].multipleChoice").value(hasItem(DEFAULT_MULTIPLE_CHOICE)))
            .andExpect(jsonPath("$.[*].dropDown").value(hasItem(DEFAULT_DROP_DOWN)))
            .andExpect(jsonPath("$.[*].fileUploadedContentType").value(hasItem(DEFAULT_FILE_UPLOADED_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileUploaded").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_UPLOADED))))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].submitedOn").value(hasItem(DEFAULT_SUBMITED_ON.toString())))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())));
    }

    @Test
    @Transactional
    void getAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get the answer
        restAnswerMockMvc
            .perform(get(ENTITY_API_URL_ID, answer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(answer.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.doubleValue()))
            .andExpect(jsonPath("$.booleanAnswer").value(DEFAULT_BOOLEAN_ANSWER.booleanValue()))
            .andExpect(jsonPath("$.shortAnswer").value(DEFAULT_SHORT_ANSWER))
            .andExpect(jsonPath("$.paragraph").value(DEFAULT_PARAGRAPH.toString()))
            .andExpect(jsonPath("$.multipleChoice").value(DEFAULT_MULTIPLE_CHOICE))
            .andExpect(jsonPath("$.dropDown").value(DEFAULT_DROP_DOWN))
            .andExpect(jsonPath("$.fileUploadedContentType").value(DEFAULT_FILE_UPLOADED_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileUploaded").value(Base64Utils.encodeToString(DEFAULT_FILE_UPLOADED)))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.submitedOn").value(DEFAULT_SUBMITED_ON.toString()))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()));
    }

    @Test
    @Transactional
    void getAnswersByIdFiltering() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        Long id = answer.getId();

        defaultAnswerShouldBeFound("id.equals=" + id);
        defaultAnswerShouldNotBeFound("id.notEquals=" + id);

        defaultAnswerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAnswerShouldNotBeFound("id.greaterThan=" + id);

        defaultAnswerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAnswerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAnswersByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where number equals to DEFAULT_NUMBER
        defaultAnswerShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the answerList where number equals to UPDATED_NUMBER
        defaultAnswerShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllAnswersByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where number not equals to DEFAULT_NUMBER
        defaultAnswerShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the answerList where number not equals to UPDATED_NUMBER
        defaultAnswerShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllAnswersByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultAnswerShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the answerList where number equals to UPDATED_NUMBER
        defaultAnswerShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllAnswersByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where number is not null
        defaultAnswerShouldBeFound("number.specified=true");

        // Get all the answerList where number is null
        defaultAnswerShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllAnswersByNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where number is greater than or equal to DEFAULT_NUMBER
        defaultAnswerShouldBeFound("number.greaterThanOrEqual=" + DEFAULT_NUMBER);

        // Get all the answerList where number is greater than or equal to UPDATED_NUMBER
        defaultAnswerShouldNotBeFound("number.greaterThanOrEqual=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllAnswersByNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where number is less than or equal to DEFAULT_NUMBER
        defaultAnswerShouldBeFound("number.lessThanOrEqual=" + DEFAULT_NUMBER);

        // Get all the answerList where number is less than or equal to SMALLER_NUMBER
        defaultAnswerShouldNotBeFound("number.lessThanOrEqual=" + SMALLER_NUMBER);
    }

    @Test
    @Transactional
    void getAllAnswersByNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where number is less than DEFAULT_NUMBER
        defaultAnswerShouldNotBeFound("number.lessThan=" + DEFAULT_NUMBER);

        // Get all the answerList where number is less than UPDATED_NUMBER
        defaultAnswerShouldBeFound("number.lessThan=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllAnswersByNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where number is greater than DEFAULT_NUMBER
        defaultAnswerShouldNotBeFound("number.greaterThan=" + DEFAULT_NUMBER);

        // Get all the answerList where number is greater than SMALLER_NUMBER
        defaultAnswerShouldBeFound("number.greaterThan=" + SMALLER_NUMBER);
    }

    @Test
    @Transactional
    void getAllAnswersByBooleanAnswerIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where booleanAnswer equals to DEFAULT_BOOLEAN_ANSWER
        defaultAnswerShouldBeFound("booleanAnswer.equals=" + DEFAULT_BOOLEAN_ANSWER);

        // Get all the answerList where booleanAnswer equals to UPDATED_BOOLEAN_ANSWER
        defaultAnswerShouldNotBeFound("booleanAnswer.equals=" + UPDATED_BOOLEAN_ANSWER);
    }

    @Test
    @Transactional
    void getAllAnswersByBooleanAnswerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where booleanAnswer not equals to DEFAULT_BOOLEAN_ANSWER
        defaultAnswerShouldNotBeFound("booleanAnswer.notEquals=" + DEFAULT_BOOLEAN_ANSWER);

        // Get all the answerList where booleanAnswer not equals to UPDATED_BOOLEAN_ANSWER
        defaultAnswerShouldBeFound("booleanAnswer.notEquals=" + UPDATED_BOOLEAN_ANSWER);
    }

    @Test
    @Transactional
    void getAllAnswersByBooleanAnswerIsInShouldWork() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where booleanAnswer in DEFAULT_BOOLEAN_ANSWER or UPDATED_BOOLEAN_ANSWER
        defaultAnswerShouldBeFound("booleanAnswer.in=" + DEFAULT_BOOLEAN_ANSWER + "," + UPDATED_BOOLEAN_ANSWER);

        // Get all the answerList where booleanAnswer equals to UPDATED_BOOLEAN_ANSWER
        defaultAnswerShouldNotBeFound("booleanAnswer.in=" + UPDATED_BOOLEAN_ANSWER);
    }

    @Test
    @Transactional
    void getAllAnswersByBooleanAnswerIsNullOrNotNull() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where booleanAnswer is not null
        defaultAnswerShouldBeFound("booleanAnswer.specified=true");

        // Get all the answerList where booleanAnswer is null
        defaultAnswerShouldNotBeFound("booleanAnswer.specified=false");
    }

    @Test
    @Transactional
    void getAllAnswersByShortAnswerIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where shortAnswer equals to DEFAULT_SHORT_ANSWER
        defaultAnswerShouldBeFound("shortAnswer.equals=" + DEFAULT_SHORT_ANSWER);

        // Get all the answerList where shortAnswer equals to UPDATED_SHORT_ANSWER
        defaultAnswerShouldNotBeFound("shortAnswer.equals=" + UPDATED_SHORT_ANSWER);
    }

    @Test
    @Transactional
    void getAllAnswersByShortAnswerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where shortAnswer not equals to DEFAULT_SHORT_ANSWER
        defaultAnswerShouldNotBeFound("shortAnswer.notEquals=" + DEFAULT_SHORT_ANSWER);

        // Get all the answerList where shortAnswer not equals to UPDATED_SHORT_ANSWER
        defaultAnswerShouldBeFound("shortAnswer.notEquals=" + UPDATED_SHORT_ANSWER);
    }

    @Test
    @Transactional
    void getAllAnswersByShortAnswerIsInShouldWork() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where shortAnswer in DEFAULT_SHORT_ANSWER or UPDATED_SHORT_ANSWER
        defaultAnswerShouldBeFound("shortAnswer.in=" + DEFAULT_SHORT_ANSWER + "," + UPDATED_SHORT_ANSWER);

        // Get all the answerList where shortAnswer equals to UPDATED_SHORT_ANSWER
        defaultAnswerShouldNotBeFound("shortAnswer.in=" + UPDATED_SHORT_ANSWER);
    }

    @Test
    @Transactional
    void getAllAnswersByShortAnswerIsNullOrNotNull() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where shortAnswer is not null
        defaultAnswerShouldBeFound("shortAnswer.specified=true");

        // Get all the answerList where shortAnswer is null
        defaultAnswerShouldNotBeFound("shortAnswer.specified=false");
    }

    @Test
    @Transactional
    void getAllAnswersByShortAnswerContainsSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where shortAnswer contains DEFAULT_SHORT_ANSWER
        defaultAnswerShouldBeFound("shortAnswer.contains=" + DEFAULT_SHORT_ANSWER);

        // Get all the answerList where shortAnswer contains UPDATED_SHORT_ANSWER
        defaultAnswerShouldNotBeFound("shortAnswer.contains=" + UPDATED_SHORT_ANSWER);
    }

    @Test
    @Transactional
    void getAllAnswersByShortAnswerNotContainsSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where shortAnswer does not contain DEFAULT_SHORT_ANSWER
        defaultAnswerShouldNotBeFound("shortAnswer.doesNotContain=" + DEFAULT_SHORT_ANSWER);

        // Get all the answerList where shortAnswer does not contain UPDATED_SHORT_ANSWER
        defaultAnswerShouldBeFound("shortAnswer.doesNotContain=" + UPDATED_SHORT_ANSWER);
    }

    @Test
    @Transactional
    void getAllAnswersByMultipleChoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where multipleChoice equals to DEFAULT_MULTIPLE_CHOICE
        defaultAnswerShouldBeFound("multipleChoice.equals=" + DEFAULT_MULTIPLE_CHOICE);

        // Get all the answerList where multipleChoice equals to UPDATED_MULTIPLE_CHOICE
        defaultAnswerShouldNotBeFound("multipleChoice.equals=" + UPDATED_MULTIPLE_CHOICE);
    }

    @Test
    @Transactional
    void getAllAnswersByMultipleChoiceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where multipleChoice not equals to DEFAULT_MULTIPLE_CHOICE
        defaultAnswerShouldNotBeFound("multipleChoice.notEquals=" + DEFAULT_MULTIPLE_CHOICE);

        // Get all the answerList where multipleChoice not equals to UPDATED_MULTIPLE_CHOICE
        defaultAnswerShouldBeFound("multipleChoice.notEquals=" + UPDATED_MULTIPLE_CHOICE);
    }

    @Test
    @Transactional
    void getAllAnswersByMultipleChoiceIsInShouldWork() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where multipleChoice in DEFAULT_MULTIPLE_CHOICE or UPDATED_MULTIPLE_CHOICE
        defaultAnswerShouldBeFound("multipleChoice.in=" + DEFAULT_MULTIPLE_CHOICE + "," + UPDATED_MULTIPLE_CHOICE);

        // Get all the answerList where multipleChoice equals to UPDATED_MULTIPLE_CHOICE
        defaultAnswerShouldNotBeFound("multipleChoice.in=" + UPDATED_MULTIPLE_CHOICE);
    }

    @Test
    @Transactional
    void getAllAnswersByMultipleChoiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where multipleChoice is not null
        defaultAnswerShouldBeFound("multipleChoice.specified=true");

        // Get all the answerList where multipleChoice is null
        defaultAnswerShouldNotBeFound("multipleChoice.specified=false");
    }

    @Test
    @Transactional
    void getAllAnswersByMultipleChoiceContainsSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where multipleChoice contains DEFAULT_MULTIPLE_CHOICE
        defaultAnswerShouldBeFound("multipleChoice.contains=" + DEFAULT_MULTIPLE_CHOICE);

        // Get all the answerList where multipleChoice contains UPDATED_MULTIPLE_CHOICE
        defaultAnswerShouldNotBeFound("multipleChoice.contains=" + UPDATED_MULTIPLE_CHOICE);
    }

    @Test
    @Transactional
    void getAllAnswersByMultipleChoiceNotContainsSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where multipleChoice does not contain DEFAULT_MULTIPLE_CHOICE
        defaultAnswerShouldNotBeFound("multipleChoice.doesNotContain=" + DEFAULT_MULTIPLE_CHOICE);

        // Get all the answerList where multipleChoice does not contain UPDATED_MULTIPLE_CHOICE
        defaultAnswerShouldBeFound("multipleChoice.doesNotContain=" + UPDATED_MULTIPLE_CHOICE);
    }

    @Test
    @Transactional
    void getAllAnswersByDropDownIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where dropDown equals to DEFAULT_DROP_DOWN
        defaultAnswerShouldBeFound("dropDown.equals=" + DEFAULT_DROP_DOWN);

        // Get all the answerList where dropDown equals to UPDATED_DROP_DOWN
        defaultAnswerShouldNotBeFound("dropDown.equals=" + UPDATED_DROP_DOWN);
    }

    @Test
    @Transactional
    void getAllAnswersByDropDownIsNotEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where dropDown not equals to DEFAULT_DROP_DOWN
        defaultAnswerShouldNotBeFound("dropDown.notEquals=" + DEFAULT_DROP_DOWN);

        // Get all the answerList where dropDown not equals to UPDATED_DROP_DOWN
        defaultAnswerShouldBeFound("dropDown.notEquals=" + UPDATED_DROP_DOWN);
    }

    @Test
    @Transactional
    void getAllAnswersByDropDownIsInShouldWork() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where dropDown in DEFAULT_DROP_DOWN or UPDATED_DROP_DOWN
        defaultAnswerShouldBeFound("dropDown.in=" + DEFAULT_DROP_DOWN + "," + UPDATED_DROP_DOWN);

        // Get all the answerList where dropDown equals to UPDATED_DROP_DOWN
        defaultAnswerShouldNotBeFound("dropDown.in=" + UPDATED_DROP_DOWN);
    }

    @Test
    @Transactional
    void getAllAnswersByDropDownIsNullOrNotNull() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where dropDown is not null
        defaultAnswerShouldBeFound("dropDown.specified=true");

        // Get all the answerList where dropDown is null
        defaultAnswerShouldNotBeFound("dropDown.specified=false");
    }

    @Test
    @Transactional
    void getAllAnswersByDropDownContainsSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where dropDown contains DEFAULT_DROP_DOWN
        defaultAnswerShouldBeFound("dropDown.contains=" + DEFAULT_DROP_DOWN);

        // Get all the answerList where dropDown contains UPDATED_DROP_DOWN
        defaultAnswerShouldNotBeFound("dropDown.contains=" + UPDATED_DROP_DOWN);
    }

    @Test
    @Transactional
    void getAllAnswersByDropDownNotContainsSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where dropDown does not contain DEFAULT_DROP_DOWN
        defaultAnswerShouldNotBeFound("dropDown.doesNotContain=" + DEFAULT_DROP_DOWN);

        // Get all the answerList where dropDown does not contain UPDATED_DROP_DOWN
        defaultAnswerShouldBeFound("dropDown.doesNotContain=" + UPDATED_DROP_DOWN);
    }

    @Test
    @Transactional
    void getAllAnswersByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where fileName equals to DEFAULT_FILE_NAME
        defaultAnswerShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the answerList where fileName equals to UPDATED_FILE_NAME
        defaultAnswerShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllAnswersByFileNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where fileName not equals to DEFAULT_FILE_NAME
        defaultAnswerShouldNotBeFound("fileName.notEquals=" + DEFAULT_FILE_NAME);

        // Get all the answerList where fileName not equals to UPDATED_FILE_NAME
        defaultAnswerShouldBeFound("fileName.notEquals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllAnswersByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultAnswerShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the answerList where fileName equals to UPDATED_FILE_NAME
        defaultAnswerShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllAnswersByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where fileName is not null
        defaultAnswerShouldBeFound("fileName.specified=true");

        // Get all the answerList where fileName is null
        defaultAnswerShouldNotBeFound("fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllAnswersByFileNameContainsSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where fileName contains DEFAULT_FILE_NAME
        defaultAnswerShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the answerList where fileName contains UPDATED_FILE_NAME
        defaultAnswerShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllAnswersByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where fileName does not contain DEFAULT_FILE_NAME
        defaultAnswerShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the answerList where fileName does not contain UPDATED_FILE_NAME
        defaultAnswerShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllAnswersByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where date equals to DEFAULT_DATE
        defaultAnswerShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the answerList where date equals to UPDATED_DATE
        defaultAnswerShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllAnswersByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where date not equals to DEFAULT_DATE
        defaultAnswerShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the answerList where date not equals to UPDATED_DATE
        defaultAnswerShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllAnswersByDateIsInShouldWork() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where date in DEFAULT_DATE or UPDATED_DATE
        defaultAnswerShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the answerList where date equals to UPDATED_DATE
        defaultAnswerShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllAnswersByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where date is not null
        defaultAnswerShouldBeFound("date.specified=true");

        // Get all the answerList where date is null
        defaultAnswerShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllAnswersByTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where time equals to DEFAULT_TIME
        defaultAnswerShouldBeFound("time.equals=" + DEFAULT_TIME);

        // Get all the answerList where time equals to UPDATED_TIME
        defaultAnswerShouldNotBeFound("time.equals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    void getAllAnswersByTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where time not equals to DEFAULT_TIME
        defaultAnswerShouldNotBeFound("time.notEquals=" + DEFAULT_TIME);

        // Get all the answerList where time not equals to UPDATED_TIME
        defaultAnswerShouldBeFound("time.notEquals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    void getAllAnswersByTimeIsInShouldWork() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where time in DEFAULT_TIME or UPDATED_TIME
        defaultAnswerShouldBeFound("time.in=" + DEFAULT_TIME + "," + UPDATED_TIME);

        // Get all the answerList where time equals to UPDATED_TIME
        defaultAnswerShouldNotBeFound("time.in=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    void getAllAnswersByTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where time is not null
        defaultAnswerShouldBeFound("time.specified=true");

        // Get all the answerList where time is null
        defaultAnswerShouldNotBeFound("time.specified=false");
    }

    @Test
    @Transactional
    void getAllAnswersByTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where time is greater than or equal to DEFAULT_TIME
        defaultAnswerShouldBeFound("time.greaterThanOrEqual=" + DEFAULT_TIME);

        // Get all the answerList where time is greater than or equal to UPDATED_TIME
        defaultAnswerShouldNotBeFound("time.greaterThanOrEqual=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    void getAllAnswersByTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where time is less than or equal to DEFAULT_TIME
        defaultAnswerShouldBeFound("time.lessThanOrEqual=" + DEFAULT_TIME);

        // Get all the answerList where time is less than or equal to SMALLER_TIME
        defaultAnswerShouldNotBeFound("time.lessThanOrEqual=" + SMALLER_TIME);
    }

    @Test
    @Transactional
    void getAllAnswersByTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where time is less than DEFAULT_TIME
        defaultAnswerShouldNotBeFound("time.lessThan=" + DEFAULT_TIME);

        // Get all the answerList where time is less than UPDATED_TIME
        defaultAnswerShouldBeFound("time.lessThan=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    void getAllAnswersByTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where time is greater than DEFAULT_TIME
        defaultAnswerShouldNotBeFound("time.greaterThan=" + DEFAULT_TIME);

        // Get all the answerList where time is greater than SMALLER_TIME
        defaultAnswerShouldBeFound("time.greaterThan=" + SMALLER_TIME);
    }

    @Test
    @Transactional
    void getAllAnswersBySubmitedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where submitedOn equals to DEFAULT_SUBMITED_ON
        defaultAnswerShouldBeFound("submitedOn.equals=" + DEFAULT_SUBMITED_ON);

        // Get all the answerList where submitedOn equals to UPDATED_SUBMITED_ON
        defaultAnswerShouldNotBeFound("submitedOn.equals=" + UPDATED_SUBMITED_ON);
    }

    @Test
    @Transactional
    void getAllAnswersBySubmitedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where submitedOn not equals to DEFAULT_SUBMITED_ON
        defaultAnswerShouldNotBeFound("submitedOn.notEquals=" + DEFAULT_SUBMITED_ON);

        // Get all the answerList where submitedOn not equals to UPDATED_SUBMITED_ON
        defaultAnswerShouldBeFound("submitedOn.notEquals=" + UPDATED_SUBMITED_ON);
    }

    @Test
    @Transactional
    void getAllAnswersBySubmitedOnIsInShouldWork() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where submitedOn in DEFAULT_SUBMITED_ON or UPDATED_SUBMITED_ON
        defaultAnswerShouldBeFound("submitedOn.in=" + DEFAULT_SUBMITED_ON + "," + UPDATED_SUBMITED_ON);

        // Get all the answerList where submitedOn equals to UPDATED_SUBMITED_ON
        defaultAnswerShouldNotBeFound("submitedOn.in=" + UPDATED_SUBMITED_ON);
    }

    @Test
    @Transactional
    void getAllAnswersBySubmitedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where submitedOn is not null
        defaultAnswerShouldBeFound("submitedOn.specified=true");

        // Get all the answerList where submitedOn is null
        defaultAnswerShouldNotBeFound("submitedOn.specified=false");
    }

    @Test
    @Transactional
    void getAllAnswersByDataTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where dataType equals to DEFAULT_DATA_TYPE
        defaultAnswerShouldBeFound("dataType.equals=" + DEFAULT_DATA_TYPE);

        // Get all the answerList where dataType equals to UPDATED_DATA_TYPE
        defaultAnswerShouldNotBeFound("dataType.equals=" + UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void getAllAnswersByDataTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where dataType not equals to DEFAULT_DATA_TYPE
        defaultAnswerShouldNotBeFound("dataType.notEquals=" + DEFAULT_DATA_TYPE);

        // Get all the answerList where dataType not equals to UPDATED_DATA_TYPE
        defaultAnswerShouldBeFound("dataType.notEquals=" + UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void getAllAnswersByDataTypeIsInShouldWork() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where dataType in DEFAULT_DATA_TYPE or UPDATED_DATA_TYPE
        defaultAnswerShouldBeFound("dataType.in=" + DEFAULT_DATA_TYPE + "," + UPDATED_DATA_TYPE);

        // Get all the answerList where dataType equals to UPDATED_DATA_TYPE
        defaultAnswerShouldNotBeFound("dataType.in=" + UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void getAllAnswersByDataTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where dataType is not null
        defaultAnswerShouldBeFound("dataType.specified=true");

        // Get all the answerList where dataType is null
        defaultAnswerShouldNotBeFound("dataType.specified=false");
    }

    @Test
    @Transactional
    void getAllAnswersByMultipleChoiceAnsewerIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);
        MultipleChoiceAnsewer multipleChoiceAnsewer;
        if (TestUtil.findAll(em, MultipleChoiceAnsewer.class).isEmpty()) {
            multipleChoiceAnsewer = MultipleChoiceAnsewerResourceIT.createEntity(em);
            em.persist(multipleChoiceAnsewer);
            em.flush();
        } else {
            multipleChoiceAnsewer = TestUtil.findAll(em, MultipleChoiceAnsewer.class).get(0);
        }
        em.persist(multipleChoiceAnsewer);
        em.flush();
        answer.addMultipleChoiceAnsewer(multipleChoiceAnsewer);
        answerRepository.saveAndFlush(answer);
        Long multipleChoiceAnsewerId = multipleChoiceAnsewer.getId();

        // Get all the answerList where multipleChoiceAnsewer equals to multipleChoiceAnsewerId
        defaultAnswerShouldBeFound("multipleChoiceAnsewerId.equals=" + multipleChoiceAnsewerId);

        // Get all the answerList where multipleChoiceAnsewer equals to (multipleChoiceAnsewerId + 1)
        defaultAnswerShouldNotBeFound("multipleChoiceAnsewerId.equals=" + (multipleChoiceAnsewerId + 1));
    }

    @Test
    @Transactional
    void getAllAnswersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            user = UserResourceIT.createEntity(em);
            em.persist(user);
            em.flush();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        answer.setUser(user);
        answerRepository.saveAndFlush(answer);
        Long userId = user.getId();

        // Get all the answerList where user equals to userId
        defaultAnswerShouldBeFound("userId.equals=" + userId);

        // Get all the answerList where user equals to (userId + 1)
        defaultAnswerShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllAnswersByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);
        Question question;
        if (TestUtil.findAll(em, Question.class).isEmpty()) {
            question = QuestionResourceIT.createEntity(em);
            em.persist(question);
            em.flush();
        } else {
            question = TestUtil.findAll(em, Question.class).get(0);
        }
        em.persist(question);
        em.flush();
        answer.setQuestion(question);
        answerRepository.saveAndFlush(answer);
        Long questionId = question.getId();

        // Get all the answerList where question equals to questionId
        defaultAnswerShouldBeFound("questionId.equals=" + questionId);

        // Get all the answerList where question equals to (questionId + 1)
        defaultAnswerShouldNotBeFound("questionId.equals=" + (questionId + 1));
    }

    @Test
    @Transactional
    void getAllAnswersByFormProgresssIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);
        FormProgresss formProgresss;
        if (TestUtil.findAll(em, FormProgresss.class).isEmpty()) {
            formProgresss = FormProgresssResourceIT.createEntity(em);
            em.persist(formProgresss);
            em.flush();
        } else {
            formProgresss = TestUtil.findAll(em, FormProgresss.class).get(0);
        }
        em.persist(formProgresss);
        em.flush();
        answer.setFormProgresss(formProgresss);
        answerRepository.saveAndFlush(answer);
        Long formProgresssId = formProgresss.getId();

        // Get all the answerList where formProgresss equals to formProgresssId
        defaultAnswerShouldBeFound("formProgresssId.equals=" + formProgresssId);

        // Get all the answerList where formProgresss equals to (formProgresssId + 1)
        defaultAnswerShouldNotBeFound("formProgresssId.equals=" + (formProgresssId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnswerShouldBeFound(String filter) throws Exception {
        restAnswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(answer.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].booleanAnswer").value(hasItem(DEFAULT_BOOLEAN_ANSWER.booleanValue())))
            .andExpect(jsonPath("$.[*].shortAnswer").value(hasItem(DEFAULT_SHORT_ANSWER)))
            .andExpect(jsonPath("$.[*].paragraph").value(hasItem(DEFAULT_PARAGRAPH.toString())))
            .andExpect(jsonPath("$.[*].multipleChoice").value(hasItem(DEFAULT_MULTIPLE_CHOICE)))
            .andExpect(jsonPath("$.[*].dropDown").value(hasItem(DEFAULT_DROP_DOWN)))
            .andExpect(jsonPath("$.[*].fileUploadedContentType").value(hasItem(DEFAULT_FILE_UPLOADED_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileUploaded").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_UPLOADED))))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].submitedOn").value(hasItem(DEFAULT_SUBMITED_ON.toString())))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())));

        // Check, that the count call also returns 1
        restAnswerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnswerShouldNotBeFound(String filter) throws Exception {
        restAnswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnswerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAnswer() throws Exception {
        // Get the answer
        restAnswerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        int databaseSizeBeforeUpdate = answerRepository.findAll().size();

        // Update the answer
        Answer updatedAnswer = answerRepository.findById(answer.getId()).get();
        // Disconnect from session so that the updates on updatedAnswer are not directly saved in db
        em.detach(updatedAnswer);
        updatedAnswer
            .number(UPDATED_NUMBER)
            .booleanAnswer(UPDATED_BOOLEAN_ANSWER)
            .shortAnswer(UPDATED_SHORT_ANSWER)
            .paragraph(UPDATED_PARAGRAPH)
            .multipleChoice(UPDATED_MULTIPLE_CHOICE)
            .dropDown(UPDATED_DROP_DOWN)
            .fileUploaded(UPDATED_FILE_UPLOADED)
            .fileUploadedContentType(UPDATED_FILE_UPLOADED_CONTENT_TYPE)
            .fileName(UPDATED_FILE_NAME)
            .date(UPDATED_DATE)
            .time(UPDATED_TIME)
            .submitedOn(UPDATED_SUBMITED_ON)
            .dataType(UPDATED_DATA_TYPE);
        AnswerDTO answerDTO = answerMapper.toDto(updatedAnswer);

        restAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, answerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(answerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeUpdate);
        Answer testAnswer = answerList.get(answerList.size() - 1);
        assertThat(testAnswer.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testAnswer.getBooleanAnswer()).isEqualTo(UPDATED_BOOLEAN_ANSWER);
        assertThat(testAnswer.getShortAnswer()).isEqualTo(UPDATED_SHORT_ANSWER);
        assertThat(testAnswer.getParagraph()).isEqualTo(UPDATED_PARAGRAPH);
        assertThat(testAnswer.getMultipleChoice()).isEqualTo(UPDATED_MULTIPLE_CHOICE);
        assertThat(testAnswer.getDropDown()).isEqualTo(UPDATED_DROP_DOWN);
        assertThat(testAnswer.getFileUploaded()).isEqualTo(UPDATED_FILE_UPLOADED);
        assertThat(testAnswer.getFileUploadedContentType()).isEqualTo(UPDATED_FILE_UPLOADED_CONTENT_TYPE);
        assertThat(testAnswer.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testAnswer.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAnswer.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testAnswer.getSubmitedOn()).isEqualTo(UPDATED_SUBMITED_ON);
        assertThat(testAnswer.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingAnswer() throws Exception {
        int databaseSizeBeforeUpdate = answerRepository.findAll().size();
        answer.setId(count.incrementAndGet());

        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, answerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(answerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnswer() throws Exception {
        int databaseSizeBeforeUpdate = answerRepository.findAll().size();
        answer.setId(count.incrementAndGet());

        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(answerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnswer() throws Exception {
        int databaseSizeBeforeUpdate = answerRepository.findAll().size();
        answer.setId(count.incrementAndGet());

        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnswerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnswerWithPatch() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        int databaseSizeBeforeUpdate = answerRepository.findAll().size();

        // Update the answer using partial update
        Answer partialUpdatedAnswer = new Answer();
        partialUpdatedAnswer.setId(answer.getId());

        partialUpdatedAnswer
            .number(UPDATED_NUMBER)
            .booleanAnswer(UPDATED_BOOLEAN_ANSWER)
            .multipleChoice(UPDATED_MULTIPLE_CHOICE)
            .dropDown(UPDATED_DROP_DOWN)
            .date(UPDATED_DATE)
            .time(UPDATED_TIME)
            .submitedOn(UPDATED_SUBMITED_ON)
            .dataType(UPDATED_DATA_TYPE);

        restAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnswer))
            )
            .andExpect(status().isOk());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeUpdate);
        Answer testAnswer = answerList.get(answerList.size() - 1);
        assertThat(testAnswer.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testAnswer.getBooleanAnswer()).isEqualTo(UPDATED_BOOLEAN_ANSWER);
        assertThat(testAnswer.getShortAnswer()).isEqualTo(DEFAULT_SHORT_ANSWER);
        assertThat(testAnswer.getParagraph()).isEqualTo(DEFAULT_PARAGRAPH);
        assertThat(testAnswer.getMultipleChoice()).isEqualTo(UPDATED_MULTIPLE_CHOICE);
        assertThat(testAnswer.getDropDown()).isEqualTo(UPDATED_DROP_DOWN);
        assertThat(testAnswer.getFileUploaded()).isEqualTo(DEFAULT_FILE_UPLOADED);
        assertThat(testAnswer.getFileUploadedContentType()).isEqualTo(DEFAULT_FILE_UPLOADED_CONTENT_TYPE);
        assertThat(testAnswer.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testAnswer.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAnswer.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testAnswer.getSubmitedOn()).isEqualTo(UPDATED_SUBMITED_ON);
        assertThat(testAnswer.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAnswerWithPatch() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        int databaseSizeBeforeUpdate = answerRepository.findAll().size();

        // Update the answer using partial update
        Answer partialUpdatedAnswer = new Answer();
        partialUpdatedAnswer.setId(answer.getId());

        partialUpdatedAnswer
            .number(UPDATED_NUMBER)
            .booleanAnswer(UPDATED_BOOLEAN_ANSWER)
            .shortAnswer(UPDATED_SHORT_ANSWER)
            .paragraph(UPDATED_PARAGRAPH)
            .multipleChoice(UPDATED_MULTIPLE_CHOICE)
            .dropDown(UPDATED_DROP_DOWN)
            .fileUploaded(UPDATED_FILE_UPLOADED)
            .fileUploadedContentType(UPDATED_FILE_UPLOADED_CONTENT_TYPE)
            .fileName(UPDATED_FILE_NAME)
            .date(UPDATED_DATE)
            .time(UPDATED_TIME)
            .submitedOn(UPDATED_SUBMITED_ON)
            .dataType(UPDATED_DATA_TYPE);

        restAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnswer))
            )
            .andExpect(status().isOk());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeUpdate);
        Answer testAnswer = answerList.get(answerList.size() - 1);
        assertThat(testAnswer.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testAnswer.getBooleanAnswer()).isEqualTo(UPDATED_BOOLEAN_ANSWER);
        assertThat(testAnswer.getShortAnswer()).isEqualTo(UPDATED_SHORT_ANSWER);
        assertThat(testAnswer.getParagraph()).isEqualTo(UPDATED_PARAGRAPH);
        assertThat(testAnswer.getMultipleChoice()).isEqualTo(UPDATED_MULTIPLE_CHOICE);
        assertThat(testAnswer.getDropDown()).isEqualTo(UPDATED_DROP_DOWN);
        assertThat(testAnswer.getFileUploaded()).isEqualTo(UPDATED_FILE_UPLOADED);
        assertThat(testAnswer.getFileUploadedContentType()).isEqualTo(UPDATED_FILE_UPLOADED_CONTENT_TYPE);
        assertThat(testAnswer.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testAnswer.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAnswer.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testAnswer.getSubmitedOn()).isEqualTo(UPDATED_SUBMITED_ON);
        assertThat(testAnswer.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAnswer() throws Exception {
        int databaseSizeBeforeUpdate = answerRepository.findAll().size();
        answer.setId(count.incrementAndGet());

        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, answerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(answerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnswer() throws Exception {
        int databaseSizeBeforeUpdate = answerRepository.findAll().size();
        answer.setId(count.incrementAndGet());

        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(answerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnswer() throws Exception {
        int databaseSizeBeforeUpdate = answerRepository.findAll().size();
        answer.setId(count.incrementAndGet());

        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(answerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        int databaseSizeBeforeDelete = answerRepository.findAll().size();

        // Delete the answer
        restAnswerMockMvc
            .perform(delete(ENTITY_API_URL_ID, answer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
