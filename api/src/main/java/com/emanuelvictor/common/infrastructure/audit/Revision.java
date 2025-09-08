package com.emanuelvictor.common.infrastructure.audit;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import java.io.Serial;
import java.io.Serializable;


/**
 * @param <T>
 * @param <ID>
 */
@Data
@Entity
@lombok.EqualsAndHashCode
@Table(name = Revision.TABLE_NAME)
@RevisionEntity(EntityTrackingRevisionListener.class)
public class Revision<T extends IEntity<ID>, ID extends Serializable> implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 4193623660483050410L;

    /**
     *
     */
    public static final String TABLE_NAME = "REVISION";

    /**
     * id da {@link Revision}
     */
    @Id
    @RevisionNumber
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    /**
     * data da {@link Revision}
     */
    @RevisionTimestamp
    private long timestamp;

    /**
     * Username of the logged user {@link Revision}
     */
    private String username;


}
