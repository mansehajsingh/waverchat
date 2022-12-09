package com.waverchat.api.v1.resources.organizationinvitation.dto;

import com.waverchat.api.v1.customframework.dto.CreationResponse;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class OrganizationInvitationCreationResponse extends CreationResponse {

    private UUID id;

    private UUID organizationId;

    private UUID userId;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

}
