package com.waverchat.api.v1.customframework;

import com.waverchat.api.v1.resources.user.UserConstants;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

public abstract class AbstractService<E extends AbstractEntity> {

    protected Sort createSort(boolean defaultSortAsc, String defaultSortField, Map<String, String> queryParams) {
        String sortByField = defaultSortField;
        boolean sortAscending = defaultSortAsc;

        if (queryParams.containsKey("sortBy") && UserConstants.SUPPORTED_SORT_TAGS.contains(queryParams.get("sortBy")))
        {
            sortByField = queryParams.get("sortBy");
            sortAscending = queryParams.containsKey("sort") && queryParams.get("sort").equalsIgnoreCase("ASC");
        }

        Sort sort;

        if (sortAscending) sort = Sort.by(sortByField).ascending();
        else sort = sort = Sort.by(sortByField).descending();

        return sort;
    }

    public Pageable createPageable(
            int defaultPage,
            int defaultLimit,
            int maxLimit,
            Sort sort,
            Map<String, String> queryParams
    ) {
        int page = defaultPage, limit = defaultLimit;

        if (queryParams.containsKey("page") && queryParams.containsKey("limit")) {
            page = Integer.parseInt(queryParams.get("page"));
            limit = Integer.parseInt(queryParams.get("limit"));
        } else if (queryParams.containsKey("limit")) {
            limit = Integer.parseInt(queryParams.get("limit"));
        }

        if (limit > maxLimit) limit = maxLimit;

        return PageRequest.of(page, limit, sort);
    }

}
