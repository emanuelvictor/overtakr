package com.emanuelvictor.stock.domain.model;

import lombok.Getter;

import java.util.UUID;

@Getter // todo INSERIR RFC PARA ESSE ACOPLAMENTO
public class Product {

    private final UUID id;
    private String name;
    private int quantityAvailable;
    private Model model; // TODO apenas para mostrar a integração com kotlin

    private Product(UUID id, String name, int quantityAvailable) {
        validateData(id, name, quantityAvailable);
        this.id = id;
        this.name = name;
        this.quantityAvailable = quantityAvailable;
    }

    private static void validateData(UUID id, String name, int quantityAvailable) {
        if (id == null || id.toString().isBlank()) {
            throw new IllegalArgumentException("The id cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or blank");
        }
        if (quantityAvailable < 0) {
            throw new IllegalArgumentException("Quantity available cannot be negative");
        }
    }

    public static Product create(final UUID id, final String name, final int quantityAvailable) {
        if (id == null || id.toString().isBlank())
            return new Product(UUID.randomUUID(), name, quantityAvailable);
        return new Product(id, name, quantityAvailable);
    }

    public void incrementQuantityAvailable(int amount) {
        final var newQuantity = this.quantityAvailable + amount;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("The quantity available cannot be negative");
        }
        this.quantityAvailable = newQuantity;
    }

    // TODO criar validações e testes unitários
    public void updateData(final String name, final Model model) {
        this.name = name;
        this.model = model;
    }
}
