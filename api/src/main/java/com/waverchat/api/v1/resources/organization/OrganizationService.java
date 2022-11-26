package com.waverchat.api.v1.resources.organization;

import com.waverchat.api.v1.customframework.AbstractApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizationService extends AbstractApplicationService<Organization> {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public Optional<Organization> create(Organization entityToCreate) {
        return Optional.of(this.organizationRepository.save(entityToCreate));
    }



}
