package com.waverchat.api.v1;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@MappedSuperclass
@Data
public class ApplicationEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;

}
