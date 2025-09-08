package com.emanuelvictor.stock.infrastructure.jpa.repository;

import com.emanuelvictor.stock.domain.gateways.ProductRepository;
import com.emanuelvictor.stock.domain.model.ProductBuilder;
import com.emanuelvictor.stock.infrastructure.jpa.entities.ProductJPA;
import com.emanuelvictor.stock.infrastructure.jpa.repository.springdata.ProductJPARepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class ProductRepositoryImplTest {

    private final ProductJPARepository productJPARepository = mock(ProductJPARepository.class);
    private final ProductRepository productRepository = new ProductRepositoryImpl(productJPARepository);

    @Test
    void mustSaveProduct() {
        final var product = new ProductBuilder().build();
        final var captor = ArgumentCaptor.forClass(ProductJPA.class);

        productRepository.addProduct(product);

        verify(productJPARepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(product.getId());
        assertThat(captor.getValue().getName()).isEqualTo(product.getName());
        assertThat(captor.getValue().getQuantityAvailable()).isEqualTo(product.getQuantityAvailable());
    }
}
