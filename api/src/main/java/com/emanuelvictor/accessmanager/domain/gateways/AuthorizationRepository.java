package com.emanuelvictor.accessmanager.domain.gateways;

import com.emanuelvictor.accessmanager.domain.model.Authorization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AuthorizationRepository {

    void removeByToken(String token);

    Page<Authorization> listByUsername(String username, Pageable pageable);

    Optional<Authorization> findByToken(String token);

}
