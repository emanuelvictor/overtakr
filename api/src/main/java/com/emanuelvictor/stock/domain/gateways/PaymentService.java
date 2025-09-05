package com.emanuelvictor.stock.domain.gateways;

import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface PaymentService {

    Page<PaymentFromProductId> getPaymentsByProductId(final String productId);

    record PaymentFromProductId(BigDecimal value) {
    }
}
