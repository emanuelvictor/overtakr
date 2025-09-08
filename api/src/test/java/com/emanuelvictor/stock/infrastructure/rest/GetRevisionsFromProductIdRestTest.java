package com.emanuelvictor.stock.infrastructure.rest;

import com.emanuelvictor.SpringBootTests;
import com.emanuelvictor.common.infrastructure.audit.repository.RevisionDTO;
import com.emanuelvictor.stock.domain.gateways.ProductRepository;
import com.emanuelvictor.stock.domain.model.ProductBuilder;
import com.emanuelvictor.stock.infrastructure.jpa.repository.springdata.ProductJPARepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;

public class GetRevisionsFromProductIdRestTest extends SpringBootTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductJPARepository productJPARepository;

    @BeforeEach
    void setUp() {
        productJPARepository.deleteAll();
    }

    @Test
    void mustReturnTheRevisionsFromOneProject() throws Exception {
        final var productOne = new ProductBuilder().build();
        productRepository.addProduct(productOne);
        final var productTwo = new ProductBuilder().build();
        productRepository.addProduct(productTwo);
        final var productThree = new ProductBuilder().build();
        productRepository.addProduct(productThree);
        productOne.updateData("Updated Name One" , null);
        productRepository.updateProduct(productOne);
        productTwo.updateData("Updated Name Two", null);
        productRepository.updateProduct(productTwo);
        productThree.updateData("Updated Name Three", null);
        productRepository.updateProduct(productThree);

        final var result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/stocks/products/" + productOne.getId() + "/revisions")
                .with(oauth2Login().authorities((GrantedAuthority) () -> "root"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        final var payloadReturned = result.andReturn().getResponse().getContentAsString();
        final var revisions = objectMapper.readValue(payloadReturned, new TypeReference<List<RevisionDTO>>() {
        });
        assertThat(revisions).hasSize(2);
        // TODO Increment more asserts
    }
}

