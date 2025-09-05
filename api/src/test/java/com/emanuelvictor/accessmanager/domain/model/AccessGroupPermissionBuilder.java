package com.emanuelvictor.accessmanager.domain.model;


public class AccessGroupPermissionBuilder {

    private Permission permission;
    private Group group;

    public AccessGroupPermissionBuilder() {
        this.permission = new PermissionBuilder().build();
        this.group = new GroupBuilder().build();
    }

    public AccessGroupPermissionBuilder permission(Permission permission) {
        this.permission = permission;
        return this;
    }

    public AccessGroupPermissionBuilder group(Group group) {
        this.group = group;
        return this;
    }

    public GroupPermission build() {
        return new GroupPermission(permission, group);
    }
}
