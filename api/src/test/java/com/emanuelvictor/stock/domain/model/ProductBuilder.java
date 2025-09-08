package com.emanuelvictor.stock.domain.model;

import java.util.UUID;

public class ProductBuilder {

    private UUID id = UUID.randomUUID();
    private String name = "Default Product Name" + UUID.randomUUID();
    private int quantityAvailable = 100;

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public ProductBuilder withQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
        return this;
    }

    public Product build() {
        return Product.create(id, name, quantityAvailable);
    }
}
