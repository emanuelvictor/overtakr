package com.emanuelvictor.api.functional.accessmanager.application.resource;

import com.emanuelvictor.api.functional.accessmanager.application.feign.ITokenFeignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("tokens")
public class TokenResource {

    private final TokenStore tokenStore;
    private final ITokenFeignRepository tokenFeignRepository;

//    /**
//     * todo deve ter preauthorize
//            todo NÃO HÁ MAIS NECESSIDADE
//     */
//    @DeleteMapping("{token}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void delete(@PathVariable final String token) {
//        // Black list
//        ((MyJwtTokenStore) this.tokenStore).revoke(token);
//    }

    /**
     * todo deve ter preauthorize
     */
    @GetMapping("{name}")
    @ResponseStatus(HttpStatus.OK)
    public Set<Object> findTokenByName(@PathVariable final String name) {
        return tokenFeignRepository.findTokenByName(name);
    }

    /*Dedicated to tests of the scope of the application. Client Credentials tests*/

    /**
     * @return ResponseEntity<String>
     */
    @GetMapping("must-return-403")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("#oauth2.hasScope('root.access-manager.sessions.read')")
    public ResponseEntity<String> mustReturn403() {
        return tokenFeignRepository.mustReturn403();
    }

    /**
     * To test of the access
     *
     * @return StringBuffer
     */
    @GetMapping("must-return-200")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> mustReturn200() {
        return tokenFeignRepository.mustReturn200();
    }

}
