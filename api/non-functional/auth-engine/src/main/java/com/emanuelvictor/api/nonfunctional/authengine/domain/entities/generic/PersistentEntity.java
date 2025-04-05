package com.emanuelvictor.api.nonfunctional.authengine.domain.entities.generic;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 2.0.0, 01/01/2020
 */
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public abstract class PersistentEntity implements IPersistentEntity<Long> {

    @Getter
    @Setter
    protected Long id;

    @Getter
    @Setter
    protected LocalDateTime createdOn;

    @Getter
    @Setter
    protected LocalDateTime updatedOn;

    /**
     * @param id Long
     */
    public PersistentEntity(final Long id) {
        this.id = id;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public boolean isSaved() {
        return this.id != null && this.id != 0;
    }
}
