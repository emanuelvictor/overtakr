package com.emanuelvictor.stock.infrastructure.beans;

import com.emanuelvictor.stock.domain.gateways.PaymentService;
import com.emanuelvictor.stock.domain.services.GetLessSoldProducts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductBeans {

    @Bean
    public GetLessSoldProducts getLessSoldProducts(PaymentService paymentService) {
        return new GetLessSoldProducts(paymentService);
    }
}
