package et.com.hmmk.rmt.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import et.com.hmmk.rmt.IntegrationTest;
import et.com.hmmk.rmt.domain.Answer;
import et.com.hmmk.rmt.domain.Form;
import et.com.hmmk.rmt.domain.FormProgresss;
import et.com.hmmk.rmt.domain.User;
import et.com.hmmk.rmt.repository.FormProgresssRepository;
import et.com.hmmk.rmt.service.criteria.FormProgresssCriteria;
import et.com.hmmk.rmt.service.dto.FormProgresssDTO;
import et.com.hmmk.rmt.service.mapper.FormProgresssMapper;
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

/**
 * Integration tests for the {@link FormProgresssResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormProgresssResourceIT {

    private static final Boolean DEFAULT_SUBMITED = false;
    private static final Boolean UPDATED_SUBMITED = true;

    private static final Instant DEFAULT_STARTED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SUBMITED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBMITED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SENTED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SENTED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/form-progressses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormProgresssRepository formProgresssRepository;

    @Autowired
    private FormProgresssMapper formProgresssMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormProgresssMockMvc;

    private FormProgresss formProgresss;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormProgresss createEntity(EntityManager em) {
        FormProgresss formProgresss = new FormProgresss()
            .submited(DEFAULT_SUBMITED)
            .startedOn(DEFAULT_STARTED_ON)
            .submitedOn(DEFAULT_SUBMITED_ON)
            .sentedOn(DEFAULT_SENTED_ON);
        return formProgresss;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormProgresss createUpdatedEntity(EntityManager em) {
        FormProgresss formProgresss = new FormProgresss()
            .submited(UPDATED_SUBMITED)
            .startedOn(UPDATED_STARTED_ON)
            .submitedOn(UPDATED_SUBMITED_ON)
            .sentedOn(UPDATED_SENTED_ON);
        return formProgresss;
    }

    @BeforeEach
    public void initTest() {
        formProgresss = createEntity(em);
    }

    @Test
    @Transactional
    void createFormProgresss() throws Exception {
        int databaseSizeBeforeCreate = formProgresssRepository.findAll().size();
        // Create the FormProgresss
        FormProgresssDTO formProgresssDTO = formProgresssMapper.toDto(formProgresss);
        restFormProgresssMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formProgresssDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FormProgresss in the database
        List<FormProgresss> formProgresssList = formProgresssRepository.findAll();
        assertThat(formProgresssList).hasSize(databaseSizeBeforeCreate + 1);
        FormProgresss testFormProgresss = formProgresssList.get(formProgresssList.size() - 1);
        assertThat(testFormProgresss.getSubmited()).isEqualTo(DEFAULT_SUBMITED);
        assertThat(testFormProgresss.getStartedOn()).isEqualTo(DEFAULT_STARTED_ON);
        assertThat(testFormProgresss.getSubmitedOn()).isEqualTo(DEFAULT_SUBMITED_ON);
        assertThat(testFormProgresss.getSentedOn()).isEqualTo(DEFAULT_SENTED_ON);
    }

    @Test
    @Transactional
    void createFormProgresssWithExistingId() throws Exception {
        // Create the FormProgresss with an existing ID
        formProgresss.setId(1L);
        FormProgresssDTO formProgresssDTO = formProgresssMapper.toDto(formProgresss);

        int databaseSizeBeforeCreate = formProgresssRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormProgresssMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formProgresssDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormProgresss in the database
        List<FormProgresss> formProgresssList = formProgresssRepository.findAll();
        assertThat(formProgresssList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormProgressses() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get all the formProgresssList
        restFormProgresssMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formProgresss.getId().intValue())))
            .andExpect(jsonPath("$.[*].submited").value(hasItem(DEFAULT_SUBMITED.booleanValue())))
            .andExpect(jsonPath("$.[*].startedOn").value(hasItem(DEFAULT_STARTED_ON.toString())))
            .andExpect(jsonPath("$.[*].submitedOn").value(hasItem(DEFAULT_SUBMITED_ON.toString())))
            .andExpect(jsonPath("$.[*].sentedOn").value(hasItem(DEFAULT_SENTED_ON.toString())));
    }

    @Test
    @Transactional
    void getFormProgresss() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get the formProgresss
        restFormProgresssMockMvc
            .perform(get(ENTITY_API_URL_ID, formProgresss.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formProgresss.getId().intValue()))
            .andExpect(jsonPath("$.submited").value(DEFAULT_SUBMITED.booleanValue()))
            .andExpect(jsonPath("$.startedOn").value(DEFAULT_STARTED_ON.toString()))
            .andExpect(jsonPath("$.submitedOn").value(DEFAULT_SUBMITED_ON.toString()))
            .andExpect(jsonPath("$.sentedOn").value(DEFAULT_SENTED_ON.toString()));
    }

    @Test
    @Transactional
    void getFormProgresssesByIdFiltering() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        Long id = formProgresss.getId();

        defaultFormProgresssShouldBeFound("id.equals=" + id);
        defaultFormProgresssShouldNotBeFound("id.notEquals=" + id);

        defaultFormProgresssShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFormProgresssShouldNotBeFound("id.greaterThan=" + id);

        defaultFormProgresssShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFormProgresssShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFormProgresssesBySubmitedIsEqualToSomething() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get all the formProgresssList where submited equals to DEFAULT_SUBMITED
        defaultFormProgresssShouldBeFound("submited.equals=" + DEFAULT_SUBMITED);

        // Get all the formProgresssList where submited equals to UPDATED_SUBMITED
        defaultFormProgresssShouldNotBeFound("submited.equals=" + UPDATED_SUBMITED);
    }

    @Test
    @Transactional
    void getAllFormProgresssesBySubmitedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get all the formProgresssList where submited not equals to DEFAULT_SUBMITED
        defaultFormProgresssShouldNotBeFound("submited.notEquals=" + DEFAULT_SUBMITED);

        // Get all the formProgresssList where submited not equals to UPDATED_SUBMITED
        defaultFormProgresssShouldBeFound("submited.notEquals=" + UPDATED_SUBMITED);
    }

    @Test
    @Transactional
    void getAllFormProgresssesBySubmitedIsInShouldWork() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get all the formProgresssList where submited in DEFAULT_SUBMITED or UPDATED_SUBMITED
        defaultFormProgresssShouldBeFound("submited.in=" + DEFAULT_SUBMITED + "," + UPDATED_SUBMITED);

        // Get all the formProgresssList where submited equals to UPDATED_SUBMITED
        defaultFormProgresssShouldNotBeFound("submited.in=" + UPDATED_SUBMITED);
    }

    @Test
    @Transactional
    void getAllFormProgresssesBySubmitedIsNullOrNotNull() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get all the formProgresssList where submited is not null
        defaultFormProgresssShouldBeFound("submited.specified=true");

        // Get all the formProgresssList where submited is null
        defaultFormProgresssShouldNotBeFound("submited.specified=false");
    }

    @Test
    @Transactional
    void getAllFormProgresssesByStartedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get all the formProgresssList where startedOn equals to DEFAULT_STARTED_ON
        defaultFormProgresssShouldBeFound("startedOn.equals=" + DEFAULT_STARTED_ON);

        // Get all the formProgresssList where startedOn equals to UPDATED_STARTED_ON
        defaultFormProgresssShouldNotBeFound("startedOn.equals=" + UPDATED_STARTED_ON);
    }

    @Test
    @Transactional
    void getAllFormProgresssesByStartedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get all the formProgresssList where startedOn not equals to DEFAULT_STARTED_ON
        defaultFormProgresssShouldNotBeFound("startedOn.notEquals=" + DEFAULT_STARTED_ON);

        // Get all the formProgresssList where startedOn not equals to UPDATED_STARTED_ON
        defaultFormProgresssShouldBeFound("startedOn.notEquals=" + UPDATED_STARTED_ON);
    }

    @Test
    @Transactional
    void getAllFormProgresssesByStartedOnIsInShouldWork() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get all the formProgresssList where startedOn in DEFAULT_STARTED_ON or UPDATED_STARTED_ON
        defaultFormProgresssShouldBeFound("startedOn.in=" + DEFAULT_STARTED_ON + "," + UPDATED_STARTED_ON);

        // Get all the formProgresssList where startedOn equals to UPDATED_STARTED_ON
        defaultFormProgresssShouldNotBeFound("startedOn.in=" + UPDATED_STARTED_ON);
    }

    @Test
    @Transactional
    void getAllFormProgresssesByStartedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get all the formProgresssList where startedOn is not null
        defaultFormProgresssShouldBeFound("startedOn.specified=true");

        // Get all the formProgresssList where startedOn is null
        defaultFormProgresssShouldNotBeFound("startedOn.specified=false");
    }

    @Test
    @Transactional
    void getAllFormProgresssesBySubmitedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get all the formProgresssList where submitedOn equals to DEFAULT_SUBMITED_ON
        defaultFormProgresssShouldBeFound("submitedOn.equals=" + DEFAULT_SUBMITED_ON);

        // Get all the formProgresssList where submitedOn equals to UPDATED_SUBMITED_ON
        defaultFormProgresssShouldNotBeFound("submitedOn.equals=" + UPDATED_SUBMITED_ON);
    }

    @Test
    @Transactional
    void getAllFormProgresssesBySubmitedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get all the formProgresssList where submitedOn not equals to DEFAULT_SUBMITED_ON
        defaultFormProgresssShouldNotBeFound("submitedOn.notEquals=" + DEFAULT_SUBMITED_ON);

        // Get all the formProgresssList where submitedOn not equals to UPDATED_SUBMITED_ON
        defaultFormProgresssShouldBeFound("submitedOn.notEquals=" + UPDATED_SUBMITED_ON);
    }

    @Test
    @Transactional
    void getAllFormProgresssesBySubmitedOnIsInShouldWork() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get all the formProgresssList where submitedOn in DEFAULT_SUBMITED_ON or UPDATED_SUBMITED_ON
        defaultFormProgresssShouldBeFound("submitedOn.in=" + DEFAULT_SUBMITED_ON + "," + UPDATED_SUBMITED_ON);

        // Get all the formProgresssList where submitedOn equals to UPDATED_SUBMITED_ON
        defaultFormProgresssShouldNotBeFound("submitedOn.in=" + UPDATED_SUBMITED_ON);
    }

    @Test
    @Transactional
    void getAllFormProgresssesBySubmitedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get all the formProgresssList where submitedOn is not null
        defaultFormProgresssShouldBeFound("submitedOn.specified=true");

        // Get all the formProgresssList where submitedOn is null
        defaultFormProgresssShouldNotBeFound("submitedOn.specified=false");
    }

    @Test
    @Transactional
    void getAllFormProgresssesBySentedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get all the formProgresssList where sentedOn equals to DEFAULT_SENTED_ON
        defaultFormProgresssShouldBeFound("sentedOn.equals=" + DEFAULT_SENTED_ON);

        // Get all the formProgresssList where sentedOn equals to UPDATED_SENTED_ON
        defaultFormProgresssShouldNotBeFound("sentedOn.equals=" + UPDATED_SENTED_ON);
    }

    @Test
    @Transactional
    void getAllFormProgresssesBySentedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get all the formProgresssList where sentedOn not equals to DEFAULT_SENTED_ON
        defaultFormProgresssShouldNotBeFound("sentedOn.notEquals=" + DEFAULT_SENTED_ON);

        // Get all the formProgresssList where sentedOn not equals to UPDATED_SENTED_ON
        defaultFormProgresssShouldBeFound("sentedOn.notEquals=" + UPDATED_SENTED_ON);
    }

    @Test
    @Transactional
    void getAllFormProgresssesBySentedOnIsInShouldWork() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get all the formProgresssList where sentedOn in DEFAULT_SENTED_ON or UPDATED_SENTED_ON
        defaultFormProgresssShouldBeFound("sentedOn.in=" + DEFAULT_SENTED_ON + "," + UPDATED_SENTED_ON);

        // Get all the formProgresssList where sentedOn equals to UPDATED_SENTED_ON
        defaultFormProgresssShouldNotBeFound("sentedOn.in=" + UPDATED_SENTED_ON);
    }

    @Test
    @Transactional
    void getAllFormProgresssesBySentedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        // Get all the formProgresssList where sentedOn is not null
        defaultFormProgresssShouldBeFound("sentedOn.specified=true");

        // Get all the formProgresssList where sentedOn is null
        defaultFormProgresssShouldNotBeFound("sentedOn.specified=false");
    }

    @Test
    @Transactional
    void getAllFormProgresssesByAnswerIsEqualToSomething() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);
        Answer answer;
        if (TestUtil.findAll(em, Answer.class).isEmpty()) {
            answer = AnswerResourceIT.createEntity(em);
            em.persist(answer);
            em.flush();
        } else {
            answer = TestUtil.findAll(em, Answer.class).get(0);
        }
        em.persist(answer);
        em.flush();
        formProgresss.addAnswer(answer);
        formProgresssRepository.saveAndFlush(formProgresss);
        Long answerId = answer.getId();

        // Get all the formProgresssList where answer equals to answerId
        defaultFormProgresssShouldBeFound("answerId.equals=" + answerId);

        // Get all the formProgresssList where answer equals to (answerId + 1)
        defaultFormProgresssShouldNotBeFound("answerId.equals=" + (answerId + 1));
    }

    @Test
    @Transactional
    void getAllFormProgresssesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);
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
        formProgresss.setUser(user);
        formProgresssRepository.saveAndFlush(formProgresss);
        Long userId = user.getId();

        // Get all the formProgresssList where user equals to userId
        defaultFormProgresssShouldBeFound("userId.equals=" + userId);

        // Get all the formProgresssList where user equals to (userId + 1)
        defaultFormProgresssShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllFormProgresssesByFormIsEqualToSomething() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);
        Form form;
        if (TestUtil.findAll(em, Form.class).isEmpty()) {
            form = FormResourceIT.createEntity(em);
            em.persist(form);
            em.flush();
        } else {
            form = TestUtil.findAll(em, Form.class).get(0);
        }
        em.persist(form);
        em.flush();
        formProgresss.setForm(form);
        formProgresssRepository.saveAndFlush(formProgresss);
        Long formId = form.getId();

        // Get all the formProgresssList where form equals to formId
        defaultFormProgresssShouldBeFound("formId.equals=" + formId);

        // Get all the formProgresssList where form equals to (formId + 1)
        defaultFormProgresssShouldNotBeFound("formId.equals=" + (formId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFormProgresssShouldBeFound(String filter) throws Exception {
        restFormProgresssMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formProgresss.getId().intValue())))
            .andExpect(jsonPath("$.[*].submited").value(hasItem(DEFAULT_SUBMITED.booleanValue())))
            .andExpect(jsonPath("$.[*].startedOn").value(hasItem(DEFAULT_STARTED_ON.toString())))
            .andExpect(jsonPath("$.[*].submitedOn").value(hasItem(DEFAULT_SUBMITED_ON.toString())))
            .andExpect(jsonPath("$.[*].sentedOn").value(hasItem(DEFAULT_SENTED_ON.toString())));

        // Check, that the count call also returns 1
        restFormProgresssMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFormProgresssShouldNotBeFound(String filter) throws Exception {
        restFormProgresssMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFormProgresssMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFormProgresss() throws Exception {
        // Get the formProgresss
        restFormProgresssMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFormProgresss() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        int databaseSizeBeforeUpdate = formProgresssRepository.findAll().size();

        // Update the formProgresss
        FormProgresss updatedFormProgresss = formProgresssRepository.findById(formProgresss.getId()).get();
        // Disconnect from session so that the updates on updatedFormProgresss are not directly saved in db
        em.detach(updatedFormProgresss);
        updatedFormProgresss
            .submited(UPDATED_SUBMITED)
            .startedOn(UPDATED_STARTED_ON)
            .submitedOn(UPDATED_SUBMITED_ON)
            .sentedOn(UPDATED_SENTED_ON);
        FormProgresssDTO formProgresssDTO = formProgresssMapper.toDto(updatedFormProgresss);

        restFormProgresssMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formProgresssDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formProgresssDTO))
            )
            .andExpect(status().isOk());

        // Validate the FormProgresss in the database
        List<FormProgresss> formProgresssList = formProgresssRepository.findAll();
        assertThat(formProgresssList).hasSize(databaseSizeBeforeUpdate);
        FormProgresss testFormProgresss = formProgresssList.get(formProgresssList.size() - 1);
        assertThat(testFormProgresss.getSubmited()).isEqualTo(UPDATED_SUBMITED);
        assertThat(testFormProgresss.getStartedOn()).isEqualTo(UPDATED_STARTED_ON);
        assertThat(testFormProgresss.getSubmitedOn()).isEqualTo(UPDATED_SUBMITED_ON);
        assertThat(testFormProgresss.getSentedOn()).isEqualTo(UPDATED_SENTED_ON);
    }

    @Test
    @Transactional
    void putNonExistingFormProgresss() throws Exception {
        int databaseSizeBeforeUpdate = formProgresssRepository.findAll().size();
        formProgresss.setId(count.incrementAndGet());

        // Create the FormProgresss
        FormProgresssDTO formProgresssDTO = formProgresssMapper.toDto(formProgresss);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormProgresssMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formProgresssDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formProgresssDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormProgresss in the database
        List<FormProgresss> formProgresssList = formProgresssRepository.findAll();
        assertThat(formProgresssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormProgresss() throws Exception {
        int databaseSizeBeforeUpdate = formProgresssRepository.findAll().size();
        formProgresss.setId(count.incrementAndGet());

        // Create the FormProgresss
        FormProgresssDTO formProgresssDTO = formProgresssMapper.toDto(formProgresss);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormProgresssMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formProgresssDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormProgresss in the database
        List<FormProgresss> formProgresssList = formProgresssRepository.findAll();
        assertThat(formProgresssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormProgresss() throws Exception {
        int databaseSizeBeforeUpdate = formProgresssRepository.findAll().size();
        formProgresss.setId(count.incrementAndGet());

        // Create the FormProgresss
        FormProgresssDTO formProgresssDTO = formProgresssMapper.toDto(formProgresss);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormProgresssMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formProgresssDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormProgresss in the database
        List<FormProgresss> formProgresssList = formProgresssRepository.findAll();
        assertThat(formProgresssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormProgresssWithPatch() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        int databaseSizeBeforeUpdate = formProgresssRepository.findAll().size();

        // Update the formProgresss using partial update
        FormProgresss partialUpdatedFormProgresss = new FormProgresss();
        partialUpdatedFormProgresss.setId(formProgresss.getId());

        partialUpdatedFormProgresss.submited(UPDATED_SUBMITED).startedOn(UPDATED_STARTED_ON).sentedOn(UPDATED_SENTED_ON);

        restFormProgresssMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormProgresss.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormProgresss))
            )
            .andExpect(status().isOk());

        // Validate the FormProgresss in the database
        List<FormProgresss> formProgresssList = formProgresssRepository.findAll();
        assertThat(formProgresssList).hasSize(databaseSizeBeforeUpdate);
        FormProgresss testFormProgresss = formProgresssList.get(formProgresssList.size() - 1);
        assertThat(testFormProgresss.getSubmited()).isEqualTo(UPDATED_SUBMITED);
        assertThat(testFormProgresss.getStartedOn()).isEqualTo(UPDATED_STARTED_ON);
        assertThat(testFormProgresss.getSubmitedOn()).isEqualTo(DEFAULT_SUBMITED_ON);
        assertThat(testFormProgresss.getSentedOn()).isEqualTo(UPDATED_SENTED_ON);
    }

    @Test
    @Transactional
    void fullUpdateFormProgresssWithPatch() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        int databaseSizeBeforeUpdate = formProgresssRepository.findAll().size();

        // Update the formProgresss using partial update
        FormProgresss partialUpdatedFormProgresss = new FormProgresss();
        partialUpdatedFormProgresss.setId(formProgresss.getId());

        partialUpdatedFormProgresss
            .submited(UPDATED_SUBMITED)
            .startedOn(UPDATED_STARTED_ON)
            .submitedOn(UPDATED_SUBMITED_ON)
            .sentedOn(UPDATED_SENTED_ON);

        restFormProgresssMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormProgresss.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormProgresss))
            )
            .andExpect(status().isOk());

        // Validate the FormProgresss in the database
        List<FormProgresss> formProgresssList = formProgresssRepository.findAll();
        assertThat(formProgresssList).hasSize(databaseSizeBeforeUpdate);
        FormProgresss testFormProgresss = formProgresssList.get(formProgresssList.size() - 1);
        assertThat(testFormProgresss.getSubmited()).isEqualTo(UPDATED_SUBMITED);
        assertThat(testFormProgresss.getStartedOn()).isEqualTo(UPDATED_STARTED_ON);
        assertThat(testFormProgresss.getSubmitedOn()).isEqualTo(UPDATED_SUBMITED_ON);
        assertThat(testFormProgresss.getSentedOn()).isEqualTo(UPDATED_SENTED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingFormProgresss() throws Exception {
        int databaseSizeBeforeUpdate = formProgresssRepository.findAll().size();
        formProgresss.setId(count.incrementAndGet());

        // Create the FormProgresss
        FormProgresssDTO formProgresssDTO = formProgresssMapper.toDto(formProgresss);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormProgresssMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formProgresssDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formProgresssDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormProgresss in the database
        List<FormProgresss> formProgresssList = formProgresssRepository.findAll();
        assertThat(formProgresssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormProgresss() throws Exception {
        int databaseSizeBeforeUpdate = formProgresssRepository.findAll().size();
        formProgresss.setId(count.incrementAndGet());

        // Create the FormProgresss
        FormProgresssDTO formProgresssDTO = formProgresssMapper.toDto(formProgresss);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormProgresssMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formProgresssDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormProgresss in the database
        List<FormProgresss> formProgresssList = formProgresssRepository.findAll();
        assertThat(formProgresssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormProgresss() throws Exception {
        int databaseSizeBeforeUpdate = formProgresssRepository.findAll().size();
        formProgresss.setId(count.incrementAndGet());

        // Create the FormProgresss
        FormProgresssDTO formProgresssDTO = formProgresssMapper.toDto(formProgresss);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormProgresssMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formProgresssDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormProgresss in the database
        List<FormProgresss> formProgresssList = formProgresssRepository.findAll();
        assertThat(formProgresssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormProgresss() throws Exception {
        // Initialize the database
        formProgresssRepository.saveAndFlush(formProgresss);

        int databaseSizeBeforeDelete = formProgresssRepository.findAll().size();

        // Delete the formProgresss
        restFormProgresssMockMvc
            .perform(delete(ENTITY_API_URL_ID, formProgresss.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormProgresss> formProgresssList = formProgresssRepository.findAll();
        assertThat(formProgresssList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
