package com.emanuelvictor.stock.application.services;

import com.emanuelvictor.payment.application.usecases.getpaymentsbyproductid.GetPaymentsByProductIdUseCase;
import com.emanuelvictor.stock.domain.gateways.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * É a implementação do serviço de domínio PaymentService
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final GetPaymentsByProductIdUseCase getPaymentsByProductIdUseCase;

    @Override
    public Page<PaymentFromProductId> getPaymentsByProductId(String productId) {
//        getPaymentsByProductId.execute()
        return null;
    }
}
