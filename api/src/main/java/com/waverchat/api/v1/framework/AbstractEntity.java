package com.waverchat.api.v1.framework;

import com.waverchat.api.v1.exceptions.ValidationException;
import com.waverchat.api.v1.resources.user.entity.User;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;

@MappedSuperclass
@Data
public abstract class AbstractEntity<E extends AbstractEntity> {

    @Id
    @GeneratedValue
    protected Long id;

    @CreationTimestamp
    protected ZonedDateTime createdAt;

    @UpdateTimestamp
    protected ZonedDateTime updatedAt;

    public E clone() {
        return null;
    }

    public E cloneFromDiff(E diff) {
        return null;
    }

    public void validateForCreate() throws ValidationException {}

    public void validateForUpdate() throws ValidationException {}

}
