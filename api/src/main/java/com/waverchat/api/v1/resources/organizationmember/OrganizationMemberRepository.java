package com.waverchat.api.v1.resources.organizationmember;

import com.waverchat.api.v1.customframework.BaseRepository;
import com.waverchat.api.v1.util.query.AppQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationMemberRepository extends BaseRepository<OrganizationMember, UUID>,
        QuerydslPredicateExecutor <OrganizationMember>
{

    default Page<OrganizationMember> findAll(AppQuery query, Pageable pageable) {
        return findAll(query.getBuilder(), pageable);
    }

    boolean existsByOrganization_IdAndMember_Id(UUID organizationId, UUID memberId);

    OrganizationMember findByOrganization_IdAndType(UUID organizationId, String type);

    default OrganizationMember findByOrganization_IdAndType(UUID organizationId, OrganizationMembershipType type) {
        return findByOrganization_IdAndType(organizationId, type.getValue());
    }

    default boolean existsByOrganization_IdAndMember_IdAndType(
            UUID organizationId, UUID memberId, OrganizationMembershipType type) {
        return existsByOrganization_IdAndMember_IdAndType(organizationId, memberId, type.getValue());
    }

    boolean existsByOrganization_IdAndMember_IdAndType(UUID organizationId, UUID memberId, String type);

    Optional<OrganizationMember> findByOrganization_Id(UUID organizationId);

}
