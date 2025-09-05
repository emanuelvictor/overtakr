package com.emanuelvictor.stock.infrastructure.rest;

import com.emanuelvictor.stock.application.usecases.insertnewproduct.InsertNewProductUseCase;
import com.emanuelvictor.stock.application.usecases.insertnewproduct.InsertNewProductUseCaseImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/stocks/products")
public class InsertNewProductRest {

    private final InsertNewProductUseCaseImpl insertNewProductUseCaseImpl;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('root.stocks.products.create','root.stocks.products','root.stocks.products','root.stocks','root')")
    public ResponseEntity<ProductResponse> insertNewProduct(@RequestBody ProductRequest productRequest) {
        final var input = new InsertNewProductUseCase.Input(productRequest.name(), productRequest.quantityAvailable());
        final var output = insertNewProductUseCaseImpl.execute(input);
        return new ResponseEntity<>(new ProductResponse(output.name(), output.quantityAvailable()), CREATED);
    }

    public record ProductRequest(String name, int quantityAvailable) {

    }

    public record ProductResponse(String name, int quantityAvailable) {

    }
}
