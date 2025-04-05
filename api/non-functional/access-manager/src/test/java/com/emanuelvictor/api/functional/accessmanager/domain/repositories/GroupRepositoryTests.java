package com.emanuelvictor.api.functional.accessmanager.domain.repositories;

import com.emanuelvictor.api.functional.accessmanager.AbstractIntegrationTests;
import com.emanuelvictor.api.functional.accessmanager.domain.entities.Group;
import com.emanuelvictor.api.functional.accessmanager.domain.entity.GroupBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Arrays;

public class GroupRepositoryTests extends AbstractIntegrationTests {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    public void mustFindGroupById() {
        final var groupName = "Access Group Name";
        final var group = new GroupBuilder().name(groupName).build();
        groupRepository.save(group);
        Assertions.assertThat(group.getId()).isNotNull();

        final var groupSaved = groupRepository.findById(group.getId()).orElseThrow();

        Assertions.assertThat(groupSaved.getName()).isEqualTo(groupName);
    }

    @Test
    public void mustListGroupsByFilters() {
        final var firstGroupName = "First Group";
        final var firstGroup = new GroupBuilder().name(firstGroupName).build();
        final var secondGroupName = "Second Group";
        final var secondGroup = new GroupBuilder().name(secondGroupName).build();
        groupRepository.saveAll(Arrays.asList(firstGroup, secondGroup));

        final var filteredGroups = groupRepository.listByFilters(firstGroupName, null);

        Assertions.assertThat(filteredGroups.getContent()).extracting(Group::getName).containsAnyOf(firstGroupName);
    }
}
