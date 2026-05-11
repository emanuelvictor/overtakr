package com.emanuelvictor.accessmanager.domain.gateways;

import com.emanuelvictor.accessmanager.domain.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SessionRepository {

    Page<Session> findAllBySid(String sid, Pageable pageable);

    void removeAllBySid(String sid);

    Session findByTokenValue(String token);

    void removeById(String id);

}
