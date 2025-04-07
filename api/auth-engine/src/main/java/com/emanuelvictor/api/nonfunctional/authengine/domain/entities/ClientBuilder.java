package com.emanuelvictor.api.nonfunctional.authengine.domain.entities;

import org.springframework.security.core.GrantedAuthority;  // TODO acoplamento
import org.springframework.security.oauth2.provider.ClientDetails;  // TODO acoplamento

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class ClientBuilder {

    private String clientId;

    private String clientSecret;

    private final boolean autoApprove = true;

    private boolean secretRequired = false;

    private boolean scoped = false;

    private int accessTokenValiditySeconds = 60;

    private int refreshTokenValiditySeconds = 999999999;

    private final Set<String> authorizedGrantTypes = new LinkedHashSet<>();

    private final Set<String> scope = new LinkedHashSet<>();

    private final Set<String> registeredRedirectUris = new HashSet<>();

    /**
     *
     */
    public ClientBuilder() {
    }

    /**
     * @param clientId
     * @return
     */
    public ClientBuilder withClientId(final String clientId) {
        this.clientId = clientId;
        return this;
    }

    /**
     * @return ClientDetails
     */
    public ClientBuilder withClientSecret(final String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    /**
     * @param authorizedGrantTypes
     * @return
     */
    public ClientBuilder withAuthorizedGrantTypes(final String... authorizedGrantTypes) {
        this.authorizedGrantTypes.addAll(Arrays.asList(authorizedGrantTypes));
        return this;
    }

    /**
     * @param registeredRedirectUris
     * @return
     */
    public ClientBuilder withRedirectUris(final String... registeredRedirectUris) {
        this.registeredRedirectUris.addAll(Arrays.asList(registeredRedirectUris));
        return this;
    }

    /**
     * @param scope
     * @return
     */
    public ClientBuilder withScope(final String... scope) {
        this.scope.addAll(Arrays.asList(scope));
        return this;
    }

    /**
     * @param secretRequired
     * @return
     */
    public ClientBuilder withSecretRequired(final boolean secretRequired) {
        this.secretRequired = secretRequired;
        return this;
    }

    /**
     * @param scoped
     * @return
     */
    public ClientBuilder withScoped(final boolean scoped) {
        this.scoped = scoped;
        return this;
    }

    /**
     * @param accessTokenValiditySeconds
     * @return
     */
    public ClientBuilder withAccessTokenValiditySeconds(final int accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        return this;
    }

    /**
     * @param refreshTokenValiditySeconds
     * @return
     */
    public ClientBuilder withRefreshTokenValiditySeconds(final int refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        return this;
    }

    /**
     * @param scope
     * @return
     */
    public ClientBuilder withIsAutoApprove(final String scope) {
        return this;
    }

    public ClientDetails build() {
        return new ClientDetails() {
            @Override
            public String getClientId() {
                return clientId;
            }

            @Override
            public Set<String> getResourceIds() {
                return null;
            }

            @Override
            public boolean isSecretRequired() {
                return secretRequired;
            }

            @Override
            public String getClientSecret() {
                return clientSecret;
            }

            @Override
            public boolean isScoped() {
                return scoped;
            }

            @Override
            public Set<String> getScope() {
                return scope;
            }

            @Override
            public Set<String> getAuthorizedGrantTypes() {
                return authorizedGrantTypes;
            }

            @Override
            public Set<String> getRegisteredRedirectUri() {
                return registeredRedirectUris;
            }

            @Override
            public Collection<GrantedAuthority> getAuthorities() {
                return scope.stream().map(s -> (GrantedAuthority) () -> s).collect(Collectors.toSet());
            }

            @Override
            public Integer getAccessTokenValiditySeconds() {
                return accessTokenValiditySeconds;
            }

            @Override
            public Integer getRefreshTokenValiditySeconds() {
                return refreshTokenValiditySeconds;
            }

            @Override
            public boolean isAutoApprove(final String scope) {
                return autoApprove;
            }

            @Override
            public Map<String, Object> getAdditionalInformation() {
                return null;
            }
        };
    }

}
