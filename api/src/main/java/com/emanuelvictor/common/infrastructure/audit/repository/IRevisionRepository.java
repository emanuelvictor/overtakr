package com.emanuelvictor.common.infrastructure.audit.repository;

import java.util.List;
import java.util.UUID;

public interface IRevisionRepository<T> {

    List<Object> findRevisionsById(final UUID id);

    List<Object> findRevisions();

}
