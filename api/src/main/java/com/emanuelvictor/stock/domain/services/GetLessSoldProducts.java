package com.emanuelvictor.stock.domain.services;

import com.emanuelvictor.stock.domain.gateways.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@RequiredArgsConstructor // TODO verificar possibilidade de acoplar o lombok
public class GetLessSoldProducts {

    private final PaymentService paymentService;

    /**
     * TODO Deveria haver uma camada de tradução aqui, mas é só para entender como funciona um serviço de domínio
     *
     * @param productId
     * @return
     */
    public Page<PaymentService.PaymentFromProductId> execute(final String productId) {
        final var pageOfProducts = paymentService.getPaymentsByProductId(productId);
        // faz processamento x, y, z em pageOfProducts
        return pageOfProducts;
    }
}
