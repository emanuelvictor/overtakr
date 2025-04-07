package com.emanuelvictor.api.nonfunctional.authengine.application.services;

import com.emanuelvictor.api.nonfunctional.authengine.application.feign.repositories.IAccessGroupPermissionFeignRepository;
import com.emanuelvictor.api.nonfunctional.authengine.application.feign.repositories.IUserFeignRepository;
import com.emanuelvictor.api.nonfunctional.authengine.domain.entities.GroupPermission;
import com.emanuelvictor.api.nonfunctional.authengine.domain.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 2.0.0, 04/01/2020
 */
@Component
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final IUserFeignRepository userFeignRepository;
    private final IAccessGroupPermissionFeignRepository accessGroupPermissionFeignRepository;

    /**
     * @param username String
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userFeignRepository.loadUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not founded!"));
        final Set<GroupPermission> groupPermissions = new HashSet<>(accessGroupPermissionFeignRepository.findAccessGroupPermissionsByGroupId(user.getGroup().getId(), PageRequest.of(0, 100)).getContent());
        user.getGroup().setGroupPermissions(groupPermissions);
        return user;
    }
}
