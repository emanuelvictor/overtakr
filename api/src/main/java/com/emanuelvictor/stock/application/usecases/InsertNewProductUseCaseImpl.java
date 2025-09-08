package com.emanuelvictor.stock.application.usecases;

import com.emanuelvictor.stock.domain.model.Product;
import com.emanuelvictor.stock.domain.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsertNewProductUseCaseImpl implements InsertNewProductUseCase {

    private final ProductRepository productRepository;

    @Override
    public InsertNewProductUseCase.Output execute(InsertNewProductUseCase.Input input) {
        final var product = Product.create(null, input.name(), input.quantityAvailable());
        productRepository.addProduct(product);
        return new InsertNewProductUseCase.Output(product.getId(), product.getName(), product.getQuantityAvailable());
    }

}
