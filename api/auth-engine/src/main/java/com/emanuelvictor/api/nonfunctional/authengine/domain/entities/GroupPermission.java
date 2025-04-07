package com.emanuelvictor.api.nonfunctional.authengine.domain.entities;

import com.emanuelvictor.api.nonfunctional.authengine.domain.entities.generic.PersistentEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 1.0.0, 10/09/2019
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GroupPermission extends PersistentEntity {

    /**
     *
     */
    private Permission permission;

    /**
     *
     */
    private Group group;

    /**
     * @param permission Permissao
     */
    public GroupPermission(final Permission permission) {
        this.permission = permission;
    }
}
