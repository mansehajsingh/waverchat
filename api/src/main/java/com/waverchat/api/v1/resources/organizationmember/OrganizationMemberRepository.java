package com.waverchat.api.v1.resources.organizationmember;

import com.waverchat.api.v1.customframework.BaseRepository;
import com.waverchat.api.v1.util.query.AppQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizationMemberRepository extends BaseRepository<OrganizationMember, UUID>,
        QuerydslPredicateExecutor <OrganizationMember>
{

    default Page<OrganizationMember> findAll(AppQuery query, Pageable pageable) {
        return findAll(query.getBuilder(), pageable);
    }

    boolean existsByOrganization_IdAndMember_Id(UUID organizationId, UUID memberId);

}
