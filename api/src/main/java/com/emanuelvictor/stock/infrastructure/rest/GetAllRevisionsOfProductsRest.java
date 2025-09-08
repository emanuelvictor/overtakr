package com.emanuelvictor.stock.infrastructure.rest;

import com.emanuelvictor.common.infrastructure.audit.repository.IRevisionRepository;
import com.emanuelvictor.stock.infrastructure.jpa.entities.ProductJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/stocks/products")
public class GetAllRevisionsOfProductsRest {

    private final IRevisionRepository<ProductJPA> productJPARevisionRepository;

    @GetMapping("revisions")
//    @PreAuthorize("hasAnyAuthority('root.stocks.products.read','root.stocks.products','root.stocks.products','root.stocks','root')")
    public List<?> getAllRevisions() {
        return productJPARevisionRepository.findRevisions();
    }
}
