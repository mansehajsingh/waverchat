package com.waverchat.api.v1.resources.organization;

import com.waverchat.api.v1.framework.BaseRepository;
import com.waverchat.api.v1.resources.organization.entity.Organization;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends BaseRepository<Organization, Long> {

}
