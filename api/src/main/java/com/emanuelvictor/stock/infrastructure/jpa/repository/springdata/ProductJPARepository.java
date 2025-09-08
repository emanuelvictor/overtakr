package com.emanuelvictor.stock.infrastructure.jpa.repository.springdata;

import com.emanuelvictor.stock.infrastructure.jpa.entities.ProductJPA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ProductJPARepository extends JpaRepository<ProductJPA, UUID> {

    /**
     * @param filter   String
     * @param pageable Pageable
     * @return Page<ProductJPA>
     */
    @Query("FROM ProductJPA product WHERE" +
            "   (" +
            "       FILTER(:filter, product.name) = TRUE" +
            "   )"
    )
    Page<ProductJPA> listProductsByFilters(String filter, Pageable pageable);
}
