package com.emanuelvictor.api.nonfunctional.authengine.application.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.util.Assert;

/**
 * Interceptor para as requisições feign
 *
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 1.0.0, 10/09/2019
 */
@RequiredArgsConstructor
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    /**
     *
     */
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     *
     */
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2FeignRequestInterceptor.class);

    /**
     *
     */
    private final OAuth2RestTemplate oAuth2RestTemplate;

    /**
     * @param template RequestTemplate
     */
    @Override
    public void apply(final RequestTemplate template) {
        Assert.notNull(oAuth2RestTemplate, "Context can not be null");

        if (template.headers().containsKey(AUTHORIZATION_HEADER)) {
            LOGGER.warn("The Authorization token has been already set");
            // Se não estiver autneticado ou se não tem token de acesso
        } else if (oAuth2RestTemplate.getAccessToken().getValue() == null) {
            LOGGER.warn("Can not obtain existing token for request, if it is a non secured request, ignore.");
        } else {
            LOGGER.debug("Constructing Header {} for Token {}", AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE);
            template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, oAuth2RestTemplate.getAccessToken().getValue()));
        }
    }
}
