package com.waverchat.api.v1.resources.organizationmember;

import com.querydsl.core.types.dsl.StringPath;

import java.util.HashMap;
import java.util.Map;

public class OrganizationMemberConstants {

    public static final boolean DEFAULT_SORT_IS_ASCENDING = false;

    public static final String DEFAULT_SORT_FIELD = "createdAt";

    public static final int MAX_PAGE_SIZE = 300;

    public static final Map<String, StringPath> QUERYABLE_STR_PATHS = new HashMap<>() {{
        put("username", QOrganizationMember.organizationMember.member.username);
        put("email", QOrganizationMember.organizationMember.member.email);
        put("firstName", QOrganizationMember.organizationMember.member.firstName);
        put("lastName", QOrganizationMember.organizationMember.member.lastName);
    }};

}
