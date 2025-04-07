package com.emanuelvictor.api.nonfunctional.authengine.application.resource;

import com.emanuelvictor.api.nonfunctional.authengine.application.services.token.entities.Token;
import com.emanuelvictor.api.nonfunctional.authengine.application.services.token.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("tokens")
public class TokenResource {

    /**
     *
     */
    private final TokenService tokenService;

    /**
     * @param token String
     */
    @DeleteMapping("{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('root.access-manager.sessions.delete', 'root.access-manager.sessions', 'root.access-manager', 'root')")
    public void delete(@PathVariable final String token) {
        tokenService.revokeToken(token);
    }

    /**
     * @param name String
     * @return Set<IToken>
     */
    @GetMapping("{name}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('root.access-manager.sessions.get', 'root.access-manager.sessions', 'root.access-manager', 'root')")
    public Set<Token> findTokenByName(@PathVariable final String name) {
        return tokenService.listTokensByName(name);
    }

    /*Dedicated to tests of the scope of the application. Client Credentials tests*/

    /**
     * @return ResponseEntity<String>
     */
    @GetMapping("must-return-403")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("#oauth2.hasScope('root.access-manager.sessions.get')")
    @PreAuthorize("hasAnyAuthority('asdf')")
    public ResponseEntity<String> mustReturn403() {
        return ResponseEntity.ok("Oh no! Should did not have access :(");
    }

    /**
     * @return ResponseEntity<String>
     */
    @GetMapping("must-return-200")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('root.access-manager.sessions.get', 'root.access-manager.sessions', 'root.access-manager', 'root')")
    public ResponseEntity<String> mustReturn200() {
        return ResponseEntity.ok("Yeah baby! We should did have access ;)");
    }
}
