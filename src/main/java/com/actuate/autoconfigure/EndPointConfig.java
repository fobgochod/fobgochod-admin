package com.actuate.autoconfigure;

import com.actuate.log.LogEndpoint;
import com.actuate.logging.LogFileEndpoint;
import org.springframework.boot.actuate.logging.LogFileWebEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class EndPointConfig {

    @Primary
    @Bean
    public LogFileWebEndpoint logFileWebEndpoint() {
        return new LogFileEndpoint(null, null);
    }

    @Bean
    public LogEndpoint logEndpoint() {
        return new LogEndpoint();
    }
}
