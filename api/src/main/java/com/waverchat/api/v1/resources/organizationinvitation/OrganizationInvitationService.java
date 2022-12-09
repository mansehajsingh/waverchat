package com.waverchat.api.v1.resources.organizationinvitation;

import com.waverchat.api.v1.customframework.AbstractApplicationService;
import com.waverchat.api.v1.customframework.RQRSLifecycleProperties;
import com.waverchat.api.v1.exceptions.ConflictException;
import com.waverchat.api.v1.resources.organizationmember.OrganizationMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizationInvitationService extends AbstractApplicationService<OrganizationInvitation> {

    @Autowired
    private OrganizationInvitationRepository organizationInvitationRepository;

    @Autowired
    private OrganizationMemberRepository organizationMemberRepository;

    @Override
    public void auditForCreate(OrganizationInvitation entityToCreate, RQRSLifecycleProperties props) throws ConflictException {
        boolean isAlreadyMember = this.organizationMemberRepository.existsByOrganization_IdAndMember_Id(
                entityToCreate.getOrganization().getId(), entityToCreate.getCandidate().getId());

        if (isAlreadyMember)
            throw new ConflictException("User with user id " +
                    entityToCreate.getCandidate().getId() + " is already a member of this organization.");

        boolean invitationExists = this.organizationInvitationRepository.existsByCandidate_IdAndOrganization_Id(
                entityToCreate.getCandidate().getId(), entityToCreate.getOrganization().getId());

        if (invitationExists)
            throw new ConflictException("Existing invitation registered for user id " +
                    entityToCreate.getCandidate().getId() + " and organization id " + entityToCreate.getOrganization().getId() + ".");
    }

    @Override
    public Optional<OrganizationInvitation> create(OrganizationInvitation entityToCreate, RQRSLifecycleProperties props) {
        return Optional.of(this.organizationInvitationRepository.save(entityToCreate));
    }
}
