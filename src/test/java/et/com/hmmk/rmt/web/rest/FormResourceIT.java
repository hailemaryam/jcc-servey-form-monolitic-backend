package et.com.hmmk.rmt.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import et.com.hmmk.rmt.IntegrationTest;
import et.com.hmmk.rmt.domain.Form;
import et.com.hmmk.rmt.domain.FormProgresss;
import et.com.hmmk.rmt.domain.Question;
import et.com.hmmk.rmt.domain.User;
import et.com.hmmk.rmt.repository.FormRepository;
import et.com.hmmk.rmt.service.criteria.FormCriteria;
import et.com.hmmk.rmt.service.dto.FormDTO;
import et.com.hmmk.rmt.service.mapper.FormMapper;
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
 * Integration tests for the {@link FormResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/forms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private FormMapper formMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormMockMvc;

    private Form form;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Form createEntity(EntityManager em) {
        Form form = new Form()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedOn(DEFAULT_UPDATED_ON);
        return form;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Form createUpdatedEntity(EntityManager em) {
        Form form = new Form()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);
        return form;
    }

    @BeforeEach
    public void initTest() {
        form = createEntity(em);
    }

    @Test
    @Transactional
    void createForm() throws Exception {
        int databaseSizeBeforeCreate = formRepository.findAll().size();
        // Create the Form
        FormDTO formDTO = formMapper.toDto(form);
        restFormMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formDTO)))
            .andExpect(status().isCreated());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeCreate + 1);
        Form testForm = formList.get(formList.size() - 1);
        assertThat(testForm.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testForm.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testForm.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testForm.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void createFormWithExistingId() throws Exception {
        // Create the Form with an existing ID
        form.setId(1L);
        FormDTO formDTO = formMapper.toDto(form);

        int databaseSizeBeforeCreate = formRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllForms() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the formList
        restFormMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(form.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getForm() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get the form
        restFormMockMvc
            .perform(get(ENTITY_API_URL_ID, form.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(form.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getFormsByIdFiltering() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        Long id = form.getId();

        defaultFormShouldBeFound("id.equals=" + id);
        defaultFormShouldNotBeFound("id.notEquals=" + id);

        defaultFormShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFormShouldNotBeFound("id.greaterThan=" + id);

        defaultFormShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFormShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFormsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the formList where name equals to DEFAULT_NAME
        defaultFormShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the formList where name equals to UPDATED_NAME
        defaultFormShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFormsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the formList where name not equals to DEFAULT_NAME
        defaultFormShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the formList where name not equals to UPDATED_NAME
        defaultFormShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFormsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the formList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFormShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the formList where name equals to UPDATED_NAME
        defaultFormShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFormsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the formList where name is not null
        defaultFormShouldBeFound("name.specified=true");

        // Get all the formList where name is null
        defaultFormShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllFormsByNameContainsSomething() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the formList where name contains DEFAULT_NAME
        defaultFormShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the formList where name contains UPDATED_NAME
        defaultFormShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFormsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the formList where name does not contain DEFAULT_NAME
        defaultFormShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the formList where name does not contain UPDATED_NAME
        defaultFormShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFormsByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the formList where createdOn equals to DEFAULT_CREATED_ON
        defaultFormShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the formList where createdOn equals to UPDATED_CREATED_ON
        defaultFormShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void getAllFormsByCreatedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the formList where createdOn not equals to DEFAULT_CREATED_ON
        defaultFormShouldNotBeFound("createdOn.notEquals=" + DEFAULT_CREATED_ON);

        // Get all the formList where createdOn not equals to UPDATED_CREATED_ON
        defaultFormShouldBeFound("createdOn.notEquals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void getAllFormsByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the formList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultFormShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the formList where createdOn equals to UPDATED_CREATED_ON
        defaultFormShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void getAllFormsByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the formList where createdOn is not null
        defaultFormShouldBeFound("createdOn.specified=true");

        // Get all the formList where createdOn is null
        defaultFormShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    void getAllFormsByUpdatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the formList where updatedOn equals to DEFAULT_UPDATED_ON
        defaultFormShouldBeFound("updatedOn.equals=" + DEFAULT_UPDATED_ON);

        // Get all the formList where updatedOn equals to UPDATED_UPDATED_ON
        defaultFormShouldNotBeFound("updatedOn.equals=" + UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void getAllFormsByUpdatedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the formList where updatedOn not equals to DEFAULT_UPDATED_ON
        defaultFormShouldNotBeFound("updatedOn.notEquals=" + DEFAULT_UPDATED_ON);

        // Get all the formList where updatedOn not equals to UPDATED_UPDATED_ON
        defaultFormShouldBeFound("updatedOn.notEquals=" + UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void getAllFormsByUpdatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the formList where updatedOn in DEFAULT_UPDATED_ON or UPDATED_UPDATED_ON
        defaultFormShouldBeFound("updatedOn.in=" + DEFAULT_UPDATED_ON + "," + UPDATED_UPDATED_ON);

        // Get all the formList where updatedOn equals to UPDATED_UPDATED_ON
        defaultFormShouldNotBeFound("updatedOn.in=" + UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void getAllFormsByUpdatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the formList where updatedOn is not null
        defaultFormShouldBeFound("updatedOn.specified=true");

        // Get all the formList where updatedOn is null
        defaultFormShouldNotBeFound("updatedOn.specified=false");
    }

    @Test
    @Transactional
    void getAllFormsByFormProgresssIsEqualToSomething() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);
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
        form.addFormProgresss(formProgresss);
        formRepository.saveAndFlush(form);
        Long formProgresssId = formProgresss.getId();

        // Get all the formList where formProgresss equals to formProgresssId
        defaultFormShouldBeFound("formProgresssId.equals=" + formProgresssId);

        // Get all the formList where formProgresss equals to (formProgresssId + 1)
        defaultFormShouldNotBeFound("formProgresssId.equals=" + (formProgresssId + 1));
    }

    @Test
    @Transactional
    void getAllFormsByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);
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
        form.addQuestion(question);
        formRepository.saveAndFlush(form);
        Long questionId = question.getId();

        // Get all the formList where question equals to questionId
        defaultFormShouldBeFound("questionId.equals=" + questionId);

        // Get all the formList where question equals to (questionId + 1)
        defaultFormShouldNotBeFound("questionId.equals=" + (questionId + 1));
    }

    @Test
    @Transactional
    void getAllFormsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);
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
        form.setUser(user);
        formRepository.saveAndFlush(form);
        Long userId = user.getId();

        // Get all the formList where user equals to userId
        defaultFormShouldBeFound("userId.equals=" + userId);

        // Get all the formList where user equals to (userId + 1)
        defaultFormShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFormShouldBeFound(String filter) throws Exception {
        restFormMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(form.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));

        // Check, that the count call also returns 1
        restFormMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFormShouldNotBeFound(String filter) throws Exception {
        restFormMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFormMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingForm() throws Exception {
        // Get the form
        restFormMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewForm() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        int databaseSizeBeforeUpdate = formRepository.findAll().size();

        // Update the form
        Form updatedForm = formRepository.findById(form.getId()).get();
        // Disconnect from session so that the updates on updatedForm are not directly saved in db
        em.detach(updatedForm);
        updatedForm.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).createdOn(UPDATED_CREATED_ON).updatedOn(UPDATED_UPDATED_ON);
        FormDTO formDTO = formMapper.toDto(updatedForm);

        restFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formDTO))
            )
            .andExpect(status().isOk());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
        Form testForm = formList.get(formList.size() - 1);
        assertThat(testForm.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testForm.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testForm.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testForm.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void putNonExistingForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // Create the Form
        FormDTO formDTO = formMapper.toDto(form);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // Create the Form
        FormDTO formDTO = formMapper.toDto(form);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // Create the Form
        FormDTO formDTO = formMapper.toDto(form);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormWithPatch() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        int databaseSizeBeforeUpdate = formRepository.findAll().size();

        // Update the form using partial update
        Form partialUpdatedForm = new Form();
        partialUpdatedForm.setId(form.getId());

        partialUpdatedForm.name(UPDATED_NAME).createdOn(UPDATED_CREATED_ON);

        restFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedForm))
            )
            .andExpect(status().isOk());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
        Form testForm = formList.get(formList.size() - 1);
        assertThat(testForm.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testForm.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testForm.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testForm.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void fullUpdateFormWithPatch() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        int databaseSizeBeforeUpdate = formRepository.findAll().size();

        // Update the form using partial update
        Form partialUpdatedForm = new Form();
        partialUpdatedForm.setId(form.getId());

        partialUpdatedForm.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).createdOn(UPDATED_CREATED_ON).updatedOn(UPDATED_UPDATED_ON);

        restFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedForm))
            )
            .andExpect(status().isOk());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
        Form testForm = formList.get(formList.size() - 1);
        assertThat(testForm.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testForm.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testForm.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testForm.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // Create the Form
        FormDTO formDTO = formMapper.toDto(form);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // Create the Form
        FormDTO formDTO = formMapper.toDto(form);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // Create the Form
        FormDTO formDTO = formMapper.toDto(form);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(formDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteForm() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        int databaseSizeBeforeDelete = formRepository.findAll().size();

        // Delete the form
        restFormMockMvc
            .perform(delete(ENTITY_API_URL_ID, form.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
