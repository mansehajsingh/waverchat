package com.waverchat.api.v1.authentication.session;

import com.waverchat.api.v1.exceptions.NotFoundException;
import com.waverchat.api.v1.resources.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public Session getById(Long id) throws NotFoundException {
        Optional<Session> result = this.sessionRepository.findById(id);

        if (result.isPresent()) {
            return result.get();
        } else {
            throw new NotFoundException("No session with this id.");
        }
    }

    public Session createSession(Session session) {
        return this.sessionRepository.save(session);
    }

    public void deleteSessionById(Long id) {
        this.sessionRepository.deleteById(id);
    }

    public void deleteSessionsByUser(User user) {
        this.sessionRepository.deleteByUser(user);
    }

}
