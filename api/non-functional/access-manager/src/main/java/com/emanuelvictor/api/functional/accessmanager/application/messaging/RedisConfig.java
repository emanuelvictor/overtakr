package com.emanuelvictor.api.functional.accessmanager.application.messaging;

import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    @Bean
    public RedisClient redisClient(RevokeTokenListener revokeTokenListener) {
        try {
            RedisClient redisClient = RedisClient.create("redis://localhost:6379/");
            StatefulRedisPubSubConnection<String, String> connection = redisClient.connectPubSub();
            connection.addListener(revokeTokenListener);
            RedisPubSubAsyncCommands<String, String> async = connection.async();
            async.subscribe("revoke-token-redis-channel");
            return redisClient;
        }catch (Exception e ){
            e.printStackTrace();
            return null;
        }
    }
}
