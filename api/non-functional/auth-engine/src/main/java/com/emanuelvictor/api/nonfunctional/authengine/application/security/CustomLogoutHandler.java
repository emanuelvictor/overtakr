package com.emanuelvictor.api.nonfunctional.authengine.application.security;

import com.emanuelvictor.api.nonfunctional.authengine.application.services.token.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    /**
     *
     */
    private final TokenService tokenService;

    /**
     * @param httpServletRequest  HttpServletRequest
     * @param httpServletResponse HttpServletResponse
     * @param authentication      Authentication
     */
    @Override
    public void logout(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Authentication authentication) {
        if (authentication != null && authentication.getDetails() != null && authentication.getDetails() instanceof WebAuthenticationDetails)
            if (((WebAuthenticationDetails) authentication.getDetails()).getSessionId() != null)
                tokenService.revokeToken(((WebAuthenticationDetails) authentication.getDetails()).getSessionId());
    }
}
