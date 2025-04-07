package com.emanuelvictor.api.nonfunctional.authengine.application.services;

import com.emanuelvictor.api.nonfunctional.authengine.domain.entities.*;
import com.emanuelvictor.api.nonfunctional.authengine.application.feign.repositories.IAccessGroupPermissionFeignRepository;
import com.emanuelvictor.api.nonfunctional.authengine.application.feign.repositories.IClientFeignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 2.0.0, 04/01/2020
 */
@Service
@RequiredArgsConstructor
public class ClientService implements ClientDetailsService {

    private static final String[] ALL_GRANT_TYPES = new String[]{GrantType.AUTHORIZATION_CODE.getValue(), GrantType.CLIENT_CREDENTIALS.getValue(), GrantType.IMPLICIT.getValue(), GrantType.PASSWORD.getValue(), GrantType.REFRESH_TOKEN.getValue(), "implicit"};

    private final PasswordEncoder passwordEncoder;
    private final IClientFeignRepository clientFeignRepository;
    private final IAccessGroupPermissionFeignRepository accessGroupPermissionFeignRepository;
    private final org.springframework.core.env.Environment env;

    /**
     * @param clientId String
     * @return ClientDetails
     * @throws ClientRegistrationException
     */
    @Override
    public ClientDetails loadClientByClientId(final String clientId) throws ClientRegistrationException {

        if (clientId.equals(env.getProperty("oauth.clientId"))) {
            return new ClientBuilder()
                    .withClientId(env.getProperty("oauth.clientId"))
                    .withRedirectUris("http://localhost:8081/login"/*, "http://localhost:8080/access-manager/api/logged"*/)
                    .withScoped(false)
                    .withClientSecret(passwordEncoder.encode(env.getProperty("oauth.clientSecret")))
                    .withScope("root")
                    .withSecretRequired(true)
                    .withAuthorizedGrantTypes(ALL_GRANT_TYPES)
                    .build();
        }

        final Client client = clientFeignRepository.loadClientByClientId(clientId)
                .orElseThrow(() -> new UsernameNotFoundException("ClientId " + clientId + " não localizado!"));
        final Set<GroupPermission> groupPermissions = new HashSet<>(accessGroupPermissionFeignRepository.findAccessGroupPermissionsByGroupId(client.getGroup().getId(), PageRequest.of(0, 100)).getContent());
        client.getGroup().setGroupPermissions(groupPermissions);
        return client;

    }

}
