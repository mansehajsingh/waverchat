package com.waverchat.api.v1.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

public class PageableFactory {

    private PageableFactory() {}

    public static Pageable createPageable(int defaultPage, int defaultLimit, String sortByField, boolean sortAscending, Map<String, String> queryParams, int maxLimit) {
        int page = defaultPage, limit = defaultLimit;

        if (queryParams.containsKey("page") && queryParams.containsKey("limit")) {
            page = Integer.parseInt(queryParams.get("page"));
            limit = Integer.parseInt(queryParams.get("limit"));
        } else if (queryParams.containsKey("limit")) {
            limit = Integer.parseInt(queryParams.get("limit"));
        }

        if (limit > maxLimit) limit = maxLimit;

        Sort sort;

        if (sortAscending) sort = Sort.by(sortByField).ascending();
        else sort = sort = Sort.by(sortByField).descending();

        return PageRequest.of(page, limit, sort);
    }

}
