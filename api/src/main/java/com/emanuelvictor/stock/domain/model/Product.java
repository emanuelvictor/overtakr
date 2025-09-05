package com.emanuelvictor.stock.domain.model;

import java.util.UUID;

public class Product {

    private final UUID id;
    private final String name;
    private final Integer quantityAvailable;

    private Product(UUID id, String name, Integer quantityAvailable) {
        this.id = id;
        this.name = name;
        this.quantityAvailable = quantityAvailable;
    }

    public static Product createNewProduct(final String name, Integer quantityAvailable) {
        return new Product(UUID.randomUUID(), name, quantityAvailable);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }
}
