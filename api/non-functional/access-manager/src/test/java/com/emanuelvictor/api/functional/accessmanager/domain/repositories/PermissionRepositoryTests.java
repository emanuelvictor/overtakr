package com.emanuelvictor.api.functional.accessmanager.domain.repositories;

import com.emanuelvictor.api.functional.accessmanager.AbstractIntegrationTests;
import com.emanuelvictor.api.functional.accessmanager.domain.entities.Permission;
import com.emanuelvictor.api.functional.accessmanager.domain.entity.PermissionBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class PermissionRepositoryTests extends AbstractIntegrationTests {

    @Autowired
    private PermissionRepository permissionRepository;

    @Test
    public void mustFindPermissionById() {
        final var permission = new PermissionBuilder()
                .authority(UUID.randomUUID().toString())
                .name(UUID.randomUUID().toString())
                .description(UUID.randomUUID().toString())
                .build();

        final var permissionSaved = permissionRepository.save(permission);

        Assertions.assertThat(permissionSaved).isEqualTo(permission);
    }

}
