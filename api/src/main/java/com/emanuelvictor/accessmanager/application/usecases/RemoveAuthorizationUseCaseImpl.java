package com.emanuelvictor.accessmanager.application.usecases;

import com.emanuelvictor.accessmanager.domain.gateways.AuthorizationRepository;
import com.emanuelvictor.common.infrastructure.authorization.ResourceServerNotifier;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveAuthorizationUseCaseImpl implements RemoveAuthorizationUseCase {

    private final SessionRepository<?> sessionRepository;
    private final ResourceServerNotifier resourceServerNotifier;
    private final AuthorizationRepository authorizationRepository;
    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    @Override
    public void execute(String token) {
        final var authorization = authorizationRepository.findByToken(token).orElseThrow();
        sessionRepository.deleteById(authorization.getSid());
        final var oAuth2Authorization = extractOAuth2AuthorizationFromToken(authorization.getAccessTokenValue());
        oAuth2AuthorizationService.remove(oAuth2Authorization);
        resourceServerNotifier.revoke(authorization.getAccessTokenValue());
    }

    private OAuth2Authorization extractOAuth2AuthorizationFromToken(String token) {
        var authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.REFRESH_TOKEN);
        if (authorization == null)
            authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        return authorization;
    }
}
