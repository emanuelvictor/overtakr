package com.emanuelvictor.erp.stocks.domain;

import java.util.List;

public interface ProductRepository {

    void addProduct(Product product);

    List<Product> getAllProducts();
}
