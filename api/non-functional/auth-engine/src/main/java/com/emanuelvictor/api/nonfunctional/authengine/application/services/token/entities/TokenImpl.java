package com.emanuelvictor.api.nonfunctional.authengine.application.services.token.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@JsonIgnoreProperties({"next", "previous", "root", "leaf", "refresh", "access", "all"})
public class TokenImpl implements Token {

    /**
     *
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(TokenImpl.class);

    /**
     *
     */
    @Setter
    private LocalDateTime createdOn;

    /**
     *
     */
    @Setter
    private Token next;

    /**
     *
     */
    @Setter
    private Token previous;

    /**
     *
     */
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private boolean revoked = false;

    /**
     *
     */
    @Getter
    @Setter
    private String name;

    /**
     *
     */
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private String value;

    /**
     * @param value String
     */
    public TokenImpl(final String value, final String name) {
        this.createdOn = LocalDateTime.now();
        this.value = value;
        this.name = name;
    }

    /**
     *
     */
    @Override
    public void extractNameFromToken() {
        this.getRoot().orElseThrow().setName(this.name);
    }

    /**
     * @return Optional<IToken>
     */
    public Optional<Token> getNext() {
        return Optional.ofNullable(next);
    }

    /**
     * @return Optional<IToken>
     */
    public Optional<Token> getPrevious() {
        return Optional.ofNullable(previous);
    }

    /**
     * @param next IToken
     * @return Optional<IToken>
     */
    public Optional<Token> add(final Token next) {

        if (this.getValue().equals(next.getValue())) {
//            LOGGER.info("Token already exits for this session");
            return Optional.of(this);
        }

        this.getNext().ifPresentOrElse(present -> {
            present.add(next);
            present.extractNameFromToken();
        }, () -> {
            next.setPrevious(this);
            this.setNext(next);
            this.extractNameFromToken();
        });

        return Optional.of(next);
    }

    // ------------- Revoke

    /**
     * @return Optional<IToken>
     */
    @Override
    public Optional<Token> revoke() {
        this.revokePrevious();
        if (!this.isRevoked()) {
            this.setRevoked(true);
            LOGGER.warn("Token with value " + this.getValue() + " revoked");
        }
        return this.revokeNext();
    }

    /**
     * @return Optional<IToken>
     */
    @Override
    public Optional<Token> revokeNext() {
        return this.getNext().flatMap(Token::revoke);
    }

    /**
     *
     */
    @Override
    public void revokePrevious() {
        this.getPrevious().ifPresent(present -> {
            if (!present.isRevoked())
                present.revoke();
        });
    }

    //  ------------- Print

    /**
     *
     */
    @Override
    public void print() {
        if (getPrevious().isPresent())
            System.out.print(" --> ");
        else {
            System.out.println(" ---------- ");
            System.out.println();
        }
        System.out.print(this.getValue());
        if (getNext().isEmpty()) {
            System.out.println();
            System.out.println();
            System.out.println(" ---------- ");
        }
        this.printNext();
    }

    /**
     *
     */
    @Override
    public void printFromRoot() {
        this.getRoot().orElseThrow().print();
    }

    /**
     *
     */
    @Override
    public void printNext() {
        this.getNext().ifPresent(Token::print);
    }


    // ------------- Find

    /**
     * @param value String
     * @return Optional<IToken>
     */
    @Override
    public Optional<Token> findByValue(final String value) {

        if (this.getValue().equals(value))
            return Optional.of(this);

        if (this.getNext().isPresent())
            return this.getNext().orElseThrow().findByValue(value);

        return Optional.empty();

    }

    // ------------- Retrieve

    /**
     * @return Optional<IToken>
     */
    @Override
    public Optional<Token> getRoot() {
        if (this.getPrevious().isEmpty())
            return Optional.of(this);
        else {
            return this.getPrevious().orElseThrow().getRoot();
        }
    }

    /**
     * @return Optional<IToken>
     */
    @Override
    public Optional<Token> getLeaf() {
        if (this.getNext().isEmpty())
            return Optional.of(this);
        else {
            return this.getNext().orElseThrow().getLeaf();
        }
    }


    /**
     * @return Optional<IToken> the last access token
     */
    @Override
    public Optional<Token> getAccess() {
        return this.getLeaf().orElseThrow().getPrevious();
    }

    /**
     * @return Optional<IToken> the last refresh token
     */
    @Override
    public Optional<Token> getRefresh() {
        return this.getLeaf();
    }

    // ------------- Count

    /**
     * @return int
     */
    @Override
    public int count() {
        return this.getAll().size();
    }

    // ------------- Created On

    /**
     * @return LocalDateTime
     */
    @Override
    public LocalDateTime getCreatedOn() {
        return this.createdOn;
    }

    // ------------- getAll

    /**
     * @return Set<IToken>
     */
    public Set<Token> getAll() {
        return this.getRoot().orElseThrow().getAll(new HashSet<>());
    }

    /**
     * TODO must be cover in tests
     *
     * @param tokens Set<IToken>
     * @return Set<IToken>
     */
    public Set<Token> getAll(final Set<Token> tokens) {

        tokens.add(this);

        getNext().ifPresent(next -> next.getAll(tokens));

        return tokens;

    }

    /**
     * TODO must be cover in tests
     *
     * @return boolean
     */
    public boolean isRoot() {
        return this.value.equals(this.getRoot().orElseThrow().getValue());
    }

    // ------------- Hashcode and equals

    /**
     * @param o Object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenImpl that = (TokenImpl) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }

    /**
     * @return int
     */
    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
