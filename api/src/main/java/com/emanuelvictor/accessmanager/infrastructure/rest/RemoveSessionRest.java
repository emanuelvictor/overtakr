package com.emanuelvictor.accessmanager.infrastructure.rest;

import com.emanuelvictor.accessmanager.application.usecases.RemoveSessionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("api/access-manager/sessions")
public class RemoveSessionRest {

    private final RemoveSessionUseCase removeSessionUseCase;

    @DeleteMapping("{sid}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('root.access-manager.sessions.delete','root.access-manager.sessions','root.access-manager','root')")
    public ResponseEntity<Void> remove(@PathVariable String sid) {
        removeSessionUseCase.execute(sid);
        return ResponseEntity.ok().build();
    }
}