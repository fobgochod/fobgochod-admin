package com.fobgochod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableCaching
@SpringBootApplication
public class FghApplication {

    public static ConfigurableApplicationContext ac;

    public static void main(String[] args) {
        FghApplication.ac = SpringApplication.run(FghApplication.class, args);
    }
}
