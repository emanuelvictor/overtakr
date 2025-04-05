package com.emanuelvictor.api.functional.accessmanager.application.feign;

import org.springframework.cloud.openfeign.FeignClient; // TODO acoplamento
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping; // TODO acoplamento
import org.springframework.web.bind.annotation.GetMapping; // TODO acoplamento
import org.springframework.web.bind.annotation.PathVariable; // TODO acoplamento

import java.util.Set;

/**
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 1.0.0, 10/09/2019
 */
@FeignClient(name = "tokens", url = "${oauth.endpoints.tokens}")
public interface ITokenFeignRepository {

    /**
     * @param name String
     * @return Set<String>
     */
    @GetMapping("{name}")
    Set<Object> findTokenByName(@PathVariable("name") final String name);

    /**
     * @param token String
     */
    @DeleteMapping("{token}")
    void revoke(@PathVariable("token") final String token);

    /*Dedicated to tests of the scope of the application. Client Credentials tests*/

    /**
     *
     * @return ResponseEntity<String>
     */
    @GetMapping("must-return-403")
    ResponseEntity<String> mustReturn403();

    /**
     * @return ResponseEntity<String>
     */
    @GetMapping("must-return-200")
    ResponseEntity<String> mustReturn200();
}
