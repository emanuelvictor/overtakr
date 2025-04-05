package com.emanuelvictor.api.nonfunctional.authengine.application.services.token.repositories;

import com.emanuelvictor.api.nonfunctional.authengine.application.services.token.entities.Token;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.Optional;
import java.util.Set;

public interface TokenRepository extends TokenStore {

    Optional<Token> save(final String... tokenValueToCreate);

    Optional<Token> findTokenByValue(final String tokenValue);

    Set<Token> listTokensByName(final String name);

    void revoke(final String tokenValue);

    void remove(final String tokenValue);
}
