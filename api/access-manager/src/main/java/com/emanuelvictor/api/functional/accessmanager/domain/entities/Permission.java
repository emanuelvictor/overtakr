package com.emanuelvictor.api.functional.accessmanager.domain.entities;

import com.emanuelvictor.api.functional.accessmanager.domain.entities.generic.EntityIdResolver;
import com.emanuelvictor.api.functional.accessmanager.domain.entities.generic.PersistentEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;


/**
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 1.0.0, 10/09/2019
 */
@Entity
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, // TODO ACOPLAMENTO
        property = "id",
        scope = Permission.class,
        resolver = EntityIdResolver.class)
public class Permission extends PersistentEntity {

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false, unique = true)
    private String authority;

    private String description;

    @JsonProperty
    @ManyToOne(fetch = FetchType.EAGER)
    private Permission upperPermission;

    public Permission() {
    }

    public Permission(Long id, String name, String authority, String description, Permission upperPermission) {
        super(id);
        this.name = name;
        this.authority = authority;
        this.description = description;
        this.upperPermission = upperPermission;
    }
}
