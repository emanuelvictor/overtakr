package com.emanuelvictor.api.functional.accessmanager.domain.entity;

import com.emanuelvictor.api.functional.accessmanager.domain.entities.Group;
import com.emanuelvictor.api.functional.accessmanager.domain.entities.Permission;

import java.util.UUID;

public final class GroupBuilder {

    private Long id;
    private String name = UUID.randomUUID().toString();

    public Group build() {
        return new Group(id, name);
    }

    public GroupBuilder id(final Long id) {
        this.id = id;
        return this;
    }

    public GroupBuilder name(final String name) {
        this.name = name;
        return this;
    }

}
