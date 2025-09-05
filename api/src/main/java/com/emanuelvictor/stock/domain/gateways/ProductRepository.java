package com.emanuelvictor.stock.domain.gateways;

import com.emanuelvictor.stock.domain.model.Product;

public interface ProductRepository {
    void addProduct(Product product);
}
