package com.emanuelvictor.payment.application.usecases.getpaymentsbyproductid;

import com.emanuelvictor.common.application.usecases.UseCase;

import java.math.BigDecimal;

public interface GetPaymentsByProductIdUseCase extends UseCase<GetPaymentsByProductIdUseCase.Input, GetPaymentsByProductIdUseCase.Output> {

    public record Input(String productId) {
    }

    public record Output(BigDecimal value) {
    }
}
