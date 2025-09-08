package com.emanuelvictor.stock.infrastructure.jpa.repository.springdata;

import com.emanuelvictor.SpringBootTests;
import com.emanuelvictor.stock.infrastructure.jpa.entities.ProductJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductJPARepositoryTest extends SpringBootTests {

    @Autowired
    private ProductJPARepository productJPARepository;

    @BeforeEach
    void setUp() {
        productJPARepository.deleteAll();
    }

    @Test
    void mustListProductsByFiltersAndReturnOne() {
        final var firstProduct = new ProductJPA(UUID.randomUUID(), "First name", 10);
        final var seccondProduct = new ProductJPA(UUID.randomUUID(), "Second name", 10);
        productJPARepository.save(firstProduct);
        productJPARepository.save(seccondProduct);

        final var pageOfProducts = productJPARepository.listProductsByFilters("First", null);

        assertThat(pageOfProducts.getTotalElements()).isEqualTo(1);
        assertThat(pageOfProducts.getContent()).extracting("name").containsExactly("First name");
    }

    @Test
    void mustListProductsByFiltersAndReturnTwo() {
        final var firstProduct = new ProductJPA(UUID.randomUUID(), "First name", 10);
        final var seccondProduct = new ProductJPA(UUID.randomUUID(), "Second name", 10);
        productJPARepository.save(firstProduct);
        productJPARepository.save(seccondProduct);

        final var pageOfProducts = productJPARepository.listProductsByFilters("NAME", null);

        assertThat(pageOfProducts.getTotalElements()).isEqualTo(2);
        assertThat(pageOfProducts.getContent()).extracting("name")
                .containsOnly("First name", "Second name");
    }

    @Test
    void mustListProductsByFiltersAndReturnAnyone() {
        final var firstProduct = new ProductJPA(UUID.randomUUID(), "First name", 10);
        final var seccondProduct = new ProductJPA(UUID.randomUUID(), "Second name", 10);
        productJPARepository.save(firstProduct);
        productJPARepository.save(seccondProduct);

        final var pageOfProducts = productJPARepository.listProductsByFilters("ANYONE", null);

        assertThat(pageOfProducts).isEmpty();
    }
}
