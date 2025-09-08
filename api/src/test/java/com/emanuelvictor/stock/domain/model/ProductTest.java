package com.emanuelvictor.stock.domain.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldCreateProductWithValidData() {
        final var id = UUID.randomUUID();
        final var name = "Test Product";
        final var quantity = 10;

        final var product = Product.create(id, name, quantity);

        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(quantity, product.getQuantityAvailable());
    }

    @Test
    void shouldSetIdForProductWhenItIsNotDefined() {
        final var name = "Test Product";
        final var quantity = 10;

        final var product = Product.create(null, name, quantity);

        Assertions.assertThat(product.getId()).isNotNull();
        assertEquals(name, product.getName());
        assertEquals(quantity, product.getQuantityAvailable());
    }

    @Test
    void shouldThrowExceptionWhenCreatingProductWithNullName() {
        final var id = UUID.randomUUID();
        final var quantity = 10;

        final var exception = assertThrows(
                IllegalArgumentException.class,
                () -> Product.create(id, null, quantity)
        );
        assertEquals("Product name cannot be null or blank", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCreatingProductWithEmptyName() {
        final var id = UUID.randomUUID();
        final var quantity = 10;

        final var exception = assertThrows(
                IllegalArgumentException.class,
                () -> Product.create(id, "", quantity)
        );
        assertEquals("Product name cannot be null or blank", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCreatingProductWithBlankName() {
        final var id = UUID.randomUUID();
        final var quantity = 10;

        final var exception = assertThrows(
                IllegalArgumentException.class,
                () -> Product.create(id, "   ", quantity)
        );
        assertEquals("Product name cannot be null or blank", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCreatingProductWithNegativeQuantity() {
        final var id = UUID.randomUUID();
        final var name = "Test Product";
        final var quantity = -1;

        final var exception = assertThrows(
                IllegalArgumentException.class,
                () -> Product.create(id, name, quantity)
        );
        assertEquals("Quantity available cannot be negative", exception.getMessage());
    }

    @Test
    void shouldIncrementAvailableQuantity() {
        final var id = UUID.randomUUID();
        final var name = "Test Product";
        final var initialQuantity = 10;
        final var product = Product.create(id, name, initialQuantity);

        product.incrementQuantityAvailable(5);

        assertEquals(15, product.getQuantityAvailable());
    }

    @Test
    void shouldDecrementAvailableQuantity() {
        final var id = UUID.randomUUID();
        final var name = "Test Product";
        final var initialQuantity = 10;
        final var product = Product.create(id, name, initialQuantity);

        product.incrementQuantityAvailable(-5);

        assertEquals(5, product.getQuantityAvailable());
    }

    @Test
    void shouldThrowExceptionWhenDecrementingToNegativeQuantity() {
        final var id = UUID.randomUUID();
        final var name = "Test Product";
        final var initialQuantity = 5;
        final var product = Product.create(id, name, initialQuantity);

        final var exception = assertThrows(
                IllegalArgumentException.class,
                () -> product.incrementQuantityAvailable(-10)
        );
        assertEquals("The quantity available cannot be negative", exception.getMessage());
        assertEquals(5, product.getQuantityAvailable());
    }

    @Test
    void shouldChangeProductName() {
        final var id = UUID.randomUUID();
        final var initialName = "Test Product";
        final var quantity = 10;
        final var product = Product.create(id, initialName, quantity);

        product.updateData("New Name", null);

        assertEquals("New Name", product.getName());
    }
}
