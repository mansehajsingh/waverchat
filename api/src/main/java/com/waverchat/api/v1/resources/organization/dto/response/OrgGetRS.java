package com.waverchat.api.v1.resources.organization.dto.response;

import com.waverchat.api.v1.framework.DtoRS;
import com.waverchat.api.v1.resources.organization.entity.Organization;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrgGetRS implements DtoRS<Organization> {

    private Long id;

    private String name;

    private String description;

    public static OrgGetRS from(Organization organization) {
        return new OrgGetRS(organization.getId(), organization.getName(), organization.getDescription());
    }

}
