package com.emanuelvictor.accessmanager.application.usecases;

import com.emanuelvictor.accessmanager.domain.gateways.SessionRepository;
import com.emanuelvictor.accessmanager.domain.model.Session;
import com.emanuelvictor.common.infrastructure.authorization.ResourceServerNotifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveSessionUseCaseImpl implements RemoveSessionUseCase {

    private final SessionRepository sessionRepository;
    private final ResourceServerNotifier resourceServerNotifier;
    private final OAuth2AuthorizationService authorizationService;

    @Override
    public void execute(String sid) {
        final var sessions = sessionRepository.findAllBySid(sid, null);
        revokeSessions(sessions);
        sessionRepository.removeAllBySid(sid);
    }

    private void revokeSessions(Page<Session> sessions) {
        sessions.forEach(session -> {
            var authorization = extractAuthorizationFromToken(session.getAccessTokenValue());
            revokeAuthorization(authorization);
        });
    }

    private OAuth2Authorization extractAuthorizationFromToken(String token) {
        var authorization = authorizationService.findByToken(token, OAuth2TokenType.REFRESH_TOKEN);
        if (authorization == null)
            authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        return authorization;
    }

    private void revokeAuthorization(OAuth2Authorization authorization) {
        authorizationService.remove(authorization);
        final var accessToken = authorization.getAccessToken();
        if (accessToken != null && accessToken.getClaims() != null) {
            final var sid = (String) accessToken.getClaims().get("sid");
            if (sid != null) {
                resourceServerNotifier.revoke(sid);
            }
        }
    }
}
