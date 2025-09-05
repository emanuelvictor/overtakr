package com.emanuelvictor.accessmanager.infrastructure.beans;

import com.emanuelvictor.accessmanager.infrastructure.jpa.repository.GroupPermissionRepository;
import com.emanuelvictor.accessmanager.infrastructure.jpa.repository.GroupRepository;
import com.emanuelvictor.accessmanager.infrastructure.jpa.repository.PermissionRepository;
import com.emanuelvictor.accessmanager.domain.services.LinkPermissionToGroupService;
import com.emanuelvictor.accessmanager.domain.services.UnlinkPermissionToGroupService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccessManagerBeans {

    @Bean
    LinkPermissionToGroupService linkPermissionToGroupService(final GroupRepository groupRepository,
                                                              final PermissionRepository permissionRepository,
                                                              final GroupPermissionRepository groupPermissionRepository) {
        return new LinkPermissionToGroupService(groupRepository, permissionRepository, groupPermissionRepository);
    }

    @Bean
    UnlinkPermissionToGroupService unlinkPermissionToGroupService(final PermissionRepository permissionRepository,
                                                                  final GroupPermissionRepository groupPermissionRepository,
                                                                  final LinkPermissionToGroupService linkPermissionToGroupService) {
        return new UnlinkPermissionToGroupService(permissionRepository, groupPermissionRepository, linkPermissionToGroupService);
    }
}
