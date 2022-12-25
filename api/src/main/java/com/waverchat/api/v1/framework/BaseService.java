package com.waverchat.api.v1.framework;

import com.waverchat.api.v1.exceptions.ConflictException;
import com.waverchat.api.v1.exceptions.NotFoundException;
import com.waverchat.api.v1.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaseService<E extends AbstractEntity, R extends BaseRepository<E, Long>> {

    @Autowired
    protected R repository;

    public E findById(Long id) throws NotFoundException {
        Optional<E> entityOpt = this.repository.findById(id);

        if (entityOpt.isPresent())
            return entityOpt.get();

        throw new NotFoundException("No resource exists with id " + id );
    }

    public void auditForCreate(E entity) throws ConflictException {}

    public E create(E entity) throws ValidationException, ConflictException {
        entity.validateForCreate();

        auditForCreate(entity);

        return this.repository.save(entity);
    }

    public void auditForUpdate(E entity) throws ConflictException {}

    public E update(E updatedEntity) throws ValidationException, ConflictException {
        updatedEntity.validateForUpdate();

        auditForUpdate(updatedEntity);

        return this.repository.save(updatedEntity);
    }

    public E delete(E entity) throws NotFoundException {

        E fetchedEntity = findById(entity.getId());

        this.repository.deleteById(fetchedEntity.getId());

        return fetchedEntity;
    }

    protected Sort createSort(
            boolean defaultSortAsc,
            String defaultSortField,
            List<String> supportedSortFields,
            Map<String, String> queryParams
    ) {
        String sortByField = defaultSortField;
        boolean sortAscending = defaultSortAsc;

        if (queryParams.containsKey("sortBy")
                && supportedSortFields.stream().anyMatch(queryParams.get("sortBy")::equalsIgnoreCase))
        {
            sortByField = queryParams.get("sortBy");
            sortAscending = queryParams.containsKey("sort") && queryParams.get("sort").equalsIgnoreCase("ASC");
        }

        Sort sort;

        if (sortAscending) sort = Sort.by(sortByField).ascending();
        else sort = Sort.by(sortByField).descending();

        return sort;
    }

    protected Pageable createPageable(
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
