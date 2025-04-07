package com.emanuelvictor.api.functional.accessmanager.application.messaging;


import com.emanuelvictor.api.functional.accessmanager.application.spring.oauth.jwt.MyJwtTokenStore;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

@Service
public class RevokeTokenListener extends RedisPubSubAdapter<String, String> {

    public static final Logger LOGGER = LoggerFactory.getLogger(RevokeTokenListener.class);

    private final TokenStore tokenStore;

    public RevokeTokenListener(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public void message(String channel, String token) {
        LOGGER.debug("Revoking token {}", token);
        ((MyJwtTokenStore) this.tokenStore).revoke(token);
    }
}
