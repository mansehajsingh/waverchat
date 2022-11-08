package com.waverchat.api.v1.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public class PageableFactory {

    private PageableFactory() {}

    public static Pageable createPageable(int defaultPage, int defaultLimit, Map<String, String> queryParams) {
        int page = defaultPage, limit = defaultLimit;

        if (queryParams.containsKey("page") && queryParams.containsKey("limit")) {
            page = Integer.parseInt(queryParams.get("page"));
            limit = Integer.parseInt(queryParams.get("limit"));
        } else if (queryParams.containsKey("limit")) {
            limit = Integer.parseInt(queryParams.get("limit"));
        }

        return PageRequest.of(page, limit);
    }

}
