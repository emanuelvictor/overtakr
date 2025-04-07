package com.emanuelvictor.api.nonfunctional.authengine.application.services.token.services;

import com.emanuelvictor.api.nonfunctional.authengine.application.services.token.entities.Token;
import com.emanuelvictor.api.nonfunctional.authengine.application.services.token.repositories.JwtAccessTokenConverter;
import com.emanuelvictor.api.nonfunctional.authengine.application.services.token.repositories.TokenRepository;
import com.emanuelvictor.api.nonfunctional.authengine.domain.entities.GrantType;
import com.emanuelvictor.api.nonfunctional.authengine.application.services.token.repositories.TokenRepositoryImpl;
import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.session.MapSessionRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Base implementation for token services using random UUID values for the access token and refresh token values. The
 * main extension point for customizations is the {@link TokenEnhancer} which will be called after the access and
 * refresh tokens have been generated but before they are stored.
 * <p>
 * Persistence is delegated to a {@code TokenStore} implementation and customization of the access token to a
 * {@link TokenEnhancer}.
 *
 * @author Ryan Heaton
 * @author Luke Taylor
 * @author Dave Syer
 */
@Service
public class TokenServiceImpl implements TokenService {

    private static final String SEPARATOR = ".";
    private static final int REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 30; // default 30 days.
    private static final int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 12; // default 12 hours.

    private final RedisClient redisClient;
    private final ClientDetailsService clientDetailsService;
    private final TokenRepository tokenRepository;
    private final JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * @param clientDetailsService ClientDetailsService
     * @param tokenRepository      TokenRepository
     */
    public TokenServiceImpl(final RedisClient redisClient,
                            final ClientDetailsService clientDetailsService,
                            final List<TokenRepository> tokenRepository, final JwtAccessTokenConverter jwtAccessTokenConverter) {
        this.redisClient = redisClient;
        this.clientDetailsService = clientDetailsService;
        this.tokenRepository = tokenRepository.get(0);
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
    }

    /**
     * @param authentication OAuth2Authentication
     * @return OAuth2AccessToken
     * @throws AuthenticationException
     */
    @Override
    public OAuth2AccessToken createAccessToken(final OAuth2Authentication authentication) throws AuthenticationException {

        return TokenRepositoryImpl.extractSessionID(authentication)
                .flatMap(sessionId -> this.tokenRepository.findTokenByValue(sessionId).map(iToken ->
                        // Is a authentication in the browser navigator, and Jessionid was found in repository
                {

                    // Token is revoked, that is the logout has effected
                    if (iToken.isRevoked())
                        throw new RuntimeException("Token revoked");

                    // Create the access token with refresh token with the new valid timouts
                    final DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(readAccessToken(iToken.getAccess().orElseThrow().getValue()));
                    final int refreshTokenValiditySeconds = getRefreshTokenValiditySeconds(authentication.getOAuth2Request());
                    if (refreshTokenValiditySeconds > 0) {
                        final DefaultExpiringOAuth2RefreshToken defaultExpiringOAuth2RefreshToken = new DefaultExpiringOAuth2RefreshToken(iToken.getRefresh().orElseThrow().getValue(),
                                new Date(iToken.getRefresh().orElseThrow().getCreatedOn().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + (refreshTokenValiditySeconds * 1000L)));
                        defaultOAuth2AccessToken.setRefreshToken(defaultExpiringOAuth2RefreshToken);
                    }

                    // Return the access token if it was found and is not expired
                    if (defaultOAuth2AccessToken.isExpired()) {
                        // Create access token with refresh token associated
                        final OAuth2AccessToken accessToken = createAccessTokenWithRefreshToken(authentication);
                        // Save de root (or jsessionid), access token and refresh token
                        tokenRepository.save(sessionId, accessToken.getValue(), accessToken.getRefreshToken().getValue()).orElseThrow().printFromRoot();
                        return accessToken;
                    } else {
                        return defaultOAuth2AccessToken;
                    }

                    // If the authentication is a web authentication in the browser. And is not found the jsessionid in repository
                    // It must be created.
                }).or(() -> {

                    // Create access token with refresh token associated
                    final OAuth2AccessToken accessToken = createAccessTokenWithRefreshToken(authentication);
                    // Save de root (or jsessionid), access token and refresh token
                    tokenRepository.save(sessionId, accessToken.getValue(), accessToken.getRefreshToken().getValue()).orElseThrow().printFromRoot();
                    return Optional.of((DefaultOAuth2AccessToken) accessToken);

                    // If the request is not a authorization code, or that is no have jsessionid.
                    // Is not a Web Authentication with browser.
                })).or(() -> {

                    // Client Credentials Grant Type MUST NOT implement refresh token (https://tools.ietf.org/html/rfc6749#section-4.4.3).
                    // So it not must be replace the old access token already saved
                    if (authentication.getOAuth2Request().getGrantType().equals(GrantType.CLIENT_CREDENTIALS.getValue())) {
                        // Remove the old access token associated to the clientId
                        tokenRepository.remove(authentication.getOAuth2Request().getClientId());

                        // Create a access token without refresh, because it is note necessary
                        final OAuth2AccessToken accessToken = createAccessTokenWithoutRefreshToken(authentication);

                        // Save the new access token associated to the client Id
                        tokenRepository.save(authentication.getOAuth2Request().getClientId(), accessToken.getValue()).orElseThrow().printFromRoot();
                        return Optional.of((DefaultOAuth2AccessToken) accessToken);
                    }

                    // If the grant type is different from Client Credentials, generated the access token with refresh token. Follows the flow
                    // Create access token with refresh token associated.
                    final OAuth2AccessToken accessToken = createAccessTokenWithRefreshToken(authentication);

                    // Save access token and refresh token
                    tokenRepository.save(accessToken.getValue(), accessToken.getRefreshToken().getValue()).orElseThrow().printFromRoot();
                    return Optional.of((DefaultOAuth2AccessToken) accessToken);

                }).orElseThrow();

    }

    /**
     * @param refreshTokenValue String
     * @param tokenRequest      TokenRequest
     * @return OAuth2AccessToken
     * @throws AuthenticationException
     */
    @Override
    public OAuth2AccessToken refreshAccessToken(final String refreshTokenValue, final TokenRequest tokenRequest) throws AuthenticationException {

        final Token token = tokenRepository.findTokenByValue(refreshTokenValue).orElseThrow();

        final OAuth2Authentication authentication = tokenRepository.readAuthentication(token.getAccess().orElseThrow().getValue());
//        authentication = createRefreshedAuthentication(authentication, tokenRequest);
        final DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(createAccessTokenWithRefreshToken(authentication));
        final int refreshTokenValiditySeconds = getRefreshTokenValiditySeconds(authentication.getOAuth2Request());
        if (refreshTokenValiditySeconds > 0) {
            final DefaultExpiringOAuth2RefreshToken defaultExpiringOAuth2RefreshToken = new DefaultExpiringOAuth2RefreshToken(defaultOAuth2AccessToken.getRefreshToken().getValue(),
                    new Date(token.getRefresh().orElseThrow().getCreatedOn().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + (refreshTokenValiditySeconds * 1000L)));
            defaultOAuth2AccessToken.setRefreshToken(defaultExpiringOAuth2RefreshToken);
        }

        final String clientId = authentication.getOAuth2Request().getClientId();
        if (clientId == null || !clientId.equals(tokenRequest.getClientId())) {
            throw new InvalidGrantException("Wrong client for this refresh token: " + refreshTokenValue);
        }

        if (token.isRevoked())
            throw new InvalidTokenException("Invalid refresh token (revoked): " + token.getAccess().orElseThrow().getValue());

        if (isExpired(defaultOAuth2AccessToken.getRefreshToken()))
            throw new InvalidTokenException("Invalid refresh token (expired): " + defaultOAuth2AccessToken.getRefreshToken());

        if (tokenRepository.findTokenByValue(defaultOAuth2AccessToken.getValue()).isPresent() || tokenRepository.findTokenByValue(defaultOAuth2AccessToken.getRefreshToken().getValue()).isPresent())
            throw new RuntimeException("The access token and refresh token is already saved");

        // Save de root (or jsessionid), access token and refresh token
        tokenRepository.save(refreshTokenValue, defaultOAuth2AccessToken.getValue(), defaultOAuth2AccessToken.getRefreshToken().getValue()).orElseThrow().printFromRoot();
        return defaultOAuth2AccessToken;
    }

    /**
     * @param authentication OAuth2Authentication
     * @return OAuth2AccessToken
     */
    @Override
    public OAuth2AccessToken getAccessToken(final OAuth2Authentication authentication) {
        return tokenRepository.getAccessToken(authentication);
    }

    /**
     * @param accessToken String
     * @return OAuth2AccessToken
     */
    @Override
    public OAuth2AccessToken readAccessToken(final String accessToken) {
        return tokenRepository.readAccessToken(accessToken);
    }

    /**
     * @param accessTokenValue String
     * @return OAuth2Authentication
     * @throws AuthenticationException
     * @throws InvalidTokenException
     */
    @Override
    public OAuth2Authentication loadAuthentication(final String accessTokenValue) throws AuthenticationException, InvalidTokenException {

        final OAuth2AccessToken accessToken = tokenRepository.readAccessToken(accessTokenValue);
        if (accessToken == null) {
            throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
        } else if (accessToken.isExpired()) {
            tokenRepository.removeAccessToken(accessToken);
            throw new InvalidTokenException("Access token expired: " + accessTokenValue);
        }

        final OAuth2Authentication result = tokenRepository.readAuthentication(accessToken);
        if (result == null) {
            // in case of race condition
            throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
        }
        if (clientDetailsService != null) {
            final String clientId = result.getOAuth2Request().getClientId();
            try {
                clientDetailsService.loadClientByClientId(clientId);
            } catch (final ClientRegistrationException e) {
                throw new InvalidTokenException("Client not valid: " + clientId, e);
            }
        }
        return result;
    }

    /**
     * TODO utilize correct injection. Without @Autowired
     */
    @Autowired
    private MapSessionRepository sessionRepository;

    /**
     * @param tokenValue String
     * @return boolean
     */
    @Override
    public boolean revokeToken(final String tokenValue) {

        final Optional<Token> token = tokenRepository.findTokenByValue(tokenValue);

        // Removing jsessionId
        sessionRepository.deleteById(token.orElseThrow().getRoot().orElseThrow().getValue());

        // Revoke the token in this application
        tokenRepository.revoke(token.orElseThrow().getValue());

        token.ifPresent(iToken -> iToken.getAll().forEach(innerToken -> {

            final StatefulRedisPubSubConnection<String, String> connection = redisClient.connectPubSub();
            final RedisPubSubAsyncCommands<String, String> async = connection.async();
            async.publish("revoke-token-redis-channel", innerToken.getValue());

        }));

        return true;
    }

    /**
     * @param name String
     * @return Set<String>
     */
    @Override
    public Set<Token> listTokensByName(final String name) {
        return tokenRepository.listTokensByName(name);
    }

    /**
     * @param grantedAuthorities Collection<? extends GrantedAuthority>
     * @return Set<String>
     */
    public static Set<String> extractClientsId(final Collection<? extends GrantedAuthority> grantedAuthorities) {
        Objects.requireNonNull(grantedAuthorities);
        return grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.contains(SEPARATOR) ? authority.substring(authority.indexOf(SEPARATOR) + 1) : authority).map(authority -> authority.contains(SEPARATOR) ? authority.substring(0, authority.indexOf(SEPARATOR)) : authority)
                .filter(authority -> !authority.equals("root"))
                .collect(Collectors.toSet());
    }

    /**
     * @param oAuth2Authentication OAuth2Authentication
     * @return Set<String>
     */
    private static Set<String> extractClientsId(final OAuth2Authentication oAuth2Authentication) {
        Objects.requireNonNull(oAuth2Authentication);
        return extractClientsId(oAuth2Authentication.getUserAuthentication());
    }

    /**
     * @param authentication Authentication
     * @return Set<String>
     */
    private static Set<String> extractClientsId(final Authentication authentication) {
        Objects.requireNonNull(authentication);
        return extractClientsId(authentication.getAuthorities());
    }

    /**
     * @param authentication OAuth2Authentication
     * @return OAuth2AccessToken
     */
    private OAuth2AccessToken createAccessTokenWithRefreshToken(final OAuth2Authentication authentication) {
        final DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
        int validitySeconds = getAccessTokenValiditySeconds(authentication.getOAuth2Request());
        if (validitySeconds > 0) {
            token.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
        }
        token.setRefreshToken(createRefreshToken(authentication));
        token.setScope(authentication.getOAuth2Request().getScope());

        return jwtAccessTokenConverter != null ? jwtAccessTokenConverter.enhance(token, authentication) : token;
    }

    /**
     * @param authentication OAuth2Authentication
     * @return OAuth2AccessToken
     */
    private OAuth2AccessToken createAccessTokenWithoutRefreshToken(final OAuth2Authentication authentication) {
        final DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
        int validitySeconds = getAccessTokenValiditySeconds(authentication.getOAuth2Request());
        if (validitySeconds > 0) {
            token.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
        }
        token.setScope(authentication.getOAuth2Request().getScope());

        return jwtAccessTokenConverter != null ? jwtAccessTokenConverter.enhance(token, authentication) : token;
    }

    /**
     * @param authentication OAuth2Authentication
     * @return OAuth2RefreshToken
     */
    private OAuth2RefreshToken createRefreshToken(final OAuth2Authentication authentication) {
        int validitySeconds = getRefreshTokenValiditySeconds(authentication.getOAuth2Request());
        final String value = UUID.randomUUID().toString();
        if (validitySeconds > 0) {
            return new DefaultExpiringOAuth2RefreshToken(value, new Date(System.currentTimeMillis()
                    + (validitySeconds * 1000L)));
        }
        return new DefaultOAuth2RefreshToken(value);
    }

    /**
     * The access token validity period in seconds
     *
     * @param clientAuth the current authorization request
     * @return the access token validity period in seconds
     */
    private int getAccessTokenValiditySeconds(OAuth2Request clientAuth) {
        if (clientDetailsService != null) {
            ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            Integer validity = client.getAccessTokenValiditySeconds();
            if (validity != null) {
                return validity;
            }
        }
        return ACCESS_TOKEN_VALIDITY_SECONDS;
    }

    /**
     * The refresh token validity period in seconds
     *
     * @param clientAuth the current authorization request
     * @return the refresh token validity period in seconds
     */
    private int getRefreshTokenValiditySeconds(OAuth2Request clientAuth) {
        if (clientDetailsService != null) {
            ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            Integer validity = client.getRefreshTokenValiditySeconds();
            if (validity != null) {
                return validity;
            }
        }
        return REFRESH_TOKEN_VALIDITY_SECONDS;
    }

    /**
     * @param refreshToken OAuth2RefreshToken
     * @return boolean
     */
    private boolean isExpired(OAuth2RefreshToken refreshToken) {
        if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
            ExpiringOAuth2RefreshToken expiringToken = (ExpiringOAuth2RefreshToken) refreshToken;
            return expiringToken.getExpiration() == null
                    || System.currentTimeMillis() > expiringToken.getExpiration().getTime();
        }
        return false;
    }

    /**
     * Create a refreshed authentication.
     *
     * @param authentication The authentication.
     * @param request        The scope for the refreshed token.
     * @return The refreshed authentication.
     * @throws InvalidScopeException If the scope requested is invalid or wider than the original scope.
     */
    private OAuth2Authentication createRefreshedAuthentication(OAuth2Authentication authentication, TokenRequest request) {
        final Set<String> scope = request.getScope();
        OAuth2Request clientAuth = authentication.getOAuth2Request().refresh(request);
        if (scope != null && !scope.isEmpty()) {
            Set<String> originalScope = clientAuth.getScope();
            if (originalScope == null || !originalScope.containsAll(scope)) {
                throw new InvalidScopeException("Unable to narrow the scope of the client authentication to " + scope
                        + ".", originalScope);
            } else {
                clientAuth = clientAuth.narrowScope(scope);
            }
        }
        return new OAuth2Authentication(clientAuth, authentication.getUserAuthentication());
    }

    /**
     * @param authentication
     * @param refreshToken
     * @return
     */
    private OAuth2AccessToken createAccessToken(OAuth2Authentication authentication, OAuth2RefreshToken refreshToken) {
        final DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
        int validitySeconds = getAccessTokenValiditySeconds(authentication.getOAuth2Request());
        if (validitySeconds > 0) {
            token.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
        }
        token.setRefreshToken(refreshToken);
        token.setScope(authentication.getOAuth2Request().getScope());

        return jwtAccessTokenConverter != null ? jwtAccessTokenConverter.enhance(token, authentication) : token;
    }

}
