package com.waverchat.api.v1.resources.organizationmember;

import com.waverchat.api.v1.customframework.AbstractApplicationService;
import com.waverchat.api.v1.customframework.RQRSLifecycleProperties;
import com.waverchat.api.v1.resources.user.UserRepository;
import com.waverchat.api.v1.util.query.AppQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationMemberService extends AbstractApplicationService<OrganizationMember> {

    @Autowired
    private OrganizationMemberRepository organizationMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<OrganizationMember> getById(UUID id, RQRSLifecycleProperties props) {
        return this.organizationMemberRepository.findById(id);
    }

    @Override
    public Optional<OrganizationMember> create(OrganizationMember entityToCreate, RQRSLifecycleProperties props) {
       OrganizationMember createdOM = this.organizationMemberRepository.saveAndFlush(entityToCreate);
       this.organizationMemberRepository.refresh(createdOM);

       return Optional.of(createdOM);
    }

    @Override
    public Page<OrganizationMember> getAll(RQRSLifecycleProperties props) {
        QOrganizationMember qOrgMember = QOrganizationMember.organizationMember;
        AppQuery query = new AppQuery();

        query.andAllStringQueries(OrganizationMemberConstants.QUERYABLE_STR_PATHS, props.getQueryParams());

        query.andDefaultDatePathBehaviour(qOrgMember.createdAt, qOrgMember.updatedAt, props.getQueryParams());

        // matching the type of the membership
        if (props.getQueryParams().containsKey("type")) {
            query.and(qOrgMember.type.equalsIgnoreCase(
                    props.getQueryParams().get("type")
            ));
        }

        Sort sort = this.createSort(OrganizationMemberConstants.DEFAULT_SORT_IS_ASCENDING,
                OrganizationMemberConstants.DEFAULT_SORT_FIELD, props.getQueryParams());

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort); // equivalent of no pagination

        query.and(qOrgMember.organization.id.eq(props.getPathVariableIds().get("organizationId")));

        Page<OrganizationMember> page = this.organizationMemberRepository.findAll(query, pageable);

        return page;
    }

    public boolean isMemberOfOrganization(UUID userId, UUID organizationId) {
        return this.organizationMemberRepository.existsByOrganization_IdAndMember_Id(organizationId, userId);
    }

    public boolean isOwnerOfOrganization(UUID userId, UUID organizationId) {
        return this.organizationMemberRepository.existsByOrganization_IdAndMember_IdAndType(
                organizationId, userId, OrganizationMembershipType.OWNER);
    }

    public OrganizationMember getOrganizationOwnerByOrganizationId(UUID organizationId) {
        return this.organizationMemberRepository.findByOrganization_IdAndType(organizationId, OrganizationMembershipType.OWNER);
    }

}
