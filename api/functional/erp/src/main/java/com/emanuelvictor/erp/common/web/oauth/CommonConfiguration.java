package com.emanuelvictor.erp.common.web.oauth;

import com.emanuelvictor.erp.common.web.oauth.jwt.MyJwtAccessTokenConverter;
import com.emanuelvictor.erp.common.web.oauth.jwt.MyJwtTokenStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 2.0.0, 20/02/2020
 */
@Configuration
public class CommonConfiguration {

    /**
     * @return {@link TokenStore}
     */
    @Bean
    public TokenStore tokenStore() {
        return new MyJwtTokenStore(accessTokenConverter());
    }

    /**
     * @return {@link MyJwtAccessTokenConverter}
     */
    @Bean
    public MyJwtAccessTokenConverter accessTokenConverter() {
        return new MyJwtAccessTokenConverter();
    }

    /**
     * @return {@link DefaultTokenServices}
     */
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    /**
     * @return {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * @return {@link AuthenticationManager}
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() {
        OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
        authenticationManager.setTokenServices(tokenServices());
        return authenticationManager;
    }

}
