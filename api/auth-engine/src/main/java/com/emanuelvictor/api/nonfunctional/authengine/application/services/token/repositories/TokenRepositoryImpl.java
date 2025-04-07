package com.emanuelvictor.api.nonfunctional.authengine.application.services.token.repositories;

import com.emanuelvictor.api.nonfunctional.authengine.application.services.token.entities.Token;
import com.emanuelvictor.api.nonfunctional.authengine.application.services.token.entities.TokenImpl;
import com.emanuelvictor.api.nonfunctional.authengine.domain.entities.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.*;
import java.util.stream.Collectors;

public class TokenRepositoryImpl implements TokenRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(TokenRepositoryImpl.class);

    private final Set<Token> tokens = new HashSet<>();
    private final JwtAccessTokenConverter jwtAccessTokenConverter;

    public TokenRepositoryImpl(JwtAccessTokenConverter jwtAccessTokenConverter) {
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
    }

    /**
     * Create several and return the root
     *
     * @param tokensValueToCreate String
     * @return Optional<IToken>
     */
    public Optional<Token> save(final String... tokensValueToCreate) {

        if (tokensValueToCreate == null)
            throw new RuntimeException("Token value to create must be not null");

        // Create the root token
        final Optional<Token> root = this.save(tokensValueToCreate[0]);

        // Run the others token and add then
        for (final String tokenToCreate : tokensValueToCreate) {
            this.save(root.orElseThrow().getValue(), tokenToCreate);
        }

        return this.findTokenByValue(root.orElseThrow().getValue());
    }


    /**
     * @param tokenValueToFind   String
     * @param tokenValueToCreate String
     */
    private void save(final String tokenValueToFind, final String tokenValueToCreate) {

        if (tokenValueToFind == null)
            throw new RuntimeException("Token value to find must be not null");

        // Verify if the token to create alaredy exists
        this.findTokenByValue(tokenValueToCreate).ifPresentOrElse(iToken -> {
            LOGGER.warn("Token with value: {} already found", iToken.getValue());
        }, () -> findTokenByValue(tokenValueToFind).ifPresentOrElse(iToken -> {
            final String name = extractNameFromToken(tokenValueToCreate);
            iToken.add(new TokenImpl(tokenValueToCreate, name));
        }, () -> {
            save(tokenValueToFind);
            save(tokenValueToFind, tokenValueToCreate);
        }));

        findTokenByValue(tokenValueToCreate);
    }

    /**
     * @param tokenValueToCreate String
     * @return IToken
     */
    private Optional<Token> save(final String tokenValueToCreate) {

        final String name = extractNameFromToken(tokenValueToCreate);
        this.findTokenByValue(tokenValueToCreate)
                .ifPresentOrElse(iToken -> LOGGER.warn(("Token already exists")), () -> tokens.add(new TokenImpl(tokenValueToCreate, name)));

        return this.findTokenByValue(tokenValueToCreate);
    }

    /**
     * @param token String
     * @return String
     */
    public String extractNameFromToken(final String token) {
        if (token != null)
            try {
                return jwtAccessTokenConverter.extractAuthentication(jwtAccessTokenConverter.decode(token)).getName();
            } catch (final Exception ignored) {
            }
        return token;
    }

    /**
     * @param tokenValue String
     * @return Optional<IToken>
     */
    public Optional<Token> findTokenByValue(final String tokenValue) {

        for (final Token token : this.tokens) {
            final Optional<Token> found = token.findByValue(tokenValue);
            if (found.isPresent())
                return found;
        }

        return Optional.empty();
    }


    /**
     * @param name String
     * @return Optional<IToken>
     */
    @Override
    public Set<Token> listTokensByName(final String name) {
        return this.tokens.stream().filter(iToken ->
                iToken.getName() != null && iToken.getName().equals(name)
        ).collect(Collectors.toSet());
    }

    /**
     * @param tokenValue String
     */
    @Override
    public void revoke(final String tokenValue) {
        final Optional<Token> token = findTokenByValue(tokenValue);

        token.ifPresentOrElse(Token::revoke, () -> {
            throw new RuntimeException("Token with value: " + tokenValue + " not found");
        });

    }

    /**
     * Must be coverred with tests
     *
     * @param tokenValue String
     */
    @Override
    public void remove(final String tokenValue) {
        this.findTokenByValue(tokenValue).ifPresent(this.tokens::remove);
    }

// Is the following code is Legacy

    /**
     * @param token OAuth2AccessToken
     * @return OAuth2Authentication
     */
    @Override
    public OAuth2Authentication readAuthentication(final OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    /**
     * @param token String
     * @return OAuth2Authentication
     */
    @Override
    public OAuth2Authentication readAuthentication(final String token) {
        return jwtAccessTokenConverter.extractAuthentication(jwtAccessTokenConverter.decode(token));
    }

    /**
     * It is only for authorization code. Only the grant type authorization code store the tokens
     *
     * @param oAuth2AccessToken OAuth2RefreshToken
     * @param authentication    OAuth2Authentication
     */
    @Override
    public void storeAccessToken(final OAuth2AccessToken oAuth2AccessToken, final OAuth2Authentication authentication) {
        storeToken(authentication, oAuth2AccessToken.getValue());
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuth2AccessToken accessToken = convertAccessToken(tokenValue);
        if (jwtAccessTokenConverter.isRefreshToken(accessToken)) {
            throw new InvalidTokenException("Encoded token is a refresh token");
        }

        return accessToken;
    }

    private OAuth2AccessToken convertAccessToken(final String tokenValue) {
        return jwtAccessTokenConverter.extractAccessToken(tokenValue, jwtAccessTokenConverter.decode(tokenValue));
    }

    // TODO?????
    @Override
    public void removeAccessToken(final OAuth2AccessToken oAuth2AccessToken) {
        revoke(oAuth2AccessToken.getValue());
    }

    /**
     * It is only for authorization code. Only the grant type authorization code store the tokens
     *
     * @param oAuth2RefreshToken OAuth2RefreshToken
     * @param authentication     OAuth2Authentication
     */
    @Override
    public void storeRefreshToken(final OAuth2RefreshToken oAuth2RefreshToken, final OAuth2Authentication authentication) {
        storeToken(authentication, oAuth2RefreshToken.getValue());
    }

    /**
     * @param authentication     OAuth2Authentication
     * @param tokenValueToCreate String
     */
    private void storeToken(final OAuth2Authentication authentication, final String tokenValueToCreate) {
        extractSessionID(authentication).ifPresentOrElse(root -> {
            this.save(root, tokenValueToCreate);
        }, () -> {
// TDOO
        });
    }

    /**
     * @param tokenValue String
     * @return OAuth2RefreshToken
     */
    @Override
    public OAuth2RefreshToken readRefreshToken(final String tokenValue) {
        final OAuth2AccessToken encodedRefreshToken = convertAccessToken(tokenValue);
        return createRefreshToken(encodedRefreshToken);
    }

    private OAuth2RefreshToken createRefreshToken(OAuth2AccessToken encodedRefreshToken) {
        if (!jwtAccessTokenConverter.isRefreshToken(encodedRefreshToken)) {
            throw new InvalidTokenException("Encoded token is not a refresh token");
        }
        if (encodedRefreshToken.getExpiration() != null) {
            return new DefaultExpiringOAuth2RefreshToken(encodedRefreshToken.getValue(),
                    encodedRefreshToken.getExpiration());
        }
        return new DefaultOAuth2RefreshToken(encodedRefreshToken.getValue());
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return readAuthentication(token.getValue());
    }

    @Override
    public void removeRefreshToken(final OAuth2RefreshToken oAuth2RefreshToken) {
        revoke(oAuth2RefreshToken.getValue());
    }

    /**
     * @param oAuth2RefreshToken OAuth2RefreshToken
     */
    @Override
    public void removeAccessTokenUsingRefreshToken(final OAuth2RefreshToken oAuth2RefreshToken) {
        revoke(oAuth2RefreshToken.getValue());
    }

    // TODO Must be refactored
    @Override
    public OAuth2AccessToken getAccessToken(final OAuth2Authentication authentication) {
        if (authentication.getOAuth2Request().getGrantType() != null && authentication.getOAuth2Request().getGrantType().equals(GrantType.AUTHORIZATION_CODE.getValue()))
            if (authentication.getUserAuthentication() != null && authentication.getUserAuthentication().getDetails() != null)
                if (authentication.getUserAuthentication().getDetails() instanceof WebAuthenticationDetails)
                    if (((WebAuthenticationDetails) authentication.getUserAuthentication().getDetails()).getSessionId() != null) {
                        final String root = ((WebAuthenticationDetails) authentication.getUserAuthentication().getDetails()).getSessionId();
                        final Optional<Token> toReturn = findTokenByValue(root);
                        if (toReturn.isPresent()) {
                            final OAuth2AccessToken accessToken = readAccessToken(toReturn.map(iToken -> iToken.getLeaf().orElseThrow().getPrevious().orElseThrow()).map(Token::getValue).orElse(null));
                            if (accessToken.getRefreshToken() != null)
                                return accessToken;
                            else {
                                final OAuth2RefreshToken refreshToken = readRefreshToken(toReturn.map(iToken -> iToken.getLeaf().orElseThrow()).map(Token::getValue).orElse(null));
                                final DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
                                token.setRefreshToken(refreshToken);
                                return token;
                            }
                        }
                    }

        return null; //TODO
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(final String clientId, final String userName) {
        return Collections.emptySet(); //TODO
    }

    // TODO must be refactored
    // todo verify necessity
    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(final String clientId) {
        return null;
    }


    /**
     * @param authentication OAuth2Authentication
     * @return Optional<String>
     */
    public static Optional<String> extractSessionID(final OAuth2Authentication authentication) {
        if (authentication.getUserAuthentication() != null && authentication.getUserAuthentication().getDetails() != null)
            if (authentication.getUserAuthentication().getDetails() instanceof WebAuthenticationDetails)
                return Optional.ofNullable(((WebAuthenticationDetails) authentication.getUserAuthentication().getDetails()).getSessionId());
        return Optional.empty();
    }
}
