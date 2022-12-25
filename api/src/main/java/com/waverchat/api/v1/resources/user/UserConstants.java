package com.waverchat.api.v1.resources.user;

import com.querydsl.core.types.dsl.StringPath;
import edu.emory.mathcs.backport.java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserConstants {

    public static final int MIN_USERNAME_LENGTH = 4;
    public static final int MAX_USERNAME_LENGTH = 20;

    public static final int MIN_FIRST_NAME_LENGTH = 1;
    public static final int MAX_FIRST_NAME_LENGTH = 35;

    public static final int MAX_LAST_NAME_LENGTH = 35;

    public static final int MIN_PASSWORD_LENGTH = 6;

    public static final int MAX_PASSWORD_LENGTH = 500;

    public static final int MAX_PAGE_SIZE = 300;

    public static final String DEFAULT_SORT_FIELD = "createdAt";

    public static final boolean DEFAULT_SORT_IS_ASCENDING = false;

    public static final List<String> SUPPORTED_SORT_FIELDS =
            Arrays.asList(new String[] {"email", "username", "firstName", "lastName", "createdAt", "updatedAt"});

    public static final Map<String, StringPath> QUERYABLE_STR_PATHS = new HashMap<>() {{
        put("username", QUser.user.username);
        put("email", QUser.user.email);
        put("firstName", QUser.user.firstName);
        put("lastName", QUser.user.lastName);
    }};

    private UserConstants() {}

}
