package com.emanuelvictor.api.functional.accessmanager.application.spring.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @version 1.0.0
 * @since 2.0.0, 31/01/2020
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {


    private final AuthenticationManager authenticationManagerBean;
//    /**
//     * {@inheritDoc}
//     *
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    protected void configure(final HttpSecurity http) throws Exception {
//        http.csrf()
//                .disable()
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
//        http.cors(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(
                authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer.anyRequest().permitAll());
//        http.csrf()
//                .disable()
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated();


        http
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .authenticationManager(authenticationManagerBean)
                        )
                );
        return http.build();
    }

}
