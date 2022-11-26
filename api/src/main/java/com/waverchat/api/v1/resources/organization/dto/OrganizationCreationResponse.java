package com.waverchat.api.v1.resources.organization.dto;

import com.waverchat.api.v1.customframework.dto.CreationResponse;
import lombok.Data;

import java.util.UUID;

@Data
public class OrganizationCreationResponse extends CreationResponse {

    private UUID id;

    private String name;

    private String description;

}
