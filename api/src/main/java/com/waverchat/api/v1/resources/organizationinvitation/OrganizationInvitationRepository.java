package com.waverchat.api.v1.resources.organizationinvitation;

import com.waverchat.api.v1.customframework.BaseRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizationInvitationRepository extends BaseRepository<OrganizationInvitation, UUID>,
        QuerydslPredicateExecutor<OrganizationInvitation> {

    boolean existsByCandidate_IdAndOrganization_Id(UUID candidateId, UUID organizationId);

}
