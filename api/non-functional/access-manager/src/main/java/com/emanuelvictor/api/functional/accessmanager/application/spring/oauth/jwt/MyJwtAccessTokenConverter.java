package com.emanuelvictor.api.functional.accessmanager.application.spring.oauth.jwt;

import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Map;

public class MyJwtAccessTokenConverter extends JwtAccessTokenConverter {

    private final static String DEFAULT_KEY = "integrator";

    public MyJwtAccessTokenConverter() {
        setSigningKey(DEFAULT_KEY);
    }

    protected Map<String, Object> decode(String token) {
        return super.decode(token);
    }

}
