package et.com.hmmk.rmt.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import et.com.hmmk.rmt.IntegrationTest;
import et.com.hmmk.rmt.domain.News;
import et.com.hmmk.rmt.repository.NewsRepository;
import et.com.hmmk.rmt.service.criteria.NewsCriteria;
import et.com.hmmk.rmt.service.dto.NewsDTO;
import et.com.hmmk.rmt.service.mapper.NewsMapper;
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
 * Integration tests for the {@link NewsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NewsResourceIT {

    private static final byte[] DEFAULT_FEATURED_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FEATURED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FEATURED_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FEATURED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FEATURED_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_FEATURED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_DETAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_REGISTERED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REGISTERED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/news";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNewsMockMvc;

    private News news;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static News createEntity(EntityManager em) {
        News news = new News()
            .featuredImage(DEFAULT_FEATURED_IMAGE)
            .featuredImageContentType(DEFAULT_FEATURED_IMAGE_CONTENT_TYPE)
            .featuredImageUrl(DEFAULT_FEATURED_IMAGE_URL)
            .title(DEFAULT_TITLE)
            .detail(DEFAULT_DETAIL)
            .createdBy(DEFAULT_CREATED_BY)
            .registeredTime(DEFAULT_REGISTERED_TIME)
            .updateTime(DEFAULT_UPDATE_TIME);
        return news;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static News createUpdatedEntity(EntityManager em) {
        News news = new News()
            .featuredImage(UPDATED_FEATURED_IMAGE)
            .featuredImageContentType(UPDATED_FEATURED_IMAGE_CONTENT_TYPE)
            .featuredImageUrl(UPDATED_FEATURED_IMAGE_URL)
            .title(UPDATED_TITLE)
            .detail(UPDATED_DETAIL)
            .createdBy(UPDATED_CREATED_BY)
            .registeredTime(UPDATED_REGISTERED_TIME)
            .updateTime(UPDATED_UPDATE_TIME);
        return news;
    }

    @BeforeEach
    public void initTest() {
        news = createEntity(em);
    }

    @Test
    @Transactional
    void createNews() throws Exception {
        int databaseSizeBeforeCreate = newsRepository.findAll().size();
        // Create the News
        NewsDTO newsDTO = newsMapper.toDto(news);
        restNewsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(newsDTO)))
            .andExpect(status().isCreated());

        // Validate the News in the database
        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeCreate + 1);
        News testNews = newsList.get(newsList.size() - 1);
        assertThat(testNews.getFeaturedImage()).isEqualTo(DEFAULT_FEATURED_IMAGE);
        assertThat(testNews.getFeaturedImageContentType()).isEqualTo(DEFAULT_FEATURED_IMAGE_CONTENT_TYPE);
        assertThat(testNews.getFeaturedImageUrl()).isEqualTo(DEFAULT_FEATURED_IMAGE_URL);
        assertThat(testNews.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNews.getDetail()).isEqualTo(DEFAULT_DETAIL);
        assertThat(testNews.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testNews.getRegisteredTime()).isEqualTo(DEFAULT_REGISTERED_TIME);
        assertThat(testNews.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
    }

    @Test
    @Transactional
    void createNewsWithExistingId() throws Exception {
        // Create the News with an existing ID
        news.setId(1L);
        NewsDTO newsDTO = newsMapper.toDto(news);

        int databaseSizeBeforeCreate = newsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNewsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(newsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the News in the database
        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNews() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList
        restNewsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(news.getId().intValue())))
            .andExpect(jsonPath("$.[*].featuredImageContentType").value(hasItem(DEFAULT_FEATURED_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].featuredImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_FEATURED_IMAGE))))
            .andExpect(jsonPath("$.[*].featuredImageUrl").value(hasItem(DEFAULT_FEATURED_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].registeredTime").value(hasItem(DEFAULT_REGISTERED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())));
    }

    @Test
    @Transactional
    void getNews() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get the news
        restNewsMockMvc
            .perform(get(ENTITY_API_URL_ID, news.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(news.getId().intValue()))
            .andExpect(jsonPath("$.featuredImageContentType").value(DEFAULT_FEATURED_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.featuredImage").value(Base64Utils.encodeToString(DEFAULT_FEATURED_IMAGE)))
            .andExpect(jsonPath("$.featuredImageUrl").value(DEFAULT_FEATURED_IMAGE_URL))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.detail").value(DEFAULT_DETAIL.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.registeredTime").value(DEFAULT_REGISTERED_TIME.toString()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()));
    }

    @Test
    @Transactional
    void getNewsByIdFiltering() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        Long id = news.getId();

        defaultNewsShouldBeFound("id.equals=" + id);
        defaultNewsShouldNotBeFound("id.notEquals=" + id);

        defaultNewsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNewsShouldNotBeFound("id.greaterThan=" + id);

        defaultNewsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNewsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNewsByFeaturedImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where featuredImageUrl equals to DEFAULT_FEATURED_IMAGE_URL
        defaultNewsShouldBeFound("featuredImageUrl.equals=" + DEFAULT_FEATURED_IMAGE_URL);

        // Get all the newsList where featuredImageUrl equals to UPDATED_FEATURED_IMAGE_URL
        defaultNewsShouldNotBeFound("featuredImageUrl.equals=" + UPDATED_FEATURED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllNewsByFeaturedImageUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where featuredImageUrl not equals to DEFAULT_FEATURED_IMAGE_URL
        defaultNewsShouldNotBeFound("featuredImageUrl.notEquals=" + DEFAULT_FEATURED_IMAGE_URL);

        // Get all the newsList where featuredImageUrl not equals to UPDATED_FEATURED_IMAGE_URL
        defaultNewsShouldBeFound("featuredImageUrl.notEquals=" + UPDATED_FEATURED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllNewsByFeaturedImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where featuredImageUrl in DEFAULT_FEATURED_IMAGE_URL or UPDATED_FEATURED_IMAGE_URL
        defaultNewsShouldBeFound("featuredImageUrl.in=" + DEFAULT_FEATURED_IMAGE_URL + "," + UPDATED_FEATURED_IMAGE_URL);

        // Get all the newsList where featuredImageUrl equals to UPDATED_FEATURED_IMAGE_URL
        defaultNewsShouldNotBeFound("featuredImageUrl.in=" + UPDATED_FEATURED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllNewsByFeaturedImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where featuredImageUrl is not null
        defaultNewsShouldBeFound("featuredImageUrl.specified=true");

        // Get all the newsList where featuredImageUrl is null
        defaultNewsShouldNotBeFound("featuredImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllNewsByFeaturedImageUrlContainsSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where featuredImageUrl contains DEFAULT_FEATURED_IMAGE_URL
        defaultNewsShouldBeFound("featuredImageUrl.contains=" + DEFAULT_FEATURED_IMAGE_URL);

        // Get all the newsList where featuredImageUrl contains UPDATED_FEATURED_IMAGE_URL
        defaultNewsShouldNotBeFound("featuredImageUrl.contains=" + UPDATED_FEATURED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllNewsByFeaturedImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where featuredImageUrl does not contain DEFAULT_FEATURED_IMAGE_URL
        defaultNewsShouldNotBeFound("featuredImageUrl.doesNotContain=" + DEFAULT_FEATURED_IMAGE_URL);

        // Get all the newsList where featuredImageUrl does not contain UPDATED_FEATURED_IMAGE_URL
        defaultNewsShouldBeFound("featuredImageUrl.doesNotContain=" + UPDATED_FEATURED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllNewsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where title equals to DEFAULT_TITLE
        defaultNewsShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the newsList where title equals to UPDATED_TITLE
        defaultNewsShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllNewsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where title not equals to DEFAULT_TITLE
        defaultNewsShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the newsList where title not equals to UPDATED_TITLE
        defaultNewsShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllNewsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultNewsShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the newsList where title equals to UPDATED_TITLE
        defaultNewsShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllNewsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where title is not null
        defaultNewsShouldBeFound("title.specified=true");

        // Get all the newsList where title is null
        defaultNewsShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllNewsByTitleContainsSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where title contains DEFAULT_TITLE
        defaultNewsShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the newsList where title contains UPDATED_TITLE
        defaultNewsShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllNewsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where title does not contain DEFAULT_TITLE
        defaultNewsShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the newsList where title does not contain UPDATED_TITLE
        defaultNewsShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllNewsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where createdBy equals to DEFAULT_CREATED_BY
        defaultNewsShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the newsList where createdBy equals to UPDATED_CREATED_BY
        defaultNewsShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllNewsByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where createdBy not equals to DEFAULT_CREATED_BY
        defaultNewsShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the newsList where createdBy not equals to UPDATED_CREATED_BY
        defaultNewsShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllNewsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultNewsShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the newsList where createdBy equals to UPDATED_CREATED_BY
        defaultNewsShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllNewsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where createdBy is not null
        defaultNewsShouldBeFound("createdBy.specified=true");

        // Get all the newsList where createdBy is null
        defaultNewsShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllNewsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where createdBy contains DEFAULT_CREATED_BY
        defaultNewsShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the newsList where createdBy contains UPDATED_CREATED_BY
        defaultNewsShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllNewsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where createdBy does not contain DEFAULT_CREATED_BY
        defaultNewsShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the newsList where createdBy does not contain UPDATED_CREATED_BY
        defaultNewsShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllNewsByRegisteredTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where registeredTime equals to DEFAULT_REGISTERED_TIME
        defaultNewsShouldBeFound("registeredTime.equals=" + DEFAULT_REGISTERED_TIME);

        // Get all the newsList where registeredTime equals to UPDATED_REGISTERED_TIME
        defaultNewsShouldNotBeFound("registeredTime.equals=" + UPDATED_REGISTERED_TIME);
    }

    @Test
    @Transactional
    void getAllNewsByRegisteredTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where registeredTime not equals to DEFAULT_REGISTERED_TIME
        defaultNewsShouldNotBeFound("registeredTime.notEquals=" + DEFAULT_REGISTERED_TIME);

        // Get all the newsList where registeredTime not equals to UPDATED_REGISTERED_TIME
        defaultNewsShouldBeFound("registeredTime.notEquals=" + UPDATED_REGISTERED_TIME);
    }

    @Test
    @Transactional
    void getAllNewsByRegisteredTimeIsInShouldWork() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where registeredTime in DEFAULT_REGISTERED_TIME or UPDATED_REGISTERED_TIME
        defaultNewsShouldBeFound("registeredTime.in=" + DEFAULT_REGISTERED_TIME + "," + UPDATED_REGISTERED_TIME);

        // Get all the newsList where registeredTime equals to UPDATED_REGISTERED_TIME
        defaultNewsShouldNotBeFound("registeredTime.in=" + UPDATED_REGISTERED_TIME);
    }

    @Test
    @Transactional
    void getAllNewsByRegisteredTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where registeredTime is not null
        defaultNewsShouldBeFound("registeredTime.specified=true");

        // Get all the newsList where registeredTime is null
        defaultNewsShouldNotBeFound("registeredTime.specified=false");
    }

    @Test
    @Transactional
    void getAllNewsByUpdateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where updateTime equals to DEFAULT_UPDATE_TIME
        defaultNewsShouldBeFound("updateTime.equals=" + DEFAULT_UPDATE_TIME);

        // Get all the newsList where updateTime equals to UPDATED_UPDATE_TIME
        defaultNewsShouldNotBeFound("updateTime.equals=" + UPDATED_UPDATE_TIME);
    }

    @Test
    @Transactional
    void getAllNewsByUpdateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where updateTime not equals to DEFAULT_UPDATE_TIME
        defaultNewsShouldNotBeFound("updateTime.notEquals=" + DEFAULT_UPDATE_TIME);

        // Get all the newsList where updateTime not equals to UPDATED_UPDATE_TIME
        defaultNewsShouldBeFound("updateTime.notEquals=" + UPDATED_UPDATE_TIME);
    }

    @Test
    @Transactional
    void getAllNewsByUpdateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where updateTime in DEFAULT_UPDATE_TIME or UPDATED_UPDATE_TIME
        defaultNewsShouldBeFound("updateTime.in=" + DEFAULT_UPDATE_TIME + "," + UPDATED_UPDATE_TIME);

        // Get all the newsList where updateTime equals to UPDATED_UPDATE_TIME
        defaultNewsShouldNotBeFound("updateTime.in=" + UPDATED_UPDATE_TIME);
    }

    @Test
    @Transactional
    void getAllNewsByUpdateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where updateTime is not null
        defaultNewsShouldBeFound("updateTime.specified=true");

        // Get all the newsList where updateTime is null
        defaultNewsShouldNotBeFound("updateTime.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNewsShouldBeFound(String filter) throws Exception {
        restNewsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(news.getId().intValue())))
            .andExpect(jsonPath("$.[*].featuredImageContentType").value(hasItem(DEFAULT_FEATURED_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].featuredImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_FEATURED_IMAGE))))
            .andExpect(jsonPath("$.[*].featuredImageUrl").value(hasItem(DEFAULT_FEATURED_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].registeredTime").value(hasItem(DEFAULT_REGISTERED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())));

        // Check, that the count call also returns 1
        restNewsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNewsShouldNotBeFound(String filter) throws Exception {
        restNewsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNewsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNews() throws Exception {
        // Get the news
        restNewsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNews() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        int databaseSizeBeforeUpdate = newsRepository.findAll().size();

        // Update the news
        News updatedNews = newsRepository.findById(news.getId()).get();
        // Disconnect from session so that the updates on updatedNews are not directly saved in db
        em.detach(updatedNews);
        updatedNews
            .featuredImage(UPDATED_FEATURED_IMAGE)
            .featuredImageContentType(UPDATED_FEATURED_IMAGE_CONTENT_TYPE)
            .featuredImageUrl(UPDATED_FEATURED_IMAGE_URL)
            .title(UPDATED_TITLE)
            .detail(UPDATED_DETAIL)
            .createdBy(UPDATED_CREATED_BY)
            .registeredTime(UPDATED_REGISTERED_TIME)
            .updateTime(UPDATED_UPDATE_TIME);
        NewsDTO newsDTO = newsMapper.toDto(updatedNews);

        restNewsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, newsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(newsDTO))
            )
            .andExpect(status().isOk());

        // Validate the News in the database
        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeUpdate);
        News testNews = newsList.get(newsList.size() - 1);
        assertThat(testNews.getFeaturedImage()).isEqualTo(UPDATED_FEATURED_IMAGE);
        assertThat(testNews.getFeaturedImageContentType()).isEqualTo(UPDATED_FEATURED_IMAGE_CONTENT_TYPE);
        assertThat(testNews.getFeaturedImageUrl()).isEqualTo(UPDATED_FEATURED_IMAGE_URL);
        assertThat(testNews.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNews.getDetail()).isEqualTo(UPDATED_DETAIL);
        assertThat(testNews.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testNews.getRegisteredTime()).isEqualTo(UPDATED_REGISTERED_TIME);
        assertThat(testNews.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
    }

    @Test
    @Transactional
    void putNonExistingNews() throws Exception {
        int databaseSizeBeforeUpdate = newsRepository.findAll().size();
        news.setId(count.incrementAndGet());

        // Create the News
        NewsDTO newsDTO = newsMapper.toDto(news);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNewsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, newsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(newsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the News in the database
        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNews() throws Exception {
        int databaseSizeBeforeUpdate = newsRepository.findAll().size();
        news.setId(count.incrementAndGet());

        // Create the News
        NewsDTO newsDTO = newsMapper.toDto(news);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNewsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(newsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the News in the database
        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNews() throws Exception {
        int databaseSizeBeforeUpdate = newsRepository.findAll().size();
        news.setId(count.incrementAndGet());

        // Create the News
        NewsDTO newsDTO = newsMapper.toDto(news);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNewsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(newsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the News in the database
        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNewsWithPatch() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        int databaseSizeBeforeUpdate = newsRepository.findAll().size();

        // Update the news using partial update
        News partialUpdatedNews = new News();
        partialUpdatedNews.setId(news.getId());

        partialUpdatedNews
            .featuredImage(UPDATED_FEATURED_IMAGE)
            .featuredImageContentType(UPDATED_FEATURED_IMAGE_CONTENT_TYPE)
            .title(UPDATED_TITLE)
            .detail(UPDATED_DETAIL)
            .createdBy(UPDATED_CREATED_BY)
            .registeredTime(UPDATED_REGISTERED_TIME)
            .updateTime(UPDATED_UPDATE_TIME);

        restNewsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNews.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNews))
            )
            .andExpect(status().isOk());

        // Validate the News in the database
        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeUpdate);
        News testNews = newsList.get(newsList.size() - 1);
        assertThat(testNews.getFeaturedImage()).isEqualTo(UPDATED_FEATURED_IMAGE);
        assertThat(testNews.getFeaturedImageContentType()).isEqualTo(UPDATED_FEATURED_IMAGE_CONTENT_TYPE);
        assertThat(testNews.getFeaturedImageUrl()).isEqualTo(DEFAULT_FEATURED_IMAGE_URL);
        assertThat(testNews.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNews.getDetail()).isEqualTo(UPDATED_DETAIL);
        assertThat(testNews.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testNews.getRegisteredTime()).isEqualTo(UPDATED_REGISTERED_TIME);
        assertThat(testNews.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
    }

    @Test
    @Transactional
    void fullUpdateNewsWithPatch() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        int databaseSizeBeforeUpdate = newsRepository.findAll().size();

        // Update the news using partial update
        News partialUpdatedNews = new News();
        partialUpdatedNews.setId(news.getId());

        partialUpdatedNews
            .featuredImage(UPDATED_FEATURED_IMAGE)
            .featuredImageContentType(UPDATED_FEATURED_IMAGE_CONTENT_TYPE)
            .featuredImageUrl(UPDATED_FEATURED_IMAGE_URL)
            .title(UPDATED_TITLE)
            .detail(UPDATED_DETAIL)
            .createdBy(UPDATED_CREATED_BY)
            .registeredTime(UPDATED_REGISTERED_TIME)
            .updateTime(UPDATED_UPDATE_TIME);

        restNewsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNews.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNews))
            )
            .andExpect(status().isOk());

        // Validate the News in the database
        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeUpdate);
        News testNews = newsList.get(newsList.size() - 1);
        assertThat(testNews.getFeaturedImage()).isEqualTo(UPDATED_FEATURED_IMAGE);
        assertThat(testNews.getFeaturedImageContentType()).isEqualTo(UPDATED_FEATURED_IMAGE_CONTENT_TYPE);
        assertThat(testNews.getFeaturedImageUrl()).isEqualTo(UPDATED_FEATURED_IMAGE_URL);
        assertThat(testNews.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNews.getDetail()).isEqualTo(UPDATED_DETAIL);
        assertThat(testNews.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testNews.getRegisteredTime()).isEqualTo(UPDATED_REGISTERED_TIME);
        assertThat(testNews.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingNews() throws Exception {
        int databaseSizeBeforeUpdate = newsRepository.findAll().size();
        news.setId(count.incrementAndGet());

        // Create the News
        NewsDTO newsDTO = newsMapper.toDto(news);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNewsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, newsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(newsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the News in the database
        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNews() throws Exception {
        int databaseSizeBeforeUpdate = newsRepository.findAll().size();
        news.setId(count.incrementAndGet());

        // Create the News
        NewsDTO newsDTO = newsMapper.toDto(news);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNewsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(newsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the News in the database
        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNews() throws Exception {
        int databaseSizeBeforeUpdate = newsRepository.findAll().size();
        news.setId(count.incrementAndGet());

        // Create the News
        NewsDTO newsDTO = newsMapper.toDto(news);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNewsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(newsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the News in the database
        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNews() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        int databaseSizeBeforeDelete = newsRepository.findAll().size();

        // Delete the news
        restNewsMockMvc
            .perform(delete(ENTITY_API_URL_ID, news.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
