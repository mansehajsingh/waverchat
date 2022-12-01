package com.waverchat.api.v1.resources.organization;

import com.waverchat.api.v1.customframework.AbstractApplicationService;
import com.waverchat.api.v1.customframework.RQRSLifecycleProperties;
import com.waverchat.api.v1.resources.organizationmember.OrganizationMember;
import com.waverchat.api.v1.resources.organizationmember.OrganizationMemberRepository;
import com.waverchat.api.v1.resources.organizationmember.OrganizationMembershipType;
import com.waverchat.api.v1.resources.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrganizationService extends AbstractApplicationService<Organization> {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMemberRepository organizationMemberRepository;

    @Override
    public Optional<Organization> create(Organization entityToCreate, RQRSLifecycleProperties props) {
        return createOrgAndOwner(entityToCreate, props);
    }

    @Transactional
    private Optional<Organization> createOrgAndOwner(Organization entityToCreate, RQRSLifecycleProperties props) {
        Organization createdOrg = this.organizationRepository.save(entityToCreate);

        User owner = new User();
        owner.setId(props.getRequestingUserId());

        OrganizationMember ownership = new OrganizationMember(owner, createdOrg, OrganizationMembershipType.OWNER);
        ownership = this.organizationMemberRepository.save(ownership);
        this.organizationMemberRepository.refresh(ownership);

        props.attach("owner", ownership.getMember());

        return Optional.of(createdOrg);
    }



}
