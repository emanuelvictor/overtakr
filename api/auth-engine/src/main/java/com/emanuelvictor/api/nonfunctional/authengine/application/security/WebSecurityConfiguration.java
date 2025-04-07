package com.emanuelvictor.api.nonfunctional.authengine.application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 2.0.0, 31/01/2020
 */
@Order(20)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableSpringHttpSession
// It's required only to session repository, that is, only to removing jsessionid from session repository.
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     *
     */
    private final UserDetailsService userDetailsService;

    /**
     *
     */
    private final CustomLogoutHandler customLogoutHandler;

    /**
     * {@inheritDoc}
     *
     * @param builder
     * @throws Exception
     */
    @Override
    protected void configure(final AuthenticationManagerBuilder builder) throws Exception {
        builder/*.authenticationProvider(this.authenticationProvider())*/.userDetailsService(userDetailsService);
    }

    /**
     * Repository from jsessionid's
     * Necessary to revoke session after revoke token
     *
     * @return MapSessionRepository
     */
    @Bean
    public MapSessionRepository sessionRepository() {
        return new MapSessionRepository(new ConcurrentHashMap<>());
    }

    /**
     * @return AuthenticationManager
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/oauth/authorize");
    }

    /**
     * {@inheritDoc}
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http
                .sessionManagement().sessionFixation().none()

                .and()

                .requestMatchers()
                .antMatchers("/", "/ui/**", "/login", "/oauth/authorize", "/logout")
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/ui/public/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(customLogoutHandler)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");


//        http
//                .sessionManagement().sessionFixation().none()
//                .and()
//                .csrf()
//                .disable()
//
////                .authorizeRequests()
////                .antMatchers("/tokens/**")
////                .authenticated()
////
////                .and()
////
////                .antMatcher("/oauth/")
////                .authorizeRequests()
////                .antMatchers("/oauth/token","/oauth/token_key","/oauth/check_token")
////                .permitAll()
////
////                .and()
////
////                .antMatcher("/oauth/**")
////                .authorizeRequests()
////                .antMatchers("/oauth/authorize","/oauth/authorize/**")
////                .authenticated()
////
////                .and()
////
////                .antMatcher("/oauth/authorize")
////                .authorizeRequests()
////                .antMatchers("/login")
////                .permitAll()
////
////                .and()
//
//                .antMatcher("/login")
//                .authorizeRequests()
//
//                .antMatchers("/**")
//                .permitAll()
//
//
//                .and()
////
////                .antMatcher("/oauth/**")
////                .authorizeRequests()
////                .antMatchers("/authorize", "/tokens")
////                .permitAll()
////
////                .and()
//
//
//
//                .formLogin()
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .addLogoutHandler(customLogoutHandler)
//                .permitAll().and().logout().permitAll();
//
////        http
////                .sessionManagement().sessionFixation().none()
////                .and()
////                .csrf()
////                .disable()
////                .authorizeRequests()
////                .antMatchers("/**"/*, "/login", "/oauth/authorize"*/)
////                .permitAll()
////                .and().formLogin()
////                .and()
////                .logout()
////                .logoutUrl("/logout")
////                .addLogoutHandler(customLogoutHandler)
////                .permitAll().and().logout().permitAll();
    }
}
