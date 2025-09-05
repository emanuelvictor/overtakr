package com.emanuelvictor.stock.application.usecases.insertnewproduct;

import com.emanuelvictor.common.application.usecases.UseCase;
import com.emanuelvictor.stock.domain.model.Product;
import com.emanuelvictor.stock.domain.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsertNewProductUseCaseImpl implements UseCase<InsertNewProductUseCase.Input, InsertNewProductUseCase.Output> {

    private final ProductRepository productRepository;

    @Override
    public InsertNewProductUseCase.Output execute(InsertNewProductUseCase.Input input) {
        final var product = Product.createNewProduct(input.name(), input.quantityAvailable());
        productRepository.addProduct(product);
        return new InsertNewProductUseCase.Output(product.getName(), product.getQuantityAvailable());
    }

}
