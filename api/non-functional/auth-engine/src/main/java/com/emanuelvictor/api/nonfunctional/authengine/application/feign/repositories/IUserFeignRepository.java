package com.emanuelvictor.api.nonfunctional.authengine.application.feign.repositories;

import com.emanuelvictor.api.nonfunctional.authengine.domain.entities.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 1.0.0, 10/09/2019
 */
@FeignClient(name = "users", url = "${oauth.endpoints.users}")
public interface IUserFeignRepository {

    /**
     * @param username String
     * @return User
     */
    @GetMapping("{username}/username") //TODO// /username?
    Optional<User> loadUserByUsername(@PathVariable("username") final String username);

}
