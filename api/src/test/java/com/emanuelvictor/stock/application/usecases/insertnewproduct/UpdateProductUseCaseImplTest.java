package com.emanuelvictor.stock.application.usecases.insertnewproduct;

import com.emanuelvictor.stock.application.usecases.InsertNewProductUseCase;
import com.emanuelvictor.stock.application.usecases.InsertNewProductUseCaseImpl;
import com.emanuelvictor.stock.domain.gateways.ProductRepository;
import com.emanuelvictor.stock.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UpdateProductUseCaseImplTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private InsertNewProductUseCaseImpl insertNewProductUseCase;

    @BeforeEach
    void setUp() {
        insertNewProductUseCase = new InsertNewProductUseCaseImpl(productRepository);
    }

    @Test
    void shouldInsertNewProductSuccessfully() {
        final var name = "Test Product";
        final var quantity = 10;
        final var input = new InsertNewProductUseCase.Input(name, quantity);

        final var output = insertNewProductUseCase.execute(input);

        assertNotNull(output);
        assertEquals(name, output.name());
        assertEquals(quantity, output.quantityAvailable());
        verify(productRepository).addProduct(any(Product.class));
    }

    @Test
    void shouldInsertNewProductWithZeroQuantity() {
        final var name = "Test Product";
        final var quantity = 0;
        final var input = new InsertNewProductUseCase.Input(name, quantity);

        final var output = insertNewProductUseCase.execute(input);

        assertNotNull(output);
        assertEquals(name, output.name());
        assertEquals(quantity, output.quantityAvailable());
        verify(productRepository).addProduct(any(Product.class));
    }

    @Test
    void shouldMaintainAttributesConsistencyWhenInsertingProduct() {
        final var name = "Product With Long Name";
        final var quantity = 999;
        final var input = new InsertNewProductUseCase.Input(name, quantity);

        final var output = insertNewProductUseCase.execute(input);

        assertNotNull(output);
        assertEquals(name, output.name());
        assertEquals(quantity, output.quantityAvailable());
        verify(productRepository).addProduct(any(Product.class));
    }
}
