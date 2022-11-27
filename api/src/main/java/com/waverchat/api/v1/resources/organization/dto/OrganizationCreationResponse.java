package com.waverchat.api.v1.resources.organization.dto;

import com.waverchat.api.v1.customframework.dto.CreationResponse;
import com.waverchat.api.v1.resources.user.dto.UserViewResponse;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class OrganizationCreationResponse extends CreationResponse {

    private UUID id;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private String name;

    private String description;

    private UserViewResponse owner;

}
