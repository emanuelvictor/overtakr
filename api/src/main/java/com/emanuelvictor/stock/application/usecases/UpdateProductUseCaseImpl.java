package com.emanuelvictor.stock.application.usecases;

import com.emanuelvictor.stock.domain.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// TODO create test
@Service
@RequiredArgsConstructor
public class UpdateProductUseCaseImpl implements UpdateProductUseCase {

    private final ProductRepository productRepository;

    @Override
    public UpdateProductUseCase.Output execute(UpdateProductUseCase.Input input) {
        final var product = productRepository.findById(input.id()).orElseThrow();
        product.updateData(input.name(), null);
        productRepository.updateProduct(product);
        return new UpdateProductUseCase.Output(product.getId(), product.getName(), product.getQuantityAvailable());
    }

}
