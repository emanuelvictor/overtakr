package com.emanuelvictor.stock.infrastructure.jpa.repository;

import com.emanuelvictor.stock.infrastructure.jpa.entities.ProductJPA;
import com.emanuelvictor.stock.infrastructure.jpa.repository.springdata.ProductJPARepository;
import com.emanuelvictor.stock.domain.model.Product;
import com.emanuelvictor.stock.domain.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJPARepository productJPARepository;

    @Override
    public void addProduct(Product product) {
        productJPARepository.save(new ProductJPA(product.getId(), product.getName(), product.getQuantityAvailable()));
    }

    @Override // TODO make test
    public void updateProduct(Product product) {
        productJPARepository.findById(product.getId())
                .ifPresent(productJPA -> {
                    productJPA.setName(product.getName());
                    productJPA.setQuantityAvailable(product.getQuantityAvailable());
                    productJPARepository.save(new ProductJPA(product.getId(), product.getName(), product.getQuantityAvailable()));
                });
    }

    @Override // TODO make test
    public Optional<Product> findById(UUID id) {
        return productJPARepository.findById(id)
                .map(productJPA -> Product.create(productJPA.getId(),
                        productJPA.getName(), productJPA.getQuantityAvailable()));
    }

    @Override // TODO make test
    public void remove(UUID id) {
        productJPARepository.deleteById(id);
    }
}
