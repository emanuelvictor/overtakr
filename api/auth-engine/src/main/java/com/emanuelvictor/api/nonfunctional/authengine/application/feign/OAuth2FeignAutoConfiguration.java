package com.emanuelvictor.api.nonfunctional.authengine.application.feign;

import com.emanuelvictor.api.nonfunctional.authengine.application.feign.pagination.PageMixIn;
import com.emanuelvictor.api.nonfunctional.authengine.application.feign.pagination.PageableQueryEncoder;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import feign.RequestInterceptor;
import feign.codec.Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

/**
 * Configura o feign
 *
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 1.0.0, 10/09/2019
 */
@Configuration
@RequiredArgsConstructor
public class OAuth2FeignAutoConfiguration {

    /**
     *
     */
    private final ObjectFactory<HttpMessageConverters> messageConverters;

    /**
     * @param oAuth2RestTemplate OAuth2RestTemplate
     * @return RequestInterceptor
     */
    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(final OAuth2RestTemplate oAuth2RestTemplate) {
        return new OAuth2FeignRequestInterceptor(oAuth2RestTemplate);
    }

    /**
     * @return Encoder
     */
    @Bean
    public Encoder feignEncoder() {
        return new PageableQueryEncoder(new SpringEncoder(messageConverters));
    }

    /**
     * @return Module
     */
    @Bean
    public Module jacksonModule() {
        return new JacksonModule();
    }

    /**
     *
     */
    private static class JacksonModule extends SimpleModule {

        @Override
        public void setupModule(final Module.SetupContext context) {
            context.setMixInAnnotations(Page.class, PageMixIn.class);
        }
    }

}
