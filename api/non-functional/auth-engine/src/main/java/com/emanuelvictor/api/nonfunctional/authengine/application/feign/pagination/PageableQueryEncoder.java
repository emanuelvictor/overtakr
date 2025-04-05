package com.emanuelvictor.api.nonfunctional.authengine.application.feign.pagination;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This encoder adds support for pageable, which will be applied to the query parameters.
 *
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 1.0.0, 10/09/2019
 */
public class PageableQueryEncoder implements Encoder {

    /**
     *
     */
    private final Encoder delegate;

    /**
     * @param delegate Encoder
     */
    public PageableQueryEncoder(final Encoder delegate) {
        this.delegate = delegate;
    }

    /**
     * @param object   Object
     * @param bodyType Type
     * @param template RequestTemplate
     * @throws EncodeException
     */
    @Override
    public void encode(final Object object, final Type bodyType, final RequestTemplate template) throws EncodeException {

        if (object instanceof Pageable) {
            final Pageable pageable = (Pageable) object;
            template.query("page", pageable.getPageNumber() + "");
            template.query("size", pageable.getPageSize() + "");

            pageable.getSort();
            final Collection<String> existingSorts = template.queries().get("sort");
            final List<String> sortQueries = existingSorts != null ? new ArrayList<>(existingSorts) : new ArrayList<>();
            for (Sort.Order order : pageable.getSort()) {
                sortQueries.add(order.getProperty() + "," + order.getDirection());
            }
            template.query("sort", sortQueries);

        } else {
            delegate.encode(object, bodyType, template);
        }
    }
}
