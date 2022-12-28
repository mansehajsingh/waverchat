package com.waverchat.api.v1.resources.organization.service;

import com.waverchat.api.v1.exceptions.ConflictException;
import com.waverchat.api.v1.exceptions.NotFoundException;
import com.waverchat.api.v1.exceptions.ValidationException;
import com.waverchat.api.v1.framework.BaseService;
import com.waverchat.api.v1.resources.organization.OrganizationRepository;
import com.waverchat.api.v1.resources.organization.entity.Organization;
import com.waverchat.api.v1.resources.organizationmember.OrganizationMemberRepository;
import com.waverchat.api.v1.resources.organizationmember.entity.OrganizationMember;
import com.waverchat.api.v1.resources.organizationmember.entity.OrganizationMembershipType;
import com.waverchat.api.v1.resources.user.UserRepository;
import com.waverchat.api.v1.resources.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrganizationService extends BaseService<Organization, OrganizationRepository> {

    @Autowired
    private OrganizationMemberRepository organizationMemberRepository;

    @Autowired
    private UserRepository userRepository;

    public void auditForCreate(Organization organization, Long ownerUserId) throws NotFoundException {
        Optional<User> userOpt = this.userRepository.findByIdAndDeleted(ownerUserId, false);

        if (userOpt.isEmpty())
            throw new NotFoundException("No undeleted user exists with the provided user id for organization owner.");
    }

    @Transactional
    public Organization create(Organization organization, Long ownerUserId) throws ValidationException, ConflictException, NotFoundException {
        organization.validateForCreate();

        this.auditForCreate(organization, ownerUserId);

        User owner = new User() {{ setId(ownerUserId); }};

        Organization createdOrg = this.repository.save(organization);

        OrganizationMember ownership = new OrganizationMember(owner, createdOrg, OrganizationMembershipType.OWNER);

        this.organizationMemberRepository.save(ownership);

        return createdOrg;
    }
}
