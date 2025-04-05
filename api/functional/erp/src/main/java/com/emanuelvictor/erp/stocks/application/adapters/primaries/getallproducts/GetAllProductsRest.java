package com.emanuelvictor.erp.stocks.application.adapters.primaries.getallproducts;

import com.emanuelvictor.erp.stocks.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetAllProductsRest {

    private final ProductRepository productRepository;

    @GetMapping("api/products")
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepository.getAllProducts().stream()
                .map(product -> new ProductDTO(product.getName())).toList();
    }
}
