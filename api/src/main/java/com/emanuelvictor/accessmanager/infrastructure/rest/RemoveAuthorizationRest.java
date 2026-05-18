package com.emanuelvictor.accessmanager.infrastructure.rest;

import com.emanuelvictor.accessmanager.application.usecases.RemoveAuthorizationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("api/access-manager/authorizations")
public class RemoveAuthorizationRest {

    private final RemoveAuthorizationUseCase removeAuthorizationUseCase;

    @DeleteMapping("{token}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('root.access-manager.authorizations.delete','root.access-manager.authorizations','root.access-manager','root')")
    public ResponseEntity<Void> remove(@PathVariable String token) {
        removeAuthorizationUseCase.execute(token);
        return ResponseEntity.ok().build();
    }
}