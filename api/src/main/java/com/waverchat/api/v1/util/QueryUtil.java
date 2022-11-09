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
            builder.and(stringPath.contains(qField.substring(1, qField.length() - 1)));

        else if (qField.startsWith("*"))
            builder.and(stringPath.endsWith(qField.substring(1)));

        else if (qField.endsWith("*"))
            builder.and(stringPath.startsWith(qField.substring(0, qField.length() - 1)));

        else builder.and(stringPath.eq(qField));
    }

}
