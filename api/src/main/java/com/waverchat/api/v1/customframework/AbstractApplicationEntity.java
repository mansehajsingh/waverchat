package com.waverchat.api.v1.customframework;

import com.waverchat.api.v1.exceptions.ValidationException;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@MappedSuperclass
@Data
public abstract class AbstractApplicationEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    public void edit(RequestProperties props) {}

    public void validateForCreate() throws ValidationException {}

    public void validateForEdit() throws ValidationException {}

}
