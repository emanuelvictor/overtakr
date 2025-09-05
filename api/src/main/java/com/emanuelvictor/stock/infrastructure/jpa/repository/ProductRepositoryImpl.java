package com.emanuelvictor.stock.infrastructure.jpa.repository;

import com.emanuelvictor.stock.infrastructure.jpa.entity.ProductJPA;
import com.emanuelvictor.stock.infrastructure.jpa.repository.springdata.ProductJPARepository;
import com.emanuelvictor.stock.domain.model.Product;
import com.emanuelvictor.stock.domain.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJPARepository ProductJPARepository;

    @Override
    public void addProduct(Product product) {
        ProductJPARepository.save(new ProductJPA(product.getName()));
    }

}
