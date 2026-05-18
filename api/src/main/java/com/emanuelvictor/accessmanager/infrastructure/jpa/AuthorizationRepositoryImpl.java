package com.emanuelvictor.accessmanager.infrastructure.jpa;

import com.emanuelvictor.accessmanager.domain.gateways.AuthorizationRepository;
import com.emanuelvictor.accessmanager.domain.model.Authorization;
import com.emanuelvictor.accessmanager.infrastructure.jpa.entities.AuthorizationJPA;
import com.emanuelvictor.accessmanager.infrastructure.jpa.repository.AuthorizationJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorizationRepositoryImpl implements AuthorizationRepository {

    private final AuthorizationJPARepository authorizationJPARepository;

    @Override
    public Page<Authorization> listByUsername(String username, Pageable pageable) {
        return authorizationJPARepository.findByPrincipalName(username, pageable)
                .map(authorizationJPA -> Authorization.rehydrate(
                        authorizationJPA.getId(), authorizationJPA.getPrincipalName(), authorizationJPA.getSid(),
                        authorizationJPA.getAccessTokenValue()));
    }

    @Override
    public Optional<Authorization> findByToken(String token) {
        final Optional<AuthorizationJPA> authorizationJPA = authorizationJPARepository.findAuthorizationJPASByAccessTokenValue(token);
        if (authorizationJPA.isEmpty())
            return convertToAuthorization(authorizationJPARepository.findAuthorizationJPASByRefreshTokenValue(token));
        return convertToAuthorization(authorizationJPA);
    }

    @Override
    public void removeByToken(String token) {
        final Optional<AuthorizationJPA> authorizationJPA = authorizationJPARepository.findAuthorizationJPASByAccessTokenValue(token);
        if (authorizationJPA.isEmpty())
            authorizationJPARepository.delete(authorizationJPARepository.findAuthorizationJPASByRefreshTokenValue(token).orElseThrow());
        authorizationJPARepository.delete(authorizationJPA.orElseThrow());
    }

    private static Optional<Authorization> convertToAuthorization(Optional<AuthorizationJPA> authorizationJPA) {
        return authorizationJPA.map(it -> Authorization.rehydrate(it.getId(), it.getPrincipalName(),
                it.getSid(), it.getAccessTokenValue()));
    }

}
