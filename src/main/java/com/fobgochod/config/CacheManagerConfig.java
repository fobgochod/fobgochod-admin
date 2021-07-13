package com.fobgochod.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.io.IOException;

/**
 * CacheManager配置
 *
 * @author zhouxiao
 * @date 2021/7/13
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class CacheManagerConfig {

    private final CacheProperties cacheProperties;

    CacheManagerConfig(CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    @Primary
    @Bean
    public JCacheCacheManager jCacheCacheManager() throws IOException {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        Resource configLocation = this.cacheProperties
                .resolveConfigLocation(this.cacheProperties.getJcache().getConfig());
        CacheManager cacheManager = cachingProvider.getCacheManager(configLocation.getURI(), null, null);
        return new JCacheCacheManager(cacheManager);
    }

    public interface JCacheNames {

        String LOGIN_TOKEN = "login.token";
    }
}
