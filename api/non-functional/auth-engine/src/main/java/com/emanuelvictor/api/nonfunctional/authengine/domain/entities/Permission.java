package com.emanuelvictor.api.nonfunctional.authengine.domain.entities;

import com.emanuelvictor.api.nonfunctional.authengine.domain.entities.generic.PersistentEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;  // TODO acoplamento
import com.fasterxml.jackson.annotation.JsonProperty;  // TODO acoplamento
import com.fasterxml.jackson.annotation.ObjectIdGenerators;  // TODO acoplamento
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;  // TODO acoplamento

import java.util.Set;


/**
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 1.0.0, 10/09/2019
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIdentityInfo(
        property = "id",
        scope = Permission.class,
        generator = ObjectIdGenerators.PropertyGenerator.class
)
public class Permission extends PersistentEntity implements GrantedAuthority {

    /**
     *
     */
    private String authority;

    /**
     *
     */
    @JsonProperty
    private Permission upperPermission;

    /**
     *
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Permission> lowerPermissions;

    /**
     *
     */
    public Permission() {
    }

    /**
     * @param authority        String
     * @param upperPermission  Permission
     * @param lowerPermissions Set<Permission>
     */
    public Permission(final String authority,
                      final Permission upperPermission,
                      final Set<Permission> lowerPermissions) {
        this.authority = authority;
        this.upperPermission = upperPermission;
        this.lowerPermissions = lowerPermissions;
    }

    /**
     * @return Permission
     */
    public Permission copy() {
        return new Permission(authority, upperPermission, lowerPermissions);
    }
}
