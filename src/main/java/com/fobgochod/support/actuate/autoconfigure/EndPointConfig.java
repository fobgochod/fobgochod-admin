package com.fobgochod.support.actuate.autoconfigure;

import com.fobgochod.support.actuate.log.LogEndpoint;
import com.fobgochod.support.actuate.logging.LogFileEndpoint;
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
