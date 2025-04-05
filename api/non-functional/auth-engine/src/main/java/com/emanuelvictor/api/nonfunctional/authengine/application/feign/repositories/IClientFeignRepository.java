package com.emanuelvictor.api.nonfunctional.authengine.application.feign.repositories;

import com.emanuelvictor.api.nonfunctional.authengine.domain.entities.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 1.0.0, 10/09/2019
 */
@FeignClient(name = "applications", url = "${oauth.endpoints.applications}")
public interface IClientFeignRepository {

    /**
     * @param clientId String
     * @return Optional<Client>
     */
    @GetMapping("{clientId}/load")
    Optional<Client> loadClientByClientId(@PathVariable("clientId") final String clientId);

    /**
     * @param clientId String
     * @return Optional<Boolean>
     */
    @GetMapping("{clientId}/notify")
    Optional<Boolean> notify(@PathVariable("clientId") final String clientId);
}
