package et.com.hmmk.rmt.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, et.com.hmmk.rmt.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, et.com.hmmk.rmt.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, et.com.hmmk.rmt.domain.User.class.getName());
            createCache(cm, et.com.hmmk.rmt.domain.Authority.class.getName());
            createCache(cm, et.com.hmmk.rmt.domain.User.class.getName() + ".authorities");
            createCache(cm, et.com.hmmk.rmt.domain.Form.class.getName());
            createCache(cm, et.com.hmmk.rmt.domain.Form.class.getName() + ".formProgressses");
            createCache(cm, et.com.hmmk.rmt.domain.Form.class.getName() + ".questions");
            createCache(cm, et.com.hmmk.rmt.domain.Question.class.getName());
            createCache(cm, et.com.hmmk.rmt.domain.Question.class.getName() + ".questionChoices");
            createCache(cm, et.com.hmmk.rmt.domain.Question.class.getName() + ".answers");
            createCache(cm, et.com.hmmk.rmt.domain.QuestionChoice.class.getName());
            createCache(cm, et.com.hmmk.rmt.domain.FormProgresss.class.getName());
            createCache(cm, et.com.hmmk.rmt.domain.FormProgresss.class.getName() + ".answers");
            createCache(cm, et.com.hmmk.rmt.domain.Answer.class.getName());
            createCache(cm, et.com.hmmk.rmt.domain.Answer.class.getName() + ".multipleChoiceAnsewers");
            createCache(cm, et.com.hmmk.rmt.domain.MultipleChoiceAnsewer.class.getName());
            createCache(cm, et.com.hmmk.rmt.domain.TypeOfOrganization.class.getName());
            createCache(cm, et.com.hmmk.rmt.domain.TypeOfOrganization.class.getName() + ".companies");
            createCache(cm, et.com.hmmk.rmt.domain.Company.class.getName());
            createCache(cm, et.com.hmmk.rmt.domain.Company.class.getName() + ".projects");
            createCache(cm, et.com.hmmk.rmt.domain.Project.class.getName());
            createCache(cm, et.com.hmmk.rmt.domain.Project.class.getName() + ".formProgresses");
            createCache(cm, et.com.hmmk.rmt.domain.News.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
