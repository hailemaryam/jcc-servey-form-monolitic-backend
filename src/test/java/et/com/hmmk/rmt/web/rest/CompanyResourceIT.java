package et.com.hmmk.rmt.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import et.com.hmmk.rmt.IntegrationTest;
import et.com.hmmk.rmt.domain.Company;
import et.com.hmmk.rmt.domain.TypeOfOrganization;
import et.com.hmmk.rmt.domain.User;
import et.com.hmmk.rmt.repository.CompanyRepository;
import et.com.hmmk.rmt.service.criteria.CompanyCriteria;
import et.com.hmmk.rmt.service.dto.CompanyDTO;
import et.com.hmmk.rmt.service.mapper.CompanyMapper;
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
 * Integration tests for the {@link CompanyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyResourceIT {

    private static final String DEFAULT_STRATEGIC_OBJECTIVE = "AAAAAAAAAA";
    private static final String UPDATED_STRATEGIC_OBJECTIVE = "BBBBBBBBBB";

    private static final String DEFAULT_FUTURE_FOCUS_AREA = "AAAAAAAAAA";
    private static final String UPDATED_FUTURE_FOCUS_AREA = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENT_FUNDING_CYCLE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENT_FUNDING_CYCLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyMockMvc;

    private Company company;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createEntity(EntityManager em) {
        Company company = new Company()
            .strategicObjective(DEFAULT_STRATEGIC_OBJECTIVE)
            .futureFocusArea(DEFAULT_FUTURE_FOCUS_AREA)
            .currentFundingCycle(DEFAULT_CURRENT_FUNDING_CYCLE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        company.setUser(user);
        return company;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createUpdatedEntity(EntityManager em) {
        Company company = new Company()
            .strategicObjective(UPDATED_STRATEGIC_OBJECTIVE)
            .futureFocusArea(UPDATED_FUTURE_FOCUS_AREA)
            .currentFundingCycle(UPDATED_CURRENT_FUNDING_CYCLE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        company.setUser(user);
        return company;
    }

    @BeforeEach
    public void initTest() {
        company = createEntity(em);
    }

    @Test
    @Transactional
    void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();
        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);
        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getStrategicObjective()).isEqualTo(DEFAULT_STRATEGIC_OBJECTIVE);
        assertThat(testCompany.getFutureFocusArea()).isEqualTo(DEFAULT_FUTURE_FOCUS_AREA);
        assertThat(testCompany.getCurrentFundingCycle()).isEqualTo(DEFAULT_CURRENT_FUNDING_CYCLE);

        // Validate the id for MapsId, the ids must be same
        assertThat(testCompany.getId()).isEqualTo(testCompany.getUser().getId());
    }

    @Test
    @Transactional
    void createCompanyWithExistingId() throws Exception {
        // Create the Company with an existing ID
        company.setId(1L);
        CompanyDTO companyDTO = companyMapper.toDto(company);

        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void updateCompanyMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Add a new parent entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();

        // Load the company
        Company updatedCompany = companyRepository.findById(company.getId()).get();
        assertThat(updatedCompany).isNotNull();
        // Disconnect from session so that the updates on updatedCompany are not directly saved in db
        em.detach(updatedCompany);

        // Update the User with new association value
        updatedCompany.setUser(user);
        CompanyDTO updatedCompanyDTO = companyMapper.toDto(updatedCompany);
        assertThat(updatedCompanyDTO).isNotNull();

        // Update the entity
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompanyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompanyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate);
        Company testCompany = companyList.get(companyList.size() - 1);
        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testCompany.getId()).isEqualTo(testCompany.getUser().getId());
    }

    @Test
    @Transactional
    void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].strategicObjective").value(hasItem(DEFAULT_STRATEGIC_OBJECTIVE)))
            .andExpect(jsonPath("$.[*].futureFocusArea").value(hasItem(DEFAULT_FUTURE_FOCUS_AREA)))
            .andExpect(jsonPath("$.[*].currentFundingCycle").value(hasItem(DEFAULT_CURRENT_FUNDING_CYCLE)));
    }

    @Test
    @Transactional
    void getCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL_ID, company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.strategicObjective").value(DEFAULT_STRATEGIC_OBJECTIVE))
            .andExpect(jsonPath("$.futureFocusArea").value(DEFAULT_FUTURE_FOCUS_AREA))
            .andExpect(jsonPath("$.currentFundingCycle").value(DEFAULT_CURRENT_FUNDING_CYCLE));
    }

    @Test
    @Transactional
    void getCompaniesByIdFiltering() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        Long id = company.getId();

        defaultCompanyShouldBeFound("id.equals=" + id);
        defaultCompanyShouldNotBeFound("id.notEquals=" + id);

        defaultCompanyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompanyShouldNotBeFound("id.greaterThan=" + id);

        defaultCompanyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompanyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCompaniesByStrategicObjectiveIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where strategicObjective equals to DEFAULT_STRATEGIC_OBJECTIVE
        defaultCompanyShouldBeFound("strategicObjective.equals=" + DEFAULT_STRATEGIC_OBJECTIVE);

        // Get all the companyList where strategicObjective equals to UPDATED_STRATEGIC_OBJECTIVE
        defaultCompanyShouldNotBeFound("strategicObjective.equals=" + UPDATED_STRATEGIC_OBJECTIVE);
    }

    @Test
    @Transactional
    void getAllCompaniesByStrategicObjectiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where strategicObjective not equals to DEFAULT_STRATEGIC_OBJECTIVE
        defaultCompanyShouldNotBeFound("strategicObjective.notEquals=" + DEFAULT_STRATEGIC_OBJECTIVE);

        // Get all the companyList where strategicObjective not equals to UPDATED_STRATEGIC_OBJECTIVE
        defaultCompanyShouldBeFound("strategicObjective.notEquals=" + UPDATED_STRATEGIC_OBJECTIVE);
    }

    @Test
    @Transactional
    void getAllCompaniesByStrategicObjectiveIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where strategicObjective in DEFAULT_STRATEGIC_OBJECTIVE or UPDATED_STRATEGIC_OBJECTIVE
        defaultCompanyShouldBeFound("strategicObjective.in=" + DEFAULT_STRATEGIC_OBJECTIVE + "," + UPDATED_STRATEGIC_OBJECTIVE);

        // Get all the companyList where strategicObjective equals to UPDATED_STRATEGIC_OBJECTIVE
        defaultCompanyShouldNotBeFound("strategicObjective.in=" + UPDATED_STRATEGIC_OBJECTIVE);
    }

    @Test
    @Transactional
    void getAllCompaniesByStrategicObjectiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where strategicObjective is not null
        defaultCompanyShouldBeFound("strategicObjective.specified=true");

        // Get all the companyList where strategicObjective is null
        defaultCompanyShouldNotBeFound("strategicObjective.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByStrategicObjectiveContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where strategicObjective contains DEFAULT_STRATEGIC_OBJECTIVE
        defaultCompanyShouldBeFound("strategicObjective.contains=" + DEFAULT_STRATEGIC_OBJECTIVE);

        // Get all the companyList where strategicObjective contains UPDATED_STRATEGIC_OBJECTIVE
        defaultCompanyShouldNotBeFound("strategicObjective.contains=" + UPDATED_STRATEGIC_OBJECTIVE);
    }

    @Test
    @Transactional
    void getAllCompaniesByStrategicObjectiveNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where strategicObjective does not contain DEFAULT_STRATEGIC_OBJECTIVE
        defaultCompanyShouldNotBeFound("strategicObjective.doesNotContain=" + DEFAULT_STRATEGIC_OBJECTIVE);

        // Get all the companyList where strategicObjective does not contain UPDATED_STRATEGIC_OBJECTIVE
        defaultCompanyShouldBeFound("strategicObjective.doesNotContain=" + UPDATED_STRATEGIC_OBJECTIVE);
    }

    @Test
    @Transactional
    void getAllCompaniesByFutureFocusAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where futureFocusArea equals to DEFAULT_FUTURE_FOCUS_AREA
        defaultCompanyShouldBeFound("futureFocusArea.equals=" + DEFAULT_FUTURE_FOCUS_AREA);

        // Get all the companyList where futureFocusArea equals to UPDATED_FUTURE_FOCUS_AREA
        defaultCompanyShouldNotBeFound("futureFocusArea.equals=" + UPDATED_FUTURE_FOCUS_AREA);
    }

    @Test
    @Transactional
    void getAllCompaniesByFutureFocusAreaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where futureFocusArea not equals to DEFAULT_FUTURE_FOCUS_AREA
        defaultCompanyShouldNotBeFound("futureFocusArea.notEquals=" + DEFAULT_FUTURE_FOCUS_AREA);

        // Get all the companyList where futureFocusArea not equals to UPDATED_FUTURE_FOCUS_AREA
        defaultCompanyShouldBeFound("futureFocusArea.notEquals=" + UPDATED_FUTURE_FOCUS_AREA);
    }

    @Test
    @Transactional
    void getAllCompaniesByFutureFocusAreaIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where futureFocusArea in DEFAULT_FUTURE_FOCUS_AREA or UPDATED_FUTURE_FOCUS_AREA
        defaultCompanyShouldBeFound("futureFocusArea.in=" + DEFAULT_FUTURE_FOCUS_AREA + "," + UPDATED_FUTURE_FOCUS_AREA);

        // Get all the companyList where futureFocusArea equals to UPDATED_FUTURE_FOCUS_AREA
        defaultCompanyShouldNotBeFound("futureFocusArea.in=" + UPDATED_FUTURE_FOCUS_AREA);
    }

    @Test
    @Transactional
    void getAllCompaniesByFutureFocusAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where futureFocusArea is not null
        defaultCompanyShouldBeFound("futureFocusArea.specified=true");

        // Get all the companyList where futureFocusArea is null
        defaultCompanyShouldNotBeFound("futureFocusArea.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByFutureFocusAreaContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where futureFocusArea contains DEFAULT_FUTURE_FOCUS_AREA
        defaultCompanyShouldBeFound("futureFocusArea.contains=" + DEFAULT_FUTURE_FOCUS_AREA);

        // Get all the companyList where futureFocusArea contains UPDATED_FUTURE_FOCUS_AREA
        defaultCompanyShouldNotBeFound("futureFocusArea.contains=" + UPDATED_FUTURE_FOCUS_AREA);
    }

    @Test
    @Transactional
    void getAllCompaniesByFutureFocusAreaNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where futureFocusArea does not contain DEFAULT_FUTURE_FOCUS_AREA
        defaultCompanyShouldNotBeFound("futureFocusArea.doesNotContain=" + DEFAULT_FUTURE_FOCUS_AREA);

        // Get all the companyList where futureFocusArea does not contain UPDATED_FUTURE_FOCUS_AREA
        defaultCompanyShouldBeFound("futureFocusArea.doesNotContain=" + UPDATED_FUTURE_FOCUS_AREA);
    }

    @Test
    @Transactional
    void getAllCompaniesByCurrentFundingCycleIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where currentFundingCycle equals to DEFAULT_CURRENT_FUNDING_CYCLE
        defaultCompanyShouldBeFound("currentFundingCycle.equals=" + DEFAULT_CURRENT_FUNDING_CYCLE);

        // Get all the companyList where currentFundingCycle equals to UPDATED_CURRENT_FUNDING_CYCLE
        defaultCompanyShouldNotBeFound("currentFundingCycle.equals=" + UPDATED_CURRENT_FUNDING_CYCLE);
    }

    @Test
    @Transactional
    void getAllCompaniesByCurrentFundingCycleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where currentFundingCycle not equals to DEFAULT_CURRENT_FUNDING_CYCLE
        defaultCompanyShouldNotBeFound("currentFundingCycle.notEquals=" + DEFAULT_CURRENT_FUNDING_CYCLE);

        // Get all the companyList where currentFundingCycle not equals to UPDATED_CURRENT_FUNDING_CYCLE
        defaultCompanyShouldBeFound("currentFundingCycle.notEquals=" + UPDATED_CURRENT_FUNDING_CYCLE);
    }

    @Test
    @Transactional
    void getAllCompaniesByCurrentFundingCycleIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where currentFundingCycle in DEFAULT_CURRENT_FUNDING_CYCLE or UPDATED_CURRENT_FUNDING_CYCLE
        defaultCompanyShouldBeFound("currentFundingCycle.in=" + DEFAULT_CURRENT_FUNDING_CYCLE + "," + UPDATED_CURRENT_FUNDING_CYCLE);

        // Get all the companyList where currentFundingCycle equals to UPDATED_CURRENT_FUNDING_CYCLE
        defaultCompanyShouldNotBeFound("currentFundingCycle.in=" + UPDATED_CURRENT_FUNDING_CYCLE);
    }

    @Test
    @Transactional
    void getAllCompaniesByCurrentFundingCycleIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where currentFundingCycle is not null
        defaultCompanyShouldBeFound("currentFundingCycle.specified=true");

        // Get all the companyList where currentFundingCycle is null
        defaultCompanyShouldNotBeFound("currentFundingCycle.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByCurrentFundingCycleContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where currentFundingCycle contains DEFAULT_CURRENT_FUNDING_CYCLE
        defaultCompanyShouldBeFound("currentFundingCycle.contains=" + DEFAULT_CURRENT_FUNDING_CYCLE);

        // Get all the companyList where currentFundingCycle contains UPDATED_CURRENT_FUNDING_CYCLE
        defaultCompanyShouldNotBeFound("currentFundingCycle.contains=" + UPDATED_CURRENT_FUNDING_CYCLE);
    }

    @Test
    @Transactional
    void getAllCompaniesByCurrentFundingCycleNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where currentFundingCycle does not contain DEFAULT_CURRENT_FUNDING_CYCLE
        defaultCompanyShouldNotBeFound("currentFundingCycle.doesNotContain=" + DEFAULT_CURRENT_FUNDING_CYCLE);

        // Get all the companyList where currentFundingCycle does not contain UPDATED_CURRENT_FUNDING_CYCLE
        defaultCompanyShouldBeFound("currentFundingCycle.doesNotContain=" + UPDATED_CURRENT_FUNDING_CYCLE);
    }

    @Test
    @Transactional
    void getAllCompaniesByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = company.getUser();
        companyRepository.saveAndFlush(company);
        Long userId = user.getId();

        // Get all the companyList where user equals to userId
        defaultCompanyShouldBeFound("userId.equals=" + userId);

        // Get all the companyList where user equals to (userId + 1)
        defaultCompanyShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllCompaniesByTypeOfOrganationIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);
        TypeOfOrganization typeOfOrganation;
        if (TestUtil.findAll(em, TypeOfOrganization.class).isEmpty()) {
            typeOfOrganation = TypeOfOrganizationResourceIT.createEntity(em);
            em.persist(typeOfOrganation);
            em.flush();
        } else {
            typeOfOrganation = TestUtil.findAll(em, TypeOfOrganization.class).get(0);
        }
        em.persist(typeOfOrganation);
        em.flush();
        company.setTypeOfOrganation(typeOfOrganation);
        companyRepository.saveAndFlush(company);
        Long typeOfOrganationId = typeOfOrganation.getId();

        // Get all the companyList where typeOfOrganation equals to typeOfOrganationId
        defaultCompanyShouldBeFound("typeOfOrganationId.equals=" + typeOfOrganationId);

        // Get all the companyList where typeOfOrganation equals to (typeOfOrganationId + 1)
        defaultCompanyShouldNotBeFound("typeOfOrganationId.equals=" + (typeOfOrganationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompanyShouldBeFound(String filter) throws Exception {
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].strategicObjective").value(hasItem(DEFAULT_STRATEGIC_OBJECTIVE)))
            .andExpect(jsonPath("$.[*].futureFocusArea").value(hasItem(DEFAULT_FUTURE_FOCUS_AREA)))
            .andExpect(jsonPath("$.[*].currentFundingCycle").value(hasItem(DEFAULT_CURRENT_FUNDING_CYCLE)));

        // Check, that the count call also returns 1
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompanyShouldNotBeFound(String filter) throws Exception {
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).get();
        // Disconnect from session so that the updates on updatedCompany are not directly saved in db
        em.detach(updatedCompany);
        updatedCompany
            .strategicObjective(UPDATED_STRATEGIC_OBJECTIVE)
            .futureFocusArea(UPDATED_FUTURE_FOCUS_AREA)
            .currentFundingCycle(UPDATED_CURRENT_FUNDING_CYCLE);
        CompanyDTO companyDTO = companyMapper.toDto(updatedCompany);

        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getStrategicObjective()).isEqualTo(UPDATED_STRATEGIC_OBJECTIVE);
        assertThat(testCompany.getFutureFocusArea()).isEqualTo(UPDATED_FUTURE_FOCUS_AREA);
        assertThat(testCompany.getCurrentFundingCycle()).isEqualTo(UPDATED_CURRENT_FUNDING_CYCLE);
    }

    @Test
    @Transactional
    void putNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany.futureFocusArea(UPDATED_FUTURE_FOCUS_AREA).currentFundingCycle(UPDATED_CURRENT_FUNDING_CYCLE);

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getStrategicObjective()).isEqualTo(DEFAULT_STRATEGIC_OBJECTIVE);
        assertThat(testCompany.getFutureFocusArea()).isEqualTo(UPDATED_FUTURE_FOCUS_AREA);
        assertThat(testCompany.getCurrentFundingCycle()).isEqualTo(UPDATED_CURRENT_FUNDING_CYCLE);
    }

    @Test
    @Transactional
    void fullUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany
            .strategicObjective(UPDATED_STRATEGIC_OBJECTIVE)
            .futureFocusArea(UPDATED_FUTURE_FOCUS_AREA)
            .currentFundingCycle(UPDATED_CURRENT_FUNDING_CYCLE);

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getStrategicObjective()).isEqualTo(UPDATED_STRATEGIC_OBJECTIVE);
        assertThat(testCompany.getFutureFocusArea()).isEqualTo(UPDATED_FUTURE_FOCUS_AREA);
        assertThat(testCompany.getCurrentFundingCycle()).isEqualTo(UPDATED_CURRENT_FUNDING_CYCLE);
    }

    @Test
    @Transactional
    void patchNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(companyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Delete the company
        restCompanyMockMvc
            .perform(delete(ENTITY_API_URL_ID, company.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
