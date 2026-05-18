package com.emanuelvictor.accessmanager.infrastructure.rest;

import br.org.itaipuparquetec.common.infrastructure.rest.PageResponse;
import com.emanuelvictor.accessmanager.infrastructure.jpa.repository.AuthorizationJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/access-manager/authorizations")
public class GetSessionsByFiltersRest {

    private final AuthorizationJPARepository authorizationJPARepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('root.access-manager.sessions.read','root.access-manager.sessions','root.access-manager','root')")
    public PageResponse<AuthorizationResponse> getSessionsByFiltersRest(final String principalName, final Pageable pageable) {
        final var authorizationResponsePage = authorizationJPARepository.findByPrincipalName(principalName, pageable)
                .map(authorizationJPA -> new AuthorizationResponse(
                        authorizationJPA.getId(), authorizationJPA.getPrincipalName(),
                        authorizationJPA.getSid(), authorizationJPA.getAccessTokenValue()
                ));
        return new PageResponse<>(authorizationResponsePage);
    }

    public record AuthorizationResponse(String id, String principalName, String sid, String token) {
    }
}