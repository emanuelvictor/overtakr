package com.emanuelvictor.stock.infrastructure.rest;

import com.emanuelvictor.stock.application.usecases.UpdateProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// TODO create tests
@RestController
@RequiredArgsConstructor
@RequestMapping("api/stocks/products")
public class UpdateProductRest {

    private final UpdateProductUseCase updateProductUseCase;

    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('root.stocks.products.update','root.stocks.products','root.stocks','root')")
    public ProductResponse updateProduct(@PathVariable UUID productId, @RequestBody ProductRequest productRequest) {
        final var input = new UpdateProductUseCase.Input(productId, productRequest.name(), null);
        final var output = updateProductUseCase.execute(input);
        return new ProductResponse(output.id(), output.name());
    }

    public record ProductRequest(String name) {
    }

    public record ProductResponse(UUID id, String name) {
    }

}
