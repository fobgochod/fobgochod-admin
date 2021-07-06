package com.fobgochod.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fobgochod.auth.handler.FghLocaleResolver;
import com.fobgochod.util.JsonUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * auto config
 *
 * @author zhouxiao
 * @date 2020/4/27
 */
@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class FghAutoConfiguration {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JsonUtils.createObjectMapper();
    }

    @Bean
    @Primary
    public LocaleResolver localeResolver() {
        return new FghLocaleResolver();
    }

    @Bean
    @Primary
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    @Primary
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(JsonUtils.createObjectMapper());
        return mappingJackson2HttpMessageConverter;
    }

    @Bean
    @Primary
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory();
    }

    @Bean
    @Primary
    public RestTemplate restTemplate(ClientHttpRequestFactory factory, ObjectMapper objectMapper) {
        RestTemplate restTemplate = new RestTemplate(factory);
        for (HttpMessageConverter<?> httpMessageConverter : restTemplate.getMessageConverters()) {
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) httpMessageConverter).setObjectMapper(objectMapper);
            }
        }
        restTemplate.setInterceptors(Collections.singletonList(new AcceptLanguageInterceptor()));
        return restTemplate;
    }

    @Bean
    @Primary
    public ThreadPoolTaskExecutor fobTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }

    private static class AcceptLanguageInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            HttpHeaders headers = request.getHeaders();
            if (!headers.containsKey(HttpHeaders.ACCEPT_LANGUAGE)) {
                headers.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().toLanguageTag());
            }
            return execution.execute(request, body);
        }
    }
}
