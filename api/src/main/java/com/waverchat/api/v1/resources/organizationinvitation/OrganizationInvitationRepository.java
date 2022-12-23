package com.waverchat.api.v1.resources.organizationinvitation;

import com.waverchat.api.v1.framework.BaseRepository;
import com.waverchat.api.v1.resources.organizationinvitation.entity.OrganizationInvitation;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationInvitationRepository extends BaseRepository<OrganizationInvitation, Long>,
        QuerydslPredicateExecutor<OrganizationInvitation> {

    boolean existsByCandidate_IdAndOrganization_Id(Long candidateId, Long organizationId);

}
