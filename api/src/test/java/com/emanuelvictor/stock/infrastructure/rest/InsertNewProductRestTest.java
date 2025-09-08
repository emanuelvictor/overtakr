package com.emanuelvictor.stock.infrastructure.rest;

import com.emanuelvictor.SpringBootTests;
import com.emanuelvictor.stock.infrastructure.jpa.repository.springdata.ProductJPARepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InsertNewProductRestTest extends SpringBootTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductJPARepository productJPARepository;

    @Test
    void mustInsertNewProduct() throws Exception {
        final var name = "Product A";
        final var quantityAvailable = 10;
        final var input = new InsertNewProductRest.ProductRequest(name, quantityAvailable);

        final var result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/stocks/products")
                .content(objectMapper.writeValueAsString(input))
                .with(oauth2Login()
                        .authorities((GrantedAuthority) () -> "root")
                )
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        final var payloadReturned = result.andReturn().getResponse().getContentAsString();
        final var productResponse = objectMapper.readValue(payloadReturned, InsertNewProductRest.ProductResponse.class);
        result.andExpect(status().isCreated());
        assertThat(productResponse.id()).isNotNull();
        assertThat(productResponse.name()).isEqualTo(name);
        assertThat(productResponse.quantityAvailable()).isEqualTo(quantityAvailable);
        assertThat(productJPARepository.findById(productResponse.id())).isPresent();
    }

    @Test
    public void cannotBeAccessibleWithoutRequiredPermissions() throws Exception {
        final var name = "Product A";
        final var quantityAvailable = 10;
        final var input = new InsertNewProductRest.ProductRequest(name, quantityAvailable);

        final var result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/stocks/products")
                .content(objectMapper.writeValueAsString(input))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        result.andExpect(status().isUnauthorized());
    }
}
