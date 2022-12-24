package com.waverchat.api.v1.framework;

import com.waverchat.api.v1.exceptions.ValidationException;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;

@MappedSuperclass
@Data
public abstract class AbstractEntity {

    @Id
    @GeneratedValue
    protected Long id;

    @CreationTimestamp
    protected ZonedDateTime createdAt;

    @UpdateTimestamp
    protected ZonedDateTime updatedAt;

    public void validateForCreate() throws ValidationException {}

    public void validateForUpdate() throws ValidationException {}

}
