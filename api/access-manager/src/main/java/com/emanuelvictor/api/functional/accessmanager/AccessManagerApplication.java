package com.emanuelvictor.api.functional.accessmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;
import java.util.List;

@EnableAsync
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class AccessManagerApplication extends SpringBootServletInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(AccessManagerApplication.class);

    /**
     * @param args String[]
     */
    public static void main(final String[] args) {
        SpringApplication.run(AccessManagerApplication.class, args);
    }

    /**
     * @param application SpringApplicationBuilder
     * @return SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(AccessManagerApplication.class);
    }

    /**
     * @return ApplicationListener<ApplicationReadyEvent>
     */
    @Bean
    public ApplicationListener<ApplicationReadyEvent> getApplicationReadyEvent() {
        return applicationReadyEvent -> {
            LOGGER.info("--------------------------------------------------");

            final List<String> profiles = Arrays.asList(applicationReadyEvent.getApplicationContext().getEnvironment().getActiveProfiles());

            if (profiles.isEmpty()) {
                LOGGER.info("Sistema iniciado com o perfil de configuração: dev");
            }

            profiles.forEach(profile ->
                    LOGGER.info("Sistema iniciado com o perfil de configuração: {}", profile)
            );
            LOGGER.info("--------------------------------------------------");
        };
    }
}
