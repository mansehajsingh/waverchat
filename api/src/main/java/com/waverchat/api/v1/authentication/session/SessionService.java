package com.waverchat.api.v1.authentication.session;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public Session createSession(Session session) {
        Session createdSession = sessionRepository.save(session);
        return createdSession;
    }

}
