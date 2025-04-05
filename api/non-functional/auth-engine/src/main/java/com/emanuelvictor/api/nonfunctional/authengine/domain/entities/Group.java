package com.emanuelvictor.api.nonfunctional.authengine.domain.entities;

import com.emanuelvictor.api.nonfunctional.authengine.domain.entities.generic.PersistentEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;  // TODO acoplamento
import com.fasterxml.jackson.annotation.ObjectIdGenerators;  // TODO acoplamento
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;


/**
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 1.0.0, 10/09/2019
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
//@JsonIdentityInfo(
//        property = "id",
//        scope = Group.class,
//        generator = ObjectIdGenerators.PropertyGenerator.class
//)
public class Group extends PersistentEntity {

    /**
     *
     */
    private String name;

    /**
     * TODO remove
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<GroupPermission> groupPermissions;

}
