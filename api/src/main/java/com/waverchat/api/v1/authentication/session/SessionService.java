package com.waverchat.api.v1.authentication.session;

import com.waverchat.api.v1.exceptions.ResourceNotFoundException;
import com.waverchat.api.v1.applicationresource.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public Session getById(UUID id) throws ResourceNotFoundException {
        Optional<Session> result = this.sessionRepository.findById(id);

        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ResourceNotFoundException("No session with this id.");
        }
    }

    public Session createSession(Session session) {
        return this.sessionRepository.save(session);
    }

    public void deleteSessionById(UUID id) {
        this.sessionRepository.deleteById(id);
    }

    public void deleteSessionsByUser(User user) {
        this.sessionRepository.deleteByUser(user);
    }

}
