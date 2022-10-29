package com.waverchat.api.v1.customframework;

import com.waverchat.api.v1.exceptions.ConflictException;

import java.util.Optional;
import java.util.UUID;

public abstract class AbstractApplicationService<E extends AbstractApplicationEntity> {

    public void auditForCreate(E entityToCreate) throws ConflictException {}

    public Optional<E> create(E entityToCreate) {
        return Optional.empty();
    };

    public Optional<E> getById(UUID id) {
        return Optional.empty();
    }

}
