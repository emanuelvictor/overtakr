package com.emanuelvictor.stock.infrastructure.rest;

import com.emanuelvictor.stock.application.usecases.GetLessSoldProductsUseCaseImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/stocks/products")
public class GetLessSoldProductsRest {

    private final GetLessSoldProductsUseCaseImpl getLessSoldProductsUseCaseImpl;

    /**
     * // TODO coloquei esse path variable aqui, mas deveria ser uma queryString no GetProductsByFiltersRest. Algo fazendo só a ordenação por número de vendas.
     * Só está aqui para exemplificar.
     * @param pageable
     * @return
     */
    @GetMapping("/less-sold")
    @PreAuthorize("hasAnyAuthority('root.stocks.products.read','root.stocks.products','root.stocks.products','root.stocks','root')")
    public Page<ProductResponse> getLessSoldProducts(Pageable pageable) {
        final var page = getLessSoldProductsUseCaseImpl.execute(pageable);
        return extractPage(page);
    }

    private PageImpl<ProductResponse> extractPage(Page<GetLessSoldProductsUseCaseImpl.Output> page) {
        return new PageImpl<>(
                page.stream().map(product ->
                        new ProductResponse(product.productId(), product.name())
                ).toList(),
                page.getPageable(),
                page.getTotalElements()
        );
    }

    public record ProductResponse(String productId, String name) {

    }
}
