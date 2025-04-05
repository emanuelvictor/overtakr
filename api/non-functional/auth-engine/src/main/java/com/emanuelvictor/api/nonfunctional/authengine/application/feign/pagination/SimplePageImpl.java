package com.emanuelvictor.api.nonfunctional.authengine.application.feign.pagination;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.*;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 1.0.0, 10/09/2019
 */
public class SimplePageImpl<T> implements Page<T> {

    /**
     *
     */
    private final Page<T> delegate;

    /**
     * @param content       List<T>
     * @param number        int
     * @param size          int
     * @param totalElements long
     */
    public SimplePageImpl(@JsonProperty("content") final List<T> content,
                          @JsonProperty("page") final int number,
                          @JsonProperty("size") final int size,
                          @JsonProperty("totalElements") final long totalElements) {
        delegate = new PageImpl<>(content, PageRequest.of(number, size), totalElements);
    }

    /**
     * @return
     */
    @JsonProperty
    @Override
    public int getTotalPages() {
        return delegate.getTotalPages();
    }

    /**
     * @return
     */
    @JsonProperty
    @Override
    public long getTotalElements() {
        return delegate.getTotalElements();
    }

    /**
     * @return
     */
    @JsonProperty("page")
    @Override
    public int getNumber() {
        return delegate.getNumber();
    }

    /**
     * @return
     */
    @JsonProperty
    @Override
    public int getSize() {
        return delegate.getSize();
    }

    /**
     * @return
     */
    @JsonProperty
    @Override
    public int getNumberOfElements() {
        return delegate.getNumberOfElements();
    }

    /**
     * @return
     */
    @JsonProperty
    @Override
    public List<T> getContent() {
        return delegate.getContent();
    }

    /**
     * @return
     */
    @JsonProperty
    @Override
    public boolean hasContent() {
        return delegate.hasContent();
    }

    /**
     * @return
     */
    @JsonIgnore
    @Override
    public Sort getSort() {
        return delegate.getSort();
    }

    /**
     * @return
     */
    @JsonProperty
    @Override
    public boolean isFirst() {
        return delegate.isFirst();
    }

    /**
     * @return
     */
    @JsonProperty
    @Override
    public boolean isLast() {
        return delegate.isLast();
    }

    /**
     * @return
     */
    @JsonIgnore
    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    /**
     * @return
     */
    @JsonIgnore
    @Override
    public boolean hasPrevious() {
        return delegate.hasPrevious();
    }

    /**
     * @return
     */
    @JsonIgnore
    @Override
    public Pageable nextPageable() {
        return delegate.nextPageable();
    }

    /**
     * @return Pageable
     */
    @JsonIgnore
    @Override
    public Pageable previousPageable() {
        return delegate.previousPageable();
    }

    /**
     * @param converter
     * @param <U>
     * @return
     */
    @JsonIgnore
    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return delegate.map(converter);
    }

    /**
     * @return
     */
    @JsonIgnore
    @Override
    public Iterator<T> iterator() {
        return delegate.iterator();
    }
}
