package com.waverchat.api.v1.resources.organizationmember;

import com.waverchat.api.v1.customframework.AbstractApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationMemberService extends AbstractApplicationService<OrganizationMember> {

    @Autowired
    private OrganizationMemberRepository organizationMemberRepository;

    @Override
    public Optional<OrganizationMember> getById(UUID id) {
        return this.organizationMemberRepository.findById(id);
    }

    @Override
    public Optional<OrganizationMember> create(OrganizationMember entityToCreate) {
        return Optional.of(this.organizationMemberRepository.save(entityToCreate));
    }
}
