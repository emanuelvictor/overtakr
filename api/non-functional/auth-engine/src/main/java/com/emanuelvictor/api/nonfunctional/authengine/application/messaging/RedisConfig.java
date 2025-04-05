package com.emanuelvictor.api.nonfunctional.authengine.application.messaging;

import io.lettuce.core.RedisClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    @Bean
    public RedisClient redisClient() {
        return RedisClient.create("redis://localhost:6379/");
    }
}
