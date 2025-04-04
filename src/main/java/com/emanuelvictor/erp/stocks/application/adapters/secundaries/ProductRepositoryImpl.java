package com.emanuelvictor.erp.stocks.application.adapters.secundaries;

import com.emanuelvictor.erp.stocks.domain.Product;
import com.emanuelvictor.erp.stocks.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final TProductRepository tProductRepository;

    @Override
    public void addProduct(Product product) {
        tProductRepository.save(new TProduct(product.getName()));
    }

    @Override
    public List<Product> getAllProducts() {
        return tProductRepository.findAll().stream().map(tProduct -> new Product(tProduct.getName())).toList();
    }
}
