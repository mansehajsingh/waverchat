package com.waverchat.api.v1.http.response;

import com.waverchat.api.v1.framework.AbstractEntity;
import com.waverchat.api.v1.framework.DtoRS;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Data
public class PageResponse<T extends AbstractEntity, R extends DtoRS> {

    private List<R> entities;

    private Long totalEntityCount;

    private int totalPageCount;

    public PageResponse(Page<T> entities, Function<T, R> function) {
        this.entities = new ArrayList();

        for (T entity : entities) {
            R component = function.apply(entity);
            this.entities.add(component);
        }

        this.totalEntityCount = entities.getTotalElements();
        this.totalPageCount = entities.getTotalPages();
    }

}
