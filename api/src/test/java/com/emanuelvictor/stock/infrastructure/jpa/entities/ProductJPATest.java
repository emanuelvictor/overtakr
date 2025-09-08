package com.emanuelvictor.stock.infrastructure.jpa.entities;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductJPATest {

    @Test
    void shouldCreateProductWithNoArgsConstructor() {
        ProductJPA product = new ProductJPA();

        assertThat(product).isNotNull();
        assertThat(product.getId()).isNull();
        assertThat(product.getName()).isNull();
        assertThat(product.getQuantityAvailable()).isNull();
    }

    @Test
    void shouldCreateProductWithAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        String name = "Test Product";
        Integer quantity = 10;

        ProductJPA product = new ProductJPA(id, name, quantity);

        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(id);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getQuantityAvailable()).isEqualTo(quantity);
    }

    @Test
    void shouldCreateProductWithConstructorAndModifyFields() {
        UUID originalId = UUID.randomUUID();
        UUID newId = UUID.randomUUID();
        String originalName = "Original Product";
        String newName = "Modified Product";
        Integer originalQuantity = 15;
        Integer newQuantity = 30;

        ProductJPA product = new ProductJPA(originalId, originalName, originalQuantity);
        product.setId(newId);
        product.setName(newName);
        product.setQuantityAvailable(newQuantity);

        assertThat(product.getId()).isEqualTo(newId);
        assertThat(product.getName()).isEqualTo(newName);
        assertThat(product.getQuantityAvailable()).isEqualTo(newQuantity);
    }
}