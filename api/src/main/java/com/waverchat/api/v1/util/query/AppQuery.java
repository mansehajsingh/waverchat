package com.waverchat.api.v1.util.query;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.StringPath;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

public class AppQuery extends InheritableBooleanBuilder {

    protected final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public AppQuery() {
        super();
    }

    public AppQuery(Predicate initial) {
        super(initial);
    }

    private ZonedDateTime parseDateStringToZDT(String dateString) throws ParseException {
        return ZonedDateTime.ofInstant(
                this.dateFormatter.parse(dateString).toInstant(), ZoneId.of("UTC"));
    }


    public AppQuery andAllStringQueries(
            String[] stringFields, StringPath[] stringPaths, Map<String, String> queryParams)
    {
        for (int i = 0; i < stringFields.length; i++) {
            String field = stringFields[i];
            if (queryParams.containsKey(field)) {
                String q = queryParams.get(field);
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

    public AppQuery andDefaultDatePathBehaviour(
            DateTimePath<ZonedDateTime> createdAtPath,
            DateTimePath<ZonedDateTime> updatedAtPath,
            Map<String, String> queryParams
    ) {
        if (queryParams.containsKey("createdAfter")) {
            try {
                ZonedDateTime createdAfter =  this.parseDateStringToZDT(queryParams.get("createdAfter"));
                this.and(createdAtPath.after(createdAfter));
            } catch (ParseException e) {}
        }

        if (queryParams.containsKey("createdBefore")) {
            try {
                ZonedDateTime createdBefore = this.parseDateStringToZDT(queryParams.get("createdBefore"));
                this.and(createdAtPath.before(createdBefore));
            } catch (ParseException e) {}
        }

        if (queryParams.containsKey("updatedAfter")) {
            try {
                ZonedDateTime updatedAfter = this.parseDateStringToZDT(queryParams.get("createdBefore"));
                this.and(updatedAtPath.after(updatedAfter));
            } catch (ParseException e) {
            }
        }

        if (queryParams.containsKey("updatedBefore")) {
            try {
                ZonedDateTime updatedBefore = this.parseDateStringToZDT(queryParams.get("createdBefore"));
                this.and(updatedAtPath.before(updatedBefore));
            } catch (ParseException e) {}
        }

        return this;
    }

}
