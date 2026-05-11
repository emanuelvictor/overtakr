package com.emanuelvictor.accessmanager.infrastructure.jpa.repository;

import com.emanuelvictor.accessmanager.infrastructure.jpa.entities.SessionJPA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionJPARepository extends JpaRepository<SessionJPA, String> {

    Page<SessionJPA> findByPrincipalName(final String principalName, final Pageable pageable);

    Page<SessionJPA> findSessionJPABySid(String sid, Pageable pageable);

}
