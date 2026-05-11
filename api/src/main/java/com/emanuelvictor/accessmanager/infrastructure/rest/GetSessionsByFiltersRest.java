package com.emanuelvictor.accessmanager.infrastructure.rest;

import br.org.itaipuparquetec.common.infrastructure.rest.PageResponse;
import com.emanuelvictor.accessmanager.infrastructure.jpa.entities.SessionJPA;
import com.emanuelvictor.accessmanager.infrastructure.jpa.repository.SessionJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/access-manager/sessions")
public class GetSessionsByFiltersRest {

    private final SessionJPARepository sessionJPARepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('root.access-manager.sessions.read','root.access-manager.sessions','root.access-manager','root')")
    public PageResponse<SessionResponse> getSessionsByFiltersRest(final String principalName, final Pageable pageable) {
        final var sessionResponsePage = sessionJPARepository.findByPrincipalName(principalName, pageable)
                .map(sessionJPA -> new SessionResponse(sessionJPA.getId(), sessionJPA.getPrincipalName(),
                        sessionJPA.getSid()));
        return new PageResponse<>(sessionResponsePage);
    }

    public record SessionResponse(String id, String principalName, String sid) {
    }
}