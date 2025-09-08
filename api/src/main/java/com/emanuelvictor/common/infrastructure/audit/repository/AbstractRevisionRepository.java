package com.emanuelvictor.common.infrastructure.audit.repository;

import com.emanuelvictor.common.infrastructure.aid.DAOUtil;
import com.emanuelvictor.common.infrastructure.audit.Revision;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

/**
 * AbstractService to converting audit objects to legible objects
 *
 * @param <T>
 */
public abstract class AbstractRevisionRepository<T> implements IRevisionRepository<T> {

    /**
     *
     */
    private AuditReader reader;

    /**
     *
     */
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /**
     *
     */
    private final Class<T> clazz = (Class<T>) DAOUtil.getTypeArguments(AbstractRevisionRepository.class, this.getClass()).get(0);

    /**
     *
     */
    @PostConstruct
    public void postConstruct() {
        reader = AuditReaderFactory.get(Objects.requireNonNull(entityManagerFactory.createEntityManager()));
    }

    /**
     * @param id Long
     * @return Revisions<Long, BRPhysicPerson>
     */
    public List<Object> findRevisionsById(final UUID id) {
        final List<Object> returnList = new ArrayList<>();
        final AuditQuery query = this.reader.createQuery().forRevisionsOfEntityWithChanges(clazz, true).add(AuditEntity.id().eq(id));
        final List<Object[]> results = query.getResultList();
        for (final Object[] result : results) {
            final T object = (T) result[0];
            final Revision revEntity = (Revision) result[1];
            final RevisionType revType = (RevisionType) result[2];
            final Set<String> properties = (Set<String>) result[3];

            final RevisionDTO<T> revisionDTO = new RevisionDTO<>();
            revisionDTO.setRevisionId((long) revEntity.getId());
            revisionDTO.setDateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(revEntity.getTimestamp()), TimeZone.getDefault().toZoneId()));
            revisionDTO.setUsername(revEntity.getUsername());
            revisionDTO.setType(revType);
            revisionDTO.setChangedProps(String.join(",", properties));
            revisionDTO.setEntity(object);

            returnList.add(revisionDTO);
        }

        return returnList;
    }

    public List<Object> findRevisions() {
        final List<Object> returnList = new ArrayList<>();
        final AuditQuery query = this.reader.createQuery().forRevisionsOfEntityWithChanges(clazz, true);
        final List<Object[]> results = query.getResultList();
        for (final Object[] result : results) {
            final T object = (T) result[0];
            final Revision revEntity = (Revision) result[1];
            final RevisionType revType = (RevisionType) result[2];
            final Set<String> properties = (Set<String>) result[3];

            final RevisionDTO<T> revisionDTO = new RevisionDTO<>();
            revisionDTO.setRevisionId(revEntity.getId());
            revisionDTO.setDateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(revEntity.getTimestamp()), TimeZone.getDefault().toZoneId()));
            revisionDTO.setUsername(revEntity.getUsername());
            revisionDTO.setType(revType);
            revisionDTO.setChangedProps(String.join(",", properties));
            revisionDTO.setEntity(object);

            returnList.add(revisionDTO);
        }

        return returnList;
    }


    /**
     * @param id Long
     * @return Revisions<Long, BRPhysicPerson>
     */
    public Page<T> findRevisionsById(final Long id, final Pageable pageable) {
        throw new NotImplementedException("You're probably the first to need it, so implement it for us...");
    }


}
