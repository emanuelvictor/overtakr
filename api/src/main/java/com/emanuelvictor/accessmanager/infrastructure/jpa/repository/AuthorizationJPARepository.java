package com.emanuelvictor.accessmanager.infrastructure.jpa.repository;

import com.emanuelvictor.accessmanager.infrastructure.jpa.entities.AuthorizationJPA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizationJPARepository extends JpaRepository<AuthorizationJPA, String> {

    Page<AuthorizationJPA> findByPrincipalName(final String principalName, final Pageable pageable);

    Optional<AuthorizationJPA> findAuthorizationJPASByAccessTokenValue(String accessToken);

    Optional<AuthorizationJPA> findAuthorizationJPASByRefreshTokenValue(String refreshToken);

}
