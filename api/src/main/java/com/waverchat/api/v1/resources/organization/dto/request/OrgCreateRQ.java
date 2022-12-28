package com.waverchat.api.v1.resources.organization.dto.request;

import com.waverchat.api.v1.framework.DtoRQ;
import com.waverchat.api.v1.resources.organization.entity.Organization;
import lombok.Data;

@Data
public class OrgCreateRQ implements DtoRQ<Organization> {

    private String name;

    private String description;

    @Override
    public Organization toEntity() {
        return new Organization(name, description);
    }
}
