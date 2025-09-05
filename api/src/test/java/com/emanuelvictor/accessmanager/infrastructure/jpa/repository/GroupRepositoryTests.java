package com.emanuelvictor.accessmanager.infrastructure.jpa.repository;

import com.emanuelvictor.SpringBootTests;
import com.emanuelvictor.accessmanager.infrastructure.jpa.repository.GroupRepository;
import com.emanuelvictor.accessmanager.domain.model.Group;
import com.emanuelvictor.accessmanager.domain.model.GroupBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class GroupRepositoryTests extends SpringBootTests {

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
