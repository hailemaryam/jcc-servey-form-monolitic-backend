package et.com.hmmk.rmt.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import et.com.hmmk.rmt.IntegrationTest;
import et.com.hmmk.rmt.domain.Answer;
import et.com.hmmk.rmt.domain.MultipleChoiceAnsewer;
import et.com.hmmk.rmt.repository.MultipleChoiceAnsewerRepository;
import et.com.hmmk.rmt.service.criteria.MultipleChoiceAnsewerCriteria;
import et.com.hmmk.rmt.service.dto.MultipleChoiceAnsewerDTO;
import et.com.hmmk.rmt.service.mapper.MultipleChoiceAnsewerMapper;
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
 * Integration tests for the {@link MultipleChoiceAnsewerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MultipleChoiceAnsewerResourceIT {

    private static final String DEFAULT_CHOICE = "AAAAAAAAAA";
    private static final String UPDATED_CHOICE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/multiple-choice-ansewers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MultipleChoiceAnsewerRepository multipleChoiceAnsewerRepository;

    @Autowired
    private MultipleChoiceAnsewerMapper multipleChoiceAnsewerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMultipleChoiceAnsewerMockMvc;

    private MultipleChoiceAnsewer multipleChoiceAnsewer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MultipleChoiceAnsewer createEntity(EntityManager em) {
        MultipleChoiceAnsewer multipleChoiceAnsewer = new MultipleChoiceAnsewer().choice(DEFAULT_CHOICE);
        return multipleChoiceAnsewer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MultipleChoiceAnsewer createUpdatedEntity(EntityManager em) {
        MultipleChoiceAnsewer multipleChoiceAnsewer = new MultipleChoiceAnsewer().choice(UPDATED_CHOICE);
        return multipleChoiceAnsewer;
    }

    @BeforeEach
    public void initTest() {
        multipleChoiceAnsewer = createEntity(em);
    }

    @Test
    @Transactional
    void createMultipleChoiceAnsewer() throws Exception {
        int databaseSizeBeforeCreate = multipleChoiceAnsewerRepository.findAll().size();
        // Create the MultipleChoiceAnsewer
        MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO = multipleChoiceAnsewerMapper.toDto(multipleChoiceAnsewer);
        restMultipleChoiceAnsewerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(multipleChoiceAnsewerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MultipleChoiceAnsewer in the database
        List<MultipleChoiceAnsewer> multipleChoiceAnsewerList = multipleChoiceAnsewerRepository.findAll();
        assertThat(multipleChoiceAnsewerList).hasSize(databaseSizeBeforeCreate + 1);
        MultipleChoiceAnsewer testMultipleChoiceAnsewer = multipleChoiceAnsewerList.get(multipleChoiceAnsewerList.size() - 1);
        assertThat(testMultipleChoiceAnsewer.getChoice()).isEqualTo(DEFAULT_CHOICE);
    }

    @Test
    @Transactional
    void createMultipleChoiceAnsewerWithExistingId() throws Exception {
        // Create the MultipleChoiceAnsewer with an existing ID
        multipleChoiceAnsewer.setId(1L);
        MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO = multipleChoiceAnsewerMapper.toDto(multipleChoiceAnsewer);

        int databaseSizeBeforeCreate = multipleChoiceAnsewerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMultipleChoiceAnsewerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(multipleChoiceAnsewerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MultipleChoiceAnsewer in the database
        List<MultipleChoiceAnsewer> multipleChoiceAnsewerList = multipleChoiceAnsewerRepository.findAll();
        assertThat(multipleChoiceAnsewerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMultipleChoiceAnsewers() throws Exception {
        // Initialize the database
        multipleChoiceAnsewerRepository.saveAndFlush(multipleChoiceAnsewer);

        // Get all the multipleChoiceAnsewerList
        restMultipleChoiceAnsewerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(multipleChoiceAnsewer.getId().intValue())))
            .andExpect(jsonPath("$.[*].choice").value(hasItem(DEFAULT_CHOICE)));
    }

    @Test
    @Transactional
    void getMultipleChoiceAnsewer() throws Exception {
        // Initialize the database
        multipleChoiceAnsewerRepository.saveAndFlush(multipleChoiceAnsewer);

        // Get the multipleChoiceAnsewer
        restMultipleChoiceAnsewerMockMvc
            .perform(get(ENTITY_API_URL_ID, multipleChoiceAnsewer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(multipleChoiceAnsewer.getId().intValue()))
            .andExpect(jsonPath("$.choice").value(DEFAULT_CHOICE));
    }

    @Test
    @Transactional
    void getMultipleChoiceAnsewersByIdFiltering() throws Exception {
        // Initialize the database
        multipleChoiceAnsewerRepository.saveAndFlush(multipleChoiceAnsewer);

        Long id = multipleChoiceAnsewer.getId();

        defaultMultipleChoiceAnsewerShouldBeFound("id.equals=" + id);
        defaultMultipleChoiceAnsewerShouldNotBeFound("id.notEquals=" + id);

        defaultMultipleChoiceAnsewerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMultipleChoiceAnsewerShouldNotBeFound("id.greaterThan=" + id);

        defaultMultipleChoiceAnsewerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMultipleChoiceAnsewerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMultipleChoiceAnsewersByChoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        multipleChoiceAnsewerRepository.saveAndFlush(multipleChoiceAnsewer);

        // Get all the multipleChoiceAnsewerList where choice equals to DEFAULT_CHOICE
        defaultMultipleChoiceAnsewerShouldBeFound("choice.equals=" + DEFAULT_CHOICE);

        // Get all the multipleChoiceAnsewerList where choice equals to UPDATED_CHOICE
        defaultMultipleChoiceAnsewerShouldNotBeFound("choice.equals=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    void getAllMultipleChoiceAnsewersByChoiceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        multipleChoiceAnsewerRepository.saveAndFlush(multipleChoiceAnsewer);

        // Get all the multipleChoiceAnsewerList where choice not equals to DEFAULT_CHOICE
        defaultMultipleChoiceAnsewerShouldNotBeFound("choice.notEquals=" + DEFAULT_CHOICE);

        // Get all the multipleChoiceAnsewerList where choice not equals to UPDATED_CHOICE
        defaultMultipleChoiceAnsewerShouldBeFound("choice.notEquals=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    void getAllMultipleChoiceAnsewersByChoiceIsInShouldWork() throws Exception {
        // Initialize the database
        multipleChoiceAnsewerRepository.saveAndFlush(multipleChoiceAnsewer);

        // Get all the multipleChoiceAnsewerList where choice in DEFAULT_CHOICE or UPDATED_CHOICE
        defaultMultipleChoiceAnsewerShouldBeFound("choice.in=" + DEFAULT_CHOICE + "," + UPDATED_CHOICE);

        // Get all the multipleChoiceAnsewerList where choice equals to UPDATED_CHOICE
        defaultMultipleChoiceAnsewerShouldNotBeFound("choice.in=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    void getAllMultipleChoiceAnsewersByChoiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        multipleChoiceAnsewerRepository.saveAndFlush(multipleChoiceAnsewer);

        // Get all the multipleChoiceAnsewerList where choice is not null
        defaultMultipleChoiceAnsewerShouldBeFound("choice.specified=true");

        // Get all the multipleChoiceAnsewerList where choice is null
        defaultMultipleChoiceAnsewerShouldNotBeFound("choice.specified=false");
    }

    @Test
    @Transactional
    void getAllMultipleChoiceAnsewersByChoiceContainsSomething() throws Exception {
        // Initialize the database
        multipleChoiceAnsewerRepository.saveAndFlush(multipleChoiceAnsewer);

        // Get all the multipleChoiceAnsewerList where choice contains DEFAULT_CHOICE
        defaultMultipleChoiceAnsewerShouldBeFound("choice.contains=" + DEFAULT_CHOICE);

        // Get all the multipleChoiceAnsewerList where choice contains UPDATED_CHOICE
        defaultMultipleChoiceAnsewerShouldNotBeFound("choice.contains=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    void getAllMultipleChoiceAnsewersByChoiceNotContainsSomething() throws Exception {
        // Initialize the database
        multipleChoiceAnsewerRepository.saveAndFlush(multipleChoiceAnsewer);

        // Get all the multipleChoiceAnsewerList where choice does not contain DEFAULT_CHOICE
        defaultMultipleChoiceAnsewerShouldNotBeFound("choice.doesNotContain=" + DEFAULT_CHOICE);

        // Get all the multipleChoiceAnsewerList where choice does not contain UPDATED_CHOICE
        defaultMultipleChoiceAnsewerShouldBeFound("choice.doesNotContain=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    void getAllMultipleChoiceAnsewersByAnswerIsEqualToSomething() throws Exception {
        // Initialize the database
        multipleChoiceAnsewerRepository.saveAndFlush(multipleChoiceAnsewer);
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
        multipleChoiceAnsewer.setAnswer(answer);
        multipleChoiceAnsewerRepository.saveAndFlush(multipleChoiceAnsewer);
        Long answerId = answer.getId();

        // Get all the multipleChoiceAnsewerList where answer equals to answerId
        defaultMultipleChoiceAnsewerShouldBeFound("answerId.equals=" + answerId);

        // Get all the multipleChoiceAnsewerList where answer equals to (answerId + 1)
        defaultMultipleChoiceAnsewerShouldNotBeFound("answerId.equals=" + (answerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMultipleChoiceAnsewerShouldBeFound(String filter) throws Exception {
        restMultipleChoiceAnsewerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(multipleChoiceAnsewer.getId().intValue())))
            .andExpect(jsonPath("$.[*].choice").value(hasItem(DEFAULT_CHOICE)));

        // Check, that the count call also returns 1
        restMultipleChoiceAnsewerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMultipleChoiceAnsewerShouldNotBeFound(String filter) throws Exception {
        restMultipleChoiceAnsewerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMultipleChoiceAnsewerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMultipleChoiceAnsewer() throws Exception {
        // Get the multipleChoiceAnsewer
        restMultipleChoiceAnsewerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMultipleChoiceAnsewer() throws Exception {
        // Initialize the database
        multipleChoiceAnsewerRepository.saveAndFlush(multipleChoiceAnsewer);

        int databaseSizeBeforeUpdate = multipleChoiceAnsewerRepository.findAll().size();

        // Update the multipleChoiceAnsewer
        MultipleChoiceAnsewer updatedMultipleChoiceAnsewer = multipleChoiceAnsewerRepository.findById(multipleChoiceAnsewer.getId()).get();
        // Disconnect from session so that the updates on updatedMultipleChoiceAnsewer are not directly saved in db
        em.detach(updatedMultipleChoiceAnsewer);
        updatedMultipleChoiceAnsewer.choice(UPDATED_CHOICE);
        MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO = multipleChoiceAnsewerMapper.toDto(updatedMultipleChoiceAnsewer);

        restMultipleChoiceAnsewerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, multipleChoiceAnsewerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(multipleChoiceAnsewerDTO))
            )
            .andExpect(status().isOk());

        // Validate the MultipleChoiceAnsewer in the database
        List<MultipleChoiceAnsewer> multipleChoiceAnsewerList = multipleChoiceAnsewerRepository.findAll();
        assertThat(multipleChoiceAnsewerList).hasSize(databaseSizeBeforeUpdate);
        MultipleChoiceAnsewer testMultipleChoiceAnsewer = multipleChoiceAnsewerList.get(multipleChoiceAnsewerList.size() - 1);
        assertThat(testMultipleChoiceAnsewer.getChoice()).isEqualTo(UPDATED_CHOICE);
    }

    @Test
    @Transactional
    void putNonExistingMultipleChoiceAnsewer() throws Exception {
        int databaseSizeBeforeUpdate = multipleChoiceAnsewerRepository.findAll().size();
        multipleChoiceAnsewer.setId(count.incrementAndGet());

        // Create the MultipleChoiceAnsewer
        MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO = multipleChoiceAnsewerMapper.toDto(multipleChoiceAnsewer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMultipleChoiceAnsewerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, multipleChoiceAnsewerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(multipleChoiceAnsewerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MultipleChoiceAnsewer in the database
        List<MultipleChoiceAnsewer> multipleChoiceAnsewerList = multipleChoiceAnsewerRepository.findAll();
        assertThat(multipleChoiceAnsewerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMultipleChoiceAnsewer() throws Exception {
        int databaseSizeBeforeUpdate = multipleChoiceAnsewerRepository.findAll().size();
        multipleChoiceAnsewer.setId(count.incrementAndGet());

        // Create the MultipleChoiceAnsewer
        MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO = multipleChoiceAnsewerMapper.toDto(multipleChoiceAnsewer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMultipleChoiceAnsewerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(multipleChoiceAnsewerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MultipleChoiceAnsewer in the database
        List<MultipleChoiceAnsewer> multipleChoiceAnsewerList = multipleChoiceAnsewerRepository.findAll();
        assertThat(multipleChoiceAnsewerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMultipleChoiceAnsewer() throws Exception {
        int databaseSizeBeforeUpdate = multipleChoiceAnsewerRepository.findAll().size();
        multipleChoiceAnsewer.setId(count.incrementAndGet());

        // Create the MultipleChoiceAnsewer
        MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO = multipleChoiceAnsewerMapper.toDto(multipleChoiceAnsewer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMultipleChoiceAnsewerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(multipleChoiceAnsewerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MultipleChoiceAnsewer in the database
        List<MultipleChoiceAnsewer> multipleChoiceAnsewerList = multipleChoiceAnsewerRepository.findAll();
        assertThat(multipleChoiceAnsewerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMultipleChoiceAnsewerWithPatch() throws Exception {
        // Initialize the database
        multipleChoiceAnsewerRepository.saveAndFlush(multipleChoiceAnsewer);

        int databaseSizeBeforeUpdate = multipleChoiceAnsewerRepository.findAll().size();

        // Update the multipleChoiceAnsewer using partial update
        MultipleChoiceAnsewer partialUpdatedMultipleChoiceAnsewer = new MultipleChoiceAnsewer();
        partialUpdatedMultipleChoiceAnsewer.setId(multipleChoiceAnsewer.getId());

        partialUpdatedMultipleChoiceAnsewer.choice(UPDATED_CHOICE);

        restMultipleChoiceAnsewerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMultipleChoiceAnsewer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMultipleChoiceAnsewer))
            )
            .andExpect(status().isOk());

        // Validate the MultipleChoiceAnsewer in the database
        List<MultipleChoiceAnsewer> multipleChoiceAnsewerList = multipleChoiceAnsewerRepository.findAll();
        assertThat(multipleChoiceAnsewerList).hasSize(databaseSizeBeforeUpdate);
        MultipleChoiceAnsewer testMultipleChoiceAnsewer = multipleChoiceAnsewerList.get(multipleChoiceAnsewerList.size() - 1);
        assertThat(testMultipleChoiceAnsewer.getChoice()).isEqualTo(UPDATED_CHOICE);
    }

    @Test
    @Transactional
    void fullUpdateMultipleChoiceAnsewerWithPatch() throws Exception {
        // Initialize the database
        multipleChoiceAnsewerRepository.saveAndFlush(multipleChoiceAnsewer);

        int databaseSizeBeforeUpdate = multipleChoiceAnsewerRepository.findAll().size();

        // Update the multipleChoiceAnsewer using partial update
        MultipleChoiceAnsewer partialUpdatedMultipleChoiceAnsewer = new MultipleChoiceAnsewer();
        partialUpdatedMultipleChoiceAnsewer.setId(multipleChoiceAnsewer.getId());

        partialUpdatedMultipleChoiceAnsewer.choice(UPDATED_CHOICE);

        restMultipleChoiceAnsewerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMultipleChoiceAnsewer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMultipleChoiceAnsewer))
            )
            .andExpect(status().isOk());

        // Validate the MultipleChoiceAnsewer in the database
        List<MultipleChoiceAnsewer> multipleChoiceAnsewerList = multipleChoiceAnsewerRepository.findAll();
        assertThat(multipleChoiceAnsewerList).hasSize(databaseSizeBeforeUpdate);
        MultipleChoiceAnsewer testMultipleChoiceAnsewer = multipleChoiceAnsewerList.get(multipleChoiceAnsewerList.size() - 1);
        assertThat(testMultipleChoiceAnsewer.getChoice()).isEqualTo(UPDATED_CHOICE);
    }

    @Test
    @Transactional
    void patchNonExistingMultipleChoiceAnsewer() throws Exception {
        int databaseSizeBeforeUpdate = multipleChoiceAnsewerRepository.findAll().size();
        multipleChoiceAnsewer.setId(count.incrementAndGet());

        // Create the MultipleChoiceAnsewer
        MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO = multipleChoiceAnsewerMapper.toDto(multipleChoiceAnsewer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMultipleChoiceAnsewerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, multipleChoiceAnsewerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(multipleChoiceAnsewerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MultipleChoiceAnsewer in the database
        List<MultipleChoiceAnsewer> multipleChoiceAnsewerList = multipleChoiceAnsewerRepository.findAll();
        assertThat(multipleChoiceAnsewerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMultipleChoiceAnsewer() throws Exception {
        int databaseSizeBeforeUpdate = multipleChoiceAnsewerRepository.findAll().size();
        multipleChoiceAnsewer.setId(count.incrementAndGet());

        // Create the MultipleChoiceAnsewer
        MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO = multipleChoiceAnsewerMapper.toDto(multipleChoiceAnsewer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMultipleChoiceAnsewerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(multipleChoiceAnsewerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MultipleChoiceAnsewer in the database
        List<MultipleChoiceAnsewer> multipleChoiceAnsewerList = multipleChoiceAnsewerRepository.findAll();
        assertThat(multipleChoiceAnsewerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMultipleChoiceAnsewer() throws Exception {
        int databaseSizeBeforeUpdate = multipleChoiceAnsewerRepository.findAll().size();
        multipleChoiceAnsewer.setId(count.incrementAndGet());

        // Create the MultipleChoiceAnsewer
        MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO = multipleChoiceAnsewerMapper.toDto(multipleChoiceAnsewer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMultipleChoiceAnsewerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(multipleChoiceAnsewerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MultipleChoiceAnsewer in the database
        List<MultipleChoiceAnsewer> multipleChoiceAnsewerList = multipleChoiceAnsewerRepository.findAll();
        assertThat(multipleChoiceAnsewerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMultipleChoiceAnsewer() throws Exception {
        // Initialize the database
        multipleChoiceAnsewerRepository.saveAndFlush(multipleChoiceAnsewer);

        int databaseSizeBeforeDelete = multipleChoiceAnsewerRepository.findAll().size();

        // Delete the multipleChoiceAnsewer
        restMultipleChoiceAnsewerMockMvc
            .perform(delete(ENTITY_API_URL_ID, multipleChoiceAnsewer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MultipleChoiceAnsewer> multipleChoiceAnsewerList = multipleChoiceAnsewerRepository.findAll();
        assertThat(multipleChoiceAnsewerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
