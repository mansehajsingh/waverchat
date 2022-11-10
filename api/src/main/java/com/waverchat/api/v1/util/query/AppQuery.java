package com.waverchat.api.v1.util.query;

import com.mysema.commons.lang.Pair;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;

import java.util.List;
import java.util.Map;

public class AppQuery extends InheritableBooleanBuilder {

    public AppQuery() {
        super();
    }

    public AppQuery(Predicate initial) {
        super(initial);
    }


    public AppQuery andAllStringQueries(
            String[] stringFields, StringPath[] stringPaths, Map<String, String> qValues)
    {
        for (int i = 0; i < stringFields.length; i++) {
            String field = stringFields[i];
            if (qValues.containsKey(field)) {
                String q = qValues.get(field);
                this.andStringPath(stringPaths[i], q);
            }
        }

        return this;
    }

    /**
     * Supplies the instance with a string wildcard query using and
     *
     * @param stringPath string path to branch from
     * @param qField query value
     */
    public AppQuery andStringPath(StringPath stringPath, String qField) {
        if (qField.startsWith("*") && qField.endsWith("*"))
            this.and(stringPath.containsIgnoreCase(qField.substring(1, qField.length() - 1)));

        else if (qField.startsWith("*"))
            this.and(stringPath.endsWithIgnoreCase(qField.substring(1)));

        else if (qField.endsWith("*"))
            this.and(stringPath.startsWithIgnoreCase(qField.substring(0, qField.length() - 1)));

        else this.and(stringPath.equalsIgnoreCase(qField));

        return this;
    }

}
