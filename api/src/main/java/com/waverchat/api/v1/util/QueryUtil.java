package com.waverchat.api.v1.util;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;

public class QueryUtil {

    private QueryUtil() {}

    public static void applyStringQueryWithWildcards(
            BooleanBuilder builder,
            StringPath stringPath,
            String qField
    ) {
        if (qField.startsWith("*") && qField.endsWith("*"))
            builder.and(stringPath.containsIgnoreCase(qField.substring(1, qField.length() - 1)));

        else if (qField.startsWith("*"))
            builder.and(stringPath.endsWithIgnoreCase(qField.substring(1)));

        else if (qField.endsWith("*"))
            builder.and(stringPath.startsWithIgnoreCase(qField.substring(0, qField.length() - 1)));

        else builder.and(stringPath.equalsIgnoreCase(qField));
    }

}
