package com.waverchat.api.v1.resources.organizationmember;

import com.waverchat.api.v1.framework.BaseRepository;
import com.waverchat.api.v1.resources.organizationmember.entity.OrganizationMember;
import com.waverchat.api.v1.resources.organizationmember.entity.OrganizationMembershipType;
import com.waverchat.api.v1.util.query.AppQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationMemberRepository extends BaseRepository<OrganizationMember, Long>,
        QuerydslPredicateExecutor <OrganizationMember>
{

    default Page<OrganizationMember> findAll(AppQuery query, Pageable pageable) {
        return findAll(query.getBuilder(), pageable);
    }

    boolean existsByOrganization_IdAndMember_Id(Long organizationId, Long memberId);

    OrganizationMember findByOrganization_IdAndType(Long organizationId, String type);

    default OrganizationMember findByOrganization_IdAndType(Long organizationId, OrganizationMembershipType type) {
        return findByOrganization_IdAndType(organizationId, type.getValue());
    }

    default boolean existsByOrganization_IdAndMember_IdAndType(
            Long organizationId, Long memberId, OrganizationMembershipType type) {
        return existsByOrganization_IdAndMember_IdAndType(organizationId, memberId, type.getValue());
    }

    boolean existsByOrganization_IdAndMember_IdAndType(Long organizationId, Long memberId, String type);

    Optional<OrganizationMember> findByOrganization_Id(Long organizationId);

}
