package com.emanuelvictor.api.nonfunctional.authengine.application.feign.repositories;

import com.emanuelvictor.api.nonfunctional.authengine.domain.entities.GroupPermission;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "access-group-permissions", url = "${oauth.endpoints.access-group-permissions}")
public interface IAccessGroupPermissionFeignRepository {

    /**
     * @param groupId Long
     * @return Optional<Client>
     */
    @GetMapping
    Page<GroupPermission> findAccessGroupPermissionsByGroupId(@RequestParam final long groupId, Pageable pageable);
}
