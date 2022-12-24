package com.waverchat.api.v1.framework;

public interface DtoRQ<E extends AbstractEntity> {

    E toEntity();

}
