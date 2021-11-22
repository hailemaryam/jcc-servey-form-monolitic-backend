package et.com.hmmk.rmt.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import et.com.hmmk.rmt.IntegrationTest;
import et.com.hmmk.rmt.domain.Company;
import et.com.hmmk.rmt.domain.TypeOfOrganization;
import et.com.hmmk.rmt.repository.TypeOfOrganizationRepository;
import et.com.hmmk.rmt.service.criteria.TypeOfOrganizationCriteria;
import et.com.hmmk.rmt.service.dto.TypeOfOrganizationDTO;
import et.com.hmmk.rmt.service.mapper.TypeOfOrganizationMapper;
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
 * Integration tests for the {@link TypeOfOrganizationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeOfOrganizationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-of-organizations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeOfOrganizationRepository typeOfOrganizationRepository;

    @Autowired
    private TypeOfOrganizationMapper typeOfOrganizationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeOfOrganizationMockMvc;

    private TypeOfOrganization typeOfOrganization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeOfOrganization createEntity(EntityManager em) {
        TypeOfOrganization typeOfOrganization = new TypeOfOrganization().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return typeOfOrganization;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeOfOrganization createUpdatedEntity(EntityManager em) {
        TypeOfOrganization typeOfOrganization = new TypeOfOrganization().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return typeOfOrganization;
    }

    @BeforeEach
    public void initTest() {
        typeOfOrganization = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeOfOrganization() throws Exception {
        int databaseSizeBeforeCreate = typeOfOrganizationRepository.findAll().size();
        // Create the TypeOfOrganization
        TypeOfOrganizationDTO typeOfOrganizationDTO = typeOfOrganizationMapper.toDto(typeOfOrganization);
        restTypeOfOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOfOrganizationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TypeOfOrganization in the database
        List<TypeOfOrganization> typeOfOrganizationList = typeOfOrganizationRepository.findAll();
        assertThat(typeOfOrganizationList).hasSize(databaseSizeBeforeCreate + 1);
        TypeOfOrganization testTypeOfOrganization = typeOfOrganizationList.get(typeOfOrganizationList.size() - 1);
        assertThat(testTypeOfOrganization.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTypeOfOrganization.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createTypeOfOrganizationWithExistingId() throws Exception {
        // Create the TypeOfOrganization with an existing ID
        typeOfOrganization.setId(1L);
        TypeOfOrganizationDTO typeOfOrganizationDTO = typeOfOrganizationMapper.toDto(typeOfOrganization);

        int databaseSizeBeforeCreate = typeOfOrganizationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeOfOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOfOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfOrganization in the database
        List<TypeOfOrganization> typeOfOrganizationList = typeOfOrganizationRepository.findAll();
        assertThat(typeOfOrganizationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypeOfOrganizations() throws Exception {
        // Initialize the database
        typeOfOrganizationRepository.saveAndFlush(typeOfOrganization);

        // Get all the typeOfOrganizationList
        restTypeOfOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeOfOrganization.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getTypeOfOrganization() throws Exception {
        // Initialize the database
        typeOfOrganizationRepository.saveAndFlush(typeOfOrganization);

        // Get the typeOfOrganization
        restTypeOfOrganizationMockMvc
            .perform(get(ENTITY_API_URL_ID, typeOfOrganization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeOfOrganization.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getTypeOfOrganizationsByIdFiltering() throws Exception {
        // Initialize the database
        typeOfOrganizationRepository.saveAndFlush(typeOfOrganization);

        Long id = typeOfOrganization.getId();

        defaultTypeOfOrganizationShouldBeFound("id.equals=" + id);
        defaultTypeOfOrganizationShouldNotBeFound("id.notEquals=" + id);

        defaultTypeOfOrganizationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTypeOfOrganizationShouldNotBeFound("id.greaterThan=" + id);

        defaultTypeOfOrganizationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTypeOfOrganizationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTypeOfOrganizationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        typeOfOrganizationRepository.saveAndFlush(typeOfOrganization);

        // Get all the typeOfOrganizationList where name equals to DEFAULT_NAME
        defaultTypeOfOrganizationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the typeOfOrganizationList where name equals to UPDATED_NAME
        defaultTypeOfOrganizationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTypeOfOrganizationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        typeOfOrganizationRepository.saveAndFlush(typeOfOrganization);

        // Get all the typeOfOrganizationList where name not equals to DEFAULT_NAME
        defaultTypeOfOrganizationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the typeOfOrganizationList where name not equals to UPDATED_NAME
        defaultTypeOfOrganizationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTypeOfOrganizationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        typeOfOrganizationRepository.saveAndFlush(typeOfOrganization);

        // Get all the typeOfOrganizationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTypeOfOrganizationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the typeOfOrganizationList where name equals to UPDATED_NAME
        defaultTypeOfOrganizationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTypeOfOrganizationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        typeOfOrganizationRepository.saveAndFlush(typeOfOrganization);

        // Get all the typeOfOrganizationList where name is not null
        defaultTypeOfOrganizationShouldBeFound("name.specified=true");

        // Get all the typeOfOrganizationList where name is null
        defaultTypeOfOrganizationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTypeOfOrganizationsByNameContainsSomething() throws Exception {
        // Initialize the database
        typeOfOrganizationRepository.saveAndFlush(typeOfOrganization);

        // Get all the typeOfOrganizationList where name contains DEFAULT_NAME
        defaultTypeOfOrganizationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the typeOfOrganizationList where name contains UPDATED_NAME
        defaultTypeOfOrganizationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTypeOfOrganizationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        typeOfOrganizationRepository.saveAndFlush(typeOfOrganization);

        // Get all the typeOfOrganizationList where name does not contain DEFAULT_NAME
        defaultTypeOfOrganizationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the typeOfOrganizationList where name does not contain UPDATED_NAME
        defaultTypeOfOrganizationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTypeOfOrganizationsByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        typeOfOrganizationRepository.saveAndFlush(typeOfOrganization);
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        em.persist(company);
        em.flush();
        typeOfOrganization.addCompany(company);
        typeOfOrganizationRepository.saveAndFlush(typeOfOrganization);
        Long companyId = company.getId();

        // Get all the typeOfOrganizationList where company equals to companyId
        defaultTypeOfOrganizationShouldBeFound("companyId.equals=" + companyId);

        // Get all the typeOfOrganizationList where company equals to (companyId + 1)
        defaultTypeOfOrganizationShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTypeOfOrganizationShouldBeFound(String filter) throws Exception {
        restTypeOfOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeOfOrganization.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restTypeOfOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTypeOfOrganizationShouldNotBeFound(String filter) throws Exception {
        restTypeOfOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTypeOfOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTypeOfOrganization() throws Exception {
        // Get the typeOfOrganization
        restTypeOfOrganizationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypeOfOrganization() throws Exception {
        // Initialize the database
        typeOfOrganizationRepository.saveAndFlush(typeOfOrganization);

        int databaseSizeBeforeUpdate = typeOfOrganizationRepository.findAll().size();

        // Update the typeOfOrganization
        TypeOfOrganization updatedTypeOfOrganization = typeOfOrganizationRepository.findById(typeOfOrganization.getId()).get();
        // Disconnect from session so that the updates on updatedTypeOfOrganization are not directly saved in db
        em.detach(updatedTypeOfOrganization);
        updatedTypeOfOrganization.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        TypeOfOrganizationDTO typeOfOrganizationDTO = typeOfOrganizationMapper.toDto(updatedTypeOfOrganization);

        restTypeOfOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeOfOrganizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOfOrganizationDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeOfOrganization in the database
        List<TypeOfOrganization> typeOfOrganizationList = typeOfOrganizationRepository.findAll();
        assertThat(typeOfOrganizationList).hasSize(databaseSizeBeforeUpdate);
        TypeOfOrganization testTypeOfOrganization = typeOfOrganizationList.get(typeOfOrganizationList.size() - 1);
        assertThat(testTypeOfOrganization.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTypeOfOrganization.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingTypeOfOrganization() throws Exception {
        int databaseSizeBeforeUpdate = typeOfOrganizationRepository.findAll().size();
        typeOfOrganization.setId(count.incrementAndGet());

        // Create the TypeOfOrganization
        TypeOfOrganizationDTO typeOfOrganizationDTO = typeOfOrganizationMapper.toDto(typeOfOrganization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeOfOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeOfOrganizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOfOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfOrganization in the database
        List<TypeOfOrganization> typeOfOrganizationList = typeOfOrganizationRepository.findAll();
        assertThat(typeOfOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeOfOrganization() throws Exception {
        int databaseSizeBeforeUpdate = typeOfOrganizationRepository.findAll().size();
        typeOfOrganization.setId(count.incrementAndGet());

        // Create the TypeOfOrganization
        TypeOfOrganizationDTO typeOfOrganizationDTO = typeOfOrganizationMapper.toDto(typeOfOrganization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOfOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOfOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfOrganization in the database
        List<TypeOfOrganization> typeOfOrganizationList = typeOfOrganizationRepository.findAll();
        assertThat(typeOfOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeOfOrganization() throws Exception {
        int databaseSizeBeforeUpdate = typeOfOrganizationRepository.findAll().size();
        typeOfOrganization.setId(count.incrementAndGet());

        // Create the TypeOfOrganization
        TypeOfOrganizationDTO typeOfOrganizationDTO = typeOfOrganizationMapper.toDto(typeOfOrganization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOfOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOfOrganizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeOfOrganization in the database
        List<TypeOfOrganization> typeOfOrganizationList = typeOfOrganizationRepository.findAll();
        assertThat(typeOfOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeOfOrganizationWithPatch() throws Exception {
        // Initialize the database
        typeOfOrganizationRepository.saveAndFlush(typeOfOrganization);

        int databaseSizeBeforeUpdate = typeOfOrganizationRepository.findAll().size();

        // Update the typeOfOrganization using partial update
        TypeOfOrganization partialUpdatedTypeOfOrganization = new TypeOfOrganization();
        partialUpdatedTypeOfOrganization.setId(typeOfOrganization.getId());

        partialUpdatedTypeOfOrganization.description(UPDATED_DESCRIPTION);

        restTypeOfOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeOfOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeOfOrganization))
            )
            .andExpect(status().isOk());

        // Validate the TypeOfOrganization in the database
        List<TypeOfOrganization> typeOfOrganizationList = typeOfOrganizationRepository.findAll();
        assertThat(typeOfOrganizationList).hasSize(databaseSizeBeforeUpdate);
        TypeOfOrganization testTypeOfOrganization = typeOfOrganizationList.get(typeOfOrganizationList.size() - 1);
        assertThat(testTypeOfOrganization.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTypeOfOrganization.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateTypeOfOrganizationWithPatch() throws Exception {
        // Initialize the database
        typeOfOrganizationRepository.saveAndFlush(typeOfOrganization);

        int databaseSizeBeforeUpdate = typeOfOrganizationRepository.findAll().size();

        // Update the typeOfOrganization using partial update
        TypeOfOrganization partialUpdatedTypeOfOrganization = new TypeOfOrganization();
        partialUpdatedTypeOfOrganization.setId(typeOfOrganization.getId());

        partialUpdatedTypeOfOrganization.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restTypeOfOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeOfOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeOfOrganization))
            )
            .andExpect(status().isOk());

        // Validate the TypeOfOrganization in the database
        List<TypeOfOrganization> typeOfOrganizationList = typeOfOrganizationRepository.findAll();
        assertThat(typeOfOrganizationList).hasSize(databaseSizeBeforeUpdate);
        TypeOfOrganization testTypeOfOrganization = typeOfOrganizationList.get(typeOfOrganizationList.size() - 1);
        assertThat(testTypeOfOrganization.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTypeOfOrganization.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingTypeOfOrganization() throws Exception {
        int databaseSizeBeforeUpdate = typeOfOrganizationRepository.findAll().size();
        typeOfOrganization.setId(count.incrementAndGet());

        // Create the TypeOfOrganization
        TypeOfOrganizationDTO typeOfOrganizationDTO = typeOfOrganizationMapper.toDto(typeOfOrganization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeOfOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeOfOrganizationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeOfOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfOrganization in the database
        List<TypeOfOrganization> typeOfOrganizationList = typeOfOrganizationRepository.findAll();
        assertThat(typeOfOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeOfOrganization() throws Exception {
        int databaseSizeBeforeUpdate = typeOfOrganizationRepository.findAll().size();
        typeOfOrganization.setId(count.incrementAndGet());

        // Create the TypeOfOrganization
        TypeOfOrganizationDTO typeOfOrganizationDTO = typeOfOrganizationMapper.toDto(typeOfOrganization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOfOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeOfOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfOrganization in the database
        List<TypeOfOrganization> typeOfOrganizationList = typeOfOrganizationRepository.findAll();
        assertThat(typeOfOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeOfOrganization() throws Exception {
        int databaseSizeBeforeUpdate = typeOfOrganizationRepository.findAll().size();
        typeOfOrganization.setId(count.incrementAndGet());

        // Create the TypeOfOrganization
        TypeOfOrganizationDTO typeOfOrganizationDTO = typeOfOrganizationMapper.toDto(typeOfOrganization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOfOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeOfOrganizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeOfOrganization in the database
        List<TypeOfOrganization> typeOfOrganizationList = typeOfOrganizationRepository.findAll();
        assertThat(typeOfOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeOfOrganization() throws Exception {
        // Initialize the database
        typeOfOrganizationRepository.saveAndFlush(typeOfOrganization);

        int databaseSizeBeforeDelete = typeOfOrganizationRepository.findAll().size();

        // Delete the typeOfOrganization
        restTypeOfOrganizationMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeOfOrganization.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeOfOrganization> typeOfOrganizationList = typeOfOrganizationRepository.findAll();
        assertThat(typeOfOrganizationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
