package com.emanuelvictor.api.nonfunctional.authengine.application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 2.0.0, 20/02/2020
 */
@Order(10)
@Configuration
@EnableResourceServer
@RequiredArgsConstructor
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String  RESOURCE_ID = "authorizationResourceApi";

    /**
     *
     */
    private final ResourceServerTokenServices resourceServerTokenServices;

    /**
     * @param config ResourceServerSecurityConfigurer
     */
    @Override
    public void configure(final ResourceServerSecurityConfigurer config) {
        config.tokenServices(resourceServerTokenServices).resourceId(RESOURCE_ID).stateless(false);
    }

    /**
     * Configura a permissão das requisições
     *
     * @param http HttpSecurity
     * @throws Exception
     */
    @Override
    public void configure(final HttpSecurity http) throws Exception {

        http
                .requestMatchers().antMatchers("/tokens/**")
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();

//        http
//
//                .authorizeRequests()
//                .antMatchers("/login", "/oauth/**", "/oauth/authorize").permitAll()
//                .anyRequest().authenticated()
//
//                .and()
//
//                .requestMatchers()
//                .antMatchers("/tokens/**");
////        http.antMatcher("/api/**").authorizeRequests().anyRequest().authenticated();
    }

}
