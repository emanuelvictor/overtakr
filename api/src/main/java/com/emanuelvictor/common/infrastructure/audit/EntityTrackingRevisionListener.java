package com.emanuelvictor.common.infrastructure.audit;

import org.hibernate.envers.RevisionType;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @version 1.0
 */
public class EntityTrackingRevisionListener implements org.hibernate.envers.EntityTrackingRevisionListener {

    @Override
    public void newRevision(final Object revisionEntity) {
        final String username = SecurityContextHolder.getContext() == null ? null : (SecurityContextHolder.getContext().getAuthentication() == null ? null : SecurityContextHolder.getContext().getAuthentication().getName().toUpperCase());
        ((Revision<?, ?>) revisionEntity).setUsername(username);
    }

    @Override
    public void entityChanged(Class entityClass, String entityName, Object entityId, RevisionType revisionType, Object revisionEntity) {
        final String username = SecurityContextHolder.getContext() == null ? null : (SecurityContextHolder.getContext().getAuthentication() == null ? null : SecurityContextHolder.getContext().getAuthentication().getName().toUpperCase());
        ((Revision<?, ?>) revisionEntity).setUsername(username);
    }
}
