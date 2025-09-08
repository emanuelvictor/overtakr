package com.emanuelvictor.stock.infrastructure.rest;

import com.emanuelvictor.common.infrastructure.audit.repository.IRevisionRepository;
import com.emanuelvictor.stock.infrastructure.jpa.entities.ProductJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/stocks/products")
public class GetRevisionsFromProductIdRest {

    private final IRevisionRepository<ProductJPA> productJPARevisionRepository;

    @GetMapping("{id}/revisions")
//    @PreAuthorize("hasAnyAuthority('root.stocks.products.read','root.stocks.products','root.stocks.products','root.stocks','root')")
    public List<?> getAllRevisions(@PathVariable("id") UUID productId) {
        return productJPARevisionRepository.findRevisionsById(productId);
    }
}
