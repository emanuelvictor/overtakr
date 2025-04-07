package com.emanuelvictor.api.nonfunctional.authengine.application.services.token.services;

import com.emanuelvictor.api.nonfunctional.authengine.application.services.token.entities.Token;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.Set;

public interface TokenService extends AuthorizationServerTokenServices, ResourceServerTokenServices, ConsumerTokenServices {

    Set<Token> listTokensByName(final String name);

}
