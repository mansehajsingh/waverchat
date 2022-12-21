package com.waverchat.api.v1.framework;

import com.waverchat.api.v1.exceptions.NotFoundException;
import com.waverchat.api.v1.resources.user.UserConstants;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface BaseService<E extends AbstractEntity> {
    E find(UUID id) throws NotFoundException;

    Optional<E> create(E entity);

    Optional<E> update(E entity) throws NotFoundException;

    Optional<E> delete(E entity) throws NotFoundException;

    default Sort createSort(boolean defaultSortAsc, String defaultSortField, Map<String, String> queryParams) {
        String sortByField = defaultSortField;
        boolean sortAscending = defaultSortAsc;

        if (queryParams.containsKey("sortBy") && UserConstants.SUPPORTED_SORT_TAGS.contains(queryParams.get("sortBy")))
        {
            sortByField = queryParams.get("sortBy");
            sortAscending = queryParams.containsKey("sort") && queryParams.get("sort").equalsIgnoreCase("ASC");
        }

        Sort sort;

        if (sortAscending) sort = Sort.by(sortByField).ascending();
        else sort = Sort.by(sortByField).descending();

        return sort;
    }

    default Pageable createPageable(
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
