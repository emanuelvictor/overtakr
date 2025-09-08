package com.emanuelvictor.stock.domain.gateways;

import com.emanuelvictor.stock.domain.model.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    void addProduct(Product product);

    void updateProduct(Product product);

    Optional<Product> findById(final UUID id);

    void remove(final UUID id);
}
