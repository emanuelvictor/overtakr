package com.emanuelvictor.api.nonfunctional.authengine.application.services.token.entities;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface Token {

    Optional<Token> revoke();

    Optional<Token> revokeNext();

    void revokePrevious();

    boolean isRevoked();

    Optional<Token> add(final Token token);

    void print();

    void printFromRoot();

    void printNext();

    void setPrevious(final Token token);

    Optional<Token> getPrevious();

    Optional<Token> findByValue(final String value);

    Optional<Token> getNext();

    String getValue();

    Optional<Token> getRoot();

    Optional<Token> getLeaf();

    int count();

    /**
     * @return Optional<IToken> the last access token
     */
    Optional<Token> getAccess();

    /**
     * @return Optional<IToken> the last refresh token
     */
    Optional<Token> getRefresh();

    LocalDateTime getCreatedOn();

    Set<Token> getAll();

    Set<Token>  getAll(final Set<Token> tokens);

    boolean isRoot();

    String getName();

    void setName(final String name);

    void extractNameFromToken();
}
