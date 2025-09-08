package com.emanuelvictor.stock.application.usecases;

import com.emanuelvictor.stock.domain.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

// TODO create test
@Service
@RequiredArgsConstructor
public class RemoveProductUseCaseImpl implements RemoveProductUseCase {

    private final ProductRepository productRepository;

    @Override
    public void execute(UUID productId) {
        productRepository.remove(productId);
    }

}
