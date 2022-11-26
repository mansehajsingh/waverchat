package com.waverchat.api.v1.resources.organization;

import com.waverchat.api.v1.customframework.AbstractApplicationResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationResource extends AbstractApplicationResource<Organization, OrganizationService, OrganizationResponseFactory> {
}
