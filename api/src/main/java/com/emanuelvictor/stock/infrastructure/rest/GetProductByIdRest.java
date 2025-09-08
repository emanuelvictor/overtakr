package com.emanuelvictor.stock.infrastructure.rest;

import com.emanuelvictor.stock.infrastructure.jpa.entities.ProductJPA;
import com.emanuelvictor.stock.infrastructure.jpa.repository.springdata.ProductJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

// TODO create tests
@RestController
@RequiredArgsConstructor
@RequestMapping("api/stocks/products")
public class GetProductByIdRest {

    private final ProductJPARepository productJPARepository;

    @GetMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('root.stocks.products.read','root.stocks.products','root.stocks.products','root.stocks','root')")
    public ProductResponse getProductById(@PathVariable UUID productId) {
        final var productJPA = productJPARepository.findById(productId);
        return convertToReponse(productJPA.orElseThrow());
    }

    private ProductResponse convertToReponse(ProductJPA productJPA) {
        return new ProductResponse(productJPA.getId(), productJPA.getName());
    }

    public record ProductResponse(UUID id, String name) {
    }
}
