package com.waverchat.api.v1.resources.organization;

import com.waverchat.api.v1.customframework.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizationRepository extends BaseRepository<Organization, UUID> {

}
