package com.emanuelvictor.common.infrastructure.authorization;

import io.lettuce.core.RedisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResourceServerNotifier {

    public void revoke(String sid) {
        try (final var connection = redisClient().connectPubSub()) {
            final var async = connection.async();
            async.publish("revoke-token-redis-channel", sid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RedisClient redisClient() {
        return RedisClient.create("redis://localhost:6379/");
    }

}