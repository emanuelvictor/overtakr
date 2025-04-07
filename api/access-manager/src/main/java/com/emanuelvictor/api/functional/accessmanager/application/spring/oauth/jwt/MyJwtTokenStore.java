package com.emanuelvictor.api.functional.accessmanager.application.spring.oauth.jwt;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.HashSet;
import java.util.Set;

public class MyJwtTokenStore extends JwtTokenStore {

    private final Set<String> blackList = new HashSet<>();
    private final MyJwtAccessTokenConverter jwtTokenEnhancer;

    public MyJwtTokenStore(MyJwtAccessTokenConverter jwtTokenEnhancer) {
        super(jwtTokenEnhancer);
        this.jwtTokenEnhancer = jwtTokenEnhancer;
    }

    public void revoke(final String token) {
        final OAuth2AccessToken accessToken = convertAccessToken(token);
        if (!jwtTokenEnhancer.isRefreshToken(accessToken)) {
            if (accessToken.getRefreshToken() != null)
                this.blackList.add(accessToken.getRefreshToken().getValue());
        }
        this.blackList.add(token);
    }

    private OAuth2AccessToken convertAccessToken(String tokenValue) {
        return jwtTokenEnhancer.extractAccessToken(tokenValue, jwtTokenEnhancer.decode(tokenValue));
    }

    private void revoke(final OAuth2AccessToken accessToken) {
        if (accessToken.getRefreshToken() != null)
            this.blackList.add(accessToken.getRefreshToken().getValue());
        this.blackList.add(accessToken.getValue());
    }

    private void revoke(final OAuth2RefreshToken refreshToken) {
        this.blackList.add(refreshToken.getValue());
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        if (this.blackList.contains(tokenValue))
            return null;
        return super.readRefreshToken(tokenValue);
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        revoke(token);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        if (this.blackList.contains(tokenValue))
            return null;
        return super.readAccessToken(tokenValue);
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
        revoke(refreshToken);
        super.removeRefreshToken(refreshToken);
    }
}
