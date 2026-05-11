package com.emanuelvictor.accessmanager.infrastructure.jpa;

import com.emanuelvictor.accessmanager.domain.gateways.SessionRepository;
import com.emanuelvictor.accessmanager.domain.model.Session;
import com.emanuelvictor.accessmanager.infrastructure.jpa.repository.SessionJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionRepositoryImpl implements SessionRepository {

    private final SessionJPARepository sessionJPARepository;

    @Override
    public Page<Session> findAllBySid(String sid, Pageable pageable) {
        return sessionJPARepository.findSessionJPABySid(sid, pageable)
                .map(sessionJPA -> Session.rehydrate(
                        sessionJPA.getId(), sessionJPA.getPrincipalName(), sessionJPA.getSid(), sessionJPA.getAccessTokenValue()));
    }

    @Override
    public void removeAllBySid(String sid) {
        var sessions = sessionJPARepository.findSessionJPABySid(sid, Pageable.unpaged()).getContent();
        sessionJPARepository.deleteAll(sessions);
    }

    @Override
    public Session findByTokenValue(String id) {
        return sessionJPARepository.findById(id).map(sessionJPA ->
                Session.rehydrate(sessionJPA.getId(), sessionJPA.getPrincipalName(), sessionJPA.getSid(),
                        sessionJPA.getAccessTokenValue())).orElseThrow();
    }

    @Override
    public void removeById(String id) {
        sessionJPARepository.deleteById(id);
    }

}
