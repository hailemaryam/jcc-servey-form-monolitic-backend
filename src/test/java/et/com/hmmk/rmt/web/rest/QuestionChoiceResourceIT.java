package et.com.hmmk.rmt.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import et.com.hmmk.rmt.IntegrationTest;
import et.com.hmmk.rmt.domain.Question;
import et.com.hmmk.rmt.domain.QuestionChoice;
import et.com.hmmk.rmt.repository.QuestionChoiceRepository;
import et.com.hmmk.rmt.service.criteria.QuestionChoiceCriteria;
import et.com.hmmk.rmt.service.dto.QuestionChoiceDTO;
import et.com.hmmk.rmt.service.mapper.QuestionChoiceMapper;
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
 * Integration tests for the {@link QuestionChoiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionChoiceResourceIT {

    private static final String DEFAULT_OPTION = "AAAAAAAAAA";
    private static final String UPDATED_OPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/question-choices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionChoiceRepository questionChoiceRepository;

    @Autowired
    private QuestionChoiceMapper questionChoiceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionChoiceMockMvc;

    private QuestionChoice questionChoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionChoice createEntity(EntityManager em) {
        QuestionChoice questionChoice = new QuestionChoice().option(DEFAULT_OPTION);
        return questionChoice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionChoice createUpdatedEntity(EntityManager em) {
        QuestionChoice questionChoice = new QuestionChoice().option(UPDATED_OPTION);
        return questionChoice;
    }

    @BeforeEach
    public void initTest() {
        questionChoice = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestionChoice() throws Exception {
        int databaseSizeBeforeCreate = questionChoiceRepository.findAll().size();
        // Create the QuestionChoice
        QuestionChoiceDTO questionChoiceDTO = questionChoiceMapper.toDto(questionChoice);
        restQuestionChoiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionChoiceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the QuestionChoice in the database
        List<QuestionChoice> questionChoiceList = questionChoiceRepository.findAll();
        assertThat(questionChoiceList).hasSize(databaseSizeBeforeCreate + 1);
        QuestionChoice testQuestionChoice = questionChoiceList.get(questionChoiceList.size() - 1);
        assertThat(testQuestionChoice.getOption()).isEqualTo(DEFAULT_OPTION);
    }

    @Test
    @Transactional
    void createQuestionChoiceWithExistingId() throws Exception {
        // Create the QuestionChoice with an existing ID
        questionChoice.setId(1L);
        QuestionChoiceDTO questionChoiceDTO = questionChoiceMapper.toDto(questionChoice);

        int databaseSizeBeforeCreate = questionChoiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionChoiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionChoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionChoice in the database
        List<QuestionChoice> questionChoiceList = questionChoiceRepository.findAll();
        assertThat(questionChoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionChoices() throws Exception {
        // Initialize the database
        questionChoiceRepository.saveAndFlush(questionChoice);

        // Get all the questionChoiceList
        restQuestionChoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionChoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].option").value(hasItem(DEFAULT_OPTION)));
    }

    @Test
    @Transactional
    void getQuestionChoice() throws Exception {
        // Initialize the database
        questionChoiceRepository.saveAndFlush(questionChoice);

        // Get the questionChoice
        restQuestionChoiceMockMvc
            .perform(get(ENTITY_API_URL_ID, questionChoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionChoice.getId().intValue()))
            .andExpect(jsonPath("$.option").value(DEFAULT_OPTION));
    }

    @Test
    @Transactional
    void getQuestionChoicesByIdFiltering() throws Exception {
        // Initialize the database
        questionChoiceRepository.saveAndFlush(questionChoice);

        Long id = questionChoice.getId();

        defaultQuestionChoiceShouldBeFound("id.equals=" + id);
        defaultQuestionChoiceShouldNotBeFound("id.notEquals=" + id);

        defaultQuestionChoiceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQuestionChoiceShouldNotBeFound("id.greaterThan=" + id);

        defaultQuestionChoiceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQuestionChoiceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuestionChoicesByOptionIsEqualToSomething() throws Exception {
        // Initialize the database
        questionChoiceRepository.saveAndFlush(questionChoice);

        // Get all the questionChoiceList where option equals to DEFAULT_OPTION
        defaultQuestionChoiceShouldBeFound("option.equals=" + DEFAULT_OPTION);

        // Get all the questionChoiceList where option equals to UPDATED_OPTION
        defaultQuestionChoiceShouldNotBeFound("option.equals=" + UPDATED_OPTION);
    }

    @Test
    @Transactional
    void getAllQuestionChoicesByOptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionChoiceRepository.saveAndFlush(questionChoice);

        // Get all the questionChoiceList where option not equals to DEFAULT_OPTION
        defaultQuestionChoiceShouldNotBeFound("option.notEquals=" + DEFAULT_OPTION);

        // Get all the questionChoiceList where option not equals to UPDATED_OPTION
        defaultQuestionChoiceShouldBeFound("option.notEquals=" + UPDATED_OPTION);
    }

    @Test
    @Transactional
    void getAllQuestionChoicesByOptionIsInShouldWork() throws Exception {
        // Initialize the database
        questionChoiceRepository.saveAndFlush(questionChoice);

        // Get all the questionChoiceList where option in DEFAULT_OPTION or UPDATED_OPTION
        defaultQuestionChoiceShouldBeFound("option.in=" + DEFAULT_OPTION + "," + UPDATED_OPTION);

        // Get all the questionChoiceList where option equals to UPDATED_OPTION
        defaultQuestionChoiceShouldNotBeFound("option.in=" + UPDATED_OPTION);
    }

    @Test
    @Transactional
    void getAllQuestionChoicesByOptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionChoiceRepository.saveAndFlush(questionChoice);

        // Get all the questionChoiceList where option is not null
        defaultQuestionChoiceShouldBeFound("option.specified=true");

        // Get all the questionChoiceList where option is null
        defaultQuestionChoiceShouldNotBeFound("option.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionChoicesByOptionContainsSomething() throws Exception {
        // Initialize the database
        questionChoiceRepository.saveAndFlush(questionChoice);

        // Get all the questionChoiceList where option contains DEFAULT_OPTION
        defaultQuestionChoiceShouldBeFound("option.contains=" + DEFAULT_OPTION);

        // Get all the questionChoiceList where option contains UPDATED_OPTION
        defaultQuestionChoiceShouldNotBeFound("option.contains=" + UPDATED_OPTION);
    }

    @Test
    @Transactional
    void getAllQuestionChoicesByOptionNotContainsSomething() throws Exception {
        // Initialize the database
        questionChoiceRepository.saveAndFlush(questionChoice);

        // Get all the questionChoiceList where option does not contain DEFAULT_OPTION
        defaultQuestionChoiceShouldNotBeFound("option.doesNotContain=" + DEFAULT_OPTION);

        // Get all the questionChoiceList where option does not contain UPDATED_OPTION
        defaultQuestionChoiceShouldBeFound("option.doesNotContain=" + UPDATED_OPTION);
    }

    @Test
    @Transactional
    void getAllQuestionChoicesByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        questionChoiceRepository.saveAndFlush(questionChoice);
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
        questionChoice.setQuestion(question);
        questionChoiceRepository.saveAndFlush(questionChoice);
        Long questionId = question.getId();

        // Get all the questionChoiceList where question equals to questionId
        defaultQuestionChoiceShouldBeFound("questionId.equals=" + questionId);

        // Get all the questionChoiceList where question equals to (questionId + 1)
        defaultQuestionChoiceShouldNotBeFound("questionId.equals=" + (questionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionChoiceShouldBeFound(String filter) throws Exception {
        restQuestionChoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionChoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].option").value(hasItem(DEFAULT_OPTION)));

        // Check, that the count call also returns 1
        restQuestionChoiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionChoiceShouldNotBeFound(String filter) throws Exception {
        restQuestionChoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionChoiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuestionChoice() throws Exception {
        // Get the questionChoice
        restQuestionChoiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestionChoice() throws Exception {
        // Initialize the database
        questionChoiceRepository.saveAndFlush(questionChoice);

        int databaseSizeBeforeUpdate = questionChoiceRepository.findAll().size();

        // Update the questionChoice
        QuestionChoice updatedQuestionChoice = questionChoiceRepository.findById(questionChoice.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionChoice are not directly saved in db
        em.detach(updatedQuestionChoice);
        updatedQuestionChoice.option(UPDATED_OPTION);
        QuestionChoiceDTO questionChoiceDTO = questionChoiceMapper.toDto(updatedQuestionChoice);

        restQuestionChoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionChoiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionChoiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuestionChoice in the database
        List<QuestionChoice> questionChoiceList = questionChoiceRepository.findAll();
        assertThat(questionChoiceList).hasSize(databaseSizeBeforeUpdate);
        QuestionChoice testQuestionChoice = questionChoiceList.get(questionChoiceList.size() - 1);
        assertThat(testQuestionChoice.getOption()).isEqualTo(UPDATED_OPTION);
    }

    @Test
    @Transactional
    void putNonExistingQuestionChoice() throws Exception {
        int databaseSizeBeforeUpdate = questionChoiceRepository.findAll().size();
        questionChoice.setId(count.incrementAndGet());

        // Create the QuestionChoice
        QuestionChoiceDTO questionChoiceDTO = questionChoiceMapper.toDto(questionChoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionChoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionChoiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionChoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionChoice in the database
        List<QuestionChoice> questionChoiceList = questionChoiceRepository.findAll();
        assertThat(questionChoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionChoice() throws Exception {
        int databaseSizeBeforeUpdate = questionChoiceRepository.findAll().size();
        questionChoice.setId(count.incrementAndGet());

        // Create the QuestionChoice
        QuestionChoiceDTO questionChoiceDTO = questionChoiceMapper.toDto(questionChoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionChoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionChoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionChoice in the database
        List<QuestionChoice> questionChoiceList = questionChoiceRepository.findAll();
        assertThat(questionChoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionChoice() throws Exception {
        int databaseSizeBeforeUpdate = questionChoiceRepository.findAll().size();
        questionChoice.setId(count.incrementAndGet());

        // Create the QuestionChoice
        QuestionChoiceDTO questionChoiceDTO = questionChoiceMapper.toDto(questionChoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionChoiceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionChoiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionChoice in the database
        List<QuestionChoice> questionChoiceList = questionChoiceRepository.findAll();
        assertThat(questionChoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionChoiceWithPatch() throws Exception {
        // Initialize the database
        questionChoiceRepository.saveAndFlush(questionChoice);

        int databaseSizeBeforeUpdate = questionChoiceRepository.findAll().size();

        // Update the questionChoice using partial update
        QuestionChoice partialUpdatedQuestionChoice = new QuestionChoice();
        partialUpdatedQuestionChoice.setId(questionChoice.getId());

        partialUpdatedQuestionChoice.option(UPDATED_OPTION);

        restQuestionChoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionChoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionChoice))
            )
            .andExpect(status().isOk());

        // Validate the QuestionChoice in the database
        List<QuestionChoice> questionChoiceList = questionChoiceRepository.findAll();
        assertThat(questionChoiceList).hasSize(databaseSizeBeforeUpdate);
        QuestionChoice testQuestionChoice = questionChoiceList.get(questionChoiceList.size() - 1);
        assertThat(testQuestionChoice.getOption()).isEqualTo(UPDATED_OPTION);
    }

    @Test
    @Transactional
    void fullUpdateQuestionChoiceWithPatch() throws Exception {
        // Initialize the database
        questionChoiceRepository.saveAndFlush(questionChoice);

        int databaseSizeBeforeUpdate = questionChoiceRepository.findAll().size();

        // Update the questionChoice using partial update
        QuestionChoice partialUpdatedQuestionChoice = new QuestionChoice();
        partialUpdatedQuestionChoice.setId(questionChoice.getId());

        partialUpdatedQuestionChoice.option(UPDATED_OPTION);

        restQuestionChoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionChoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionChoice))
            )
            .andExpect(status().isOk());

        // Validate the QuestionChoice in the database
        List<QuestionChoice> questionChoiceList = questionChoiceRepository.findAll();
        assertThat(questionChoiceList).hasSize(databaseSizeBeforeUpdate);
        QuestionChoice testQuestionChoice = questionChoiceList.get(questionChoiceList.size() - 1);
        assertThat(testQuestionChoice.getOption()).isEqualTo(UPDATED_OPTION);
    }

    @Test
    @Transactional
    void patchNonExistingQuestionChoice() throws Exception {
        int databaseSizeBeforeUpdate = questionChoiceRepository.findAll().size();
        questionChoice.setId(count.incrementAndGet());

        // Create the QuestionChoice
        QuestionChoiceDTO questionChoiceDTO = questionChoiceMapper.toDto(questionChoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionChoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionChoiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionChoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionChoice in the database
        List<QuestionChoice> questionChoiceList = questionChoiceRepository.findAll();
        assertThat(questionChoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionChoice() throws Exception {
        int databaseSizeBeforeUpdate = questionChoiceRepository.findAll().size();
        questionChoice.setId(count.incrementAndGet());

        // Create the QuestionChoice
        QuestionChoiceDTO questionChoiceDTO = questionChoiceMapper.toDto(questionChoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionChoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionChoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionChoice in the database
        List<QuestionChoice> questionChoiceList = questionChoiceRepository.findAll();
        assertThat(questionChoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionChoice() throws Exception {
        int databaseSizeBeforeUpdate = questionChoiceRepository.findAll().size();
        questionChoice.setId(count.incrementAndGet());

        // Create the QuestionChoice
        QuestionChoiceDTO questionChoiceDTO = questionChoiceMapper.toDto(questionChoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionChoiceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionChoiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionChoice in the database
        List<QuestionChoice> questionChoiceList = questionChoiceRepository.findAll();
        assertThat(questionChoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionChoice() throws Exception {
        // Initialize the database
        questionChoiceRepository.saveAndFlush(questionChoice);

        int databaseSizeBeforeDelete = questionChoiceRepository.findAll().size();

        // Delete the questionChoice
        restQuestionChoiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionChoice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuestionChoice> questionChoiceList = questionChoiceRepository.findAll();
        assertThat(questionChoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
