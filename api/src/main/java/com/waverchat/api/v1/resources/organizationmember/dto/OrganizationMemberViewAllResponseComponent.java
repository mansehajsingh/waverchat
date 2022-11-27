package com.waverchat.api.v1.resources.organizationmember.dto;

import com.waverchat.api.v1.customframework.dto.ViewAllResponseComponent;
import com.waverchat.api.v1.resources.organizationmember.OrganizationMembershipType;
import com.waverchat.api.v1.resources.user.dto.UserViewResponse;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class OrganizationMemberViewAllResponseComponent extends ViewAllResponseComponent {

    private UUID id;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private UUID organizationId;

    private OrganizationMembershipType type;

    private UserViewResponse user;

}
