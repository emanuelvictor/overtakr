package com.emanuelvictor.stock.infrastructure.rest;

import com.emanuelvictor.stock.application.usecases.RemoveProductUseCase;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

// TODO create tests
@RestController
@RequiredArgsConstructor
@RequestMapping("api/stocks/products")
public class RemoveProductRest {

    private final RemoveProductUseCase removeProductUseCase;

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('root.stocks.products.delete','root.stocks.products','root.stocks','root')")
    public void removeProduct(@PathVariable UUID productId) {
        removeProductUseCase.execute(productId);
    }

}
