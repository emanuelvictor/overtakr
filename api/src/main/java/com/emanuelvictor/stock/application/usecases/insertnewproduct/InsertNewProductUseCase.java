package com.emanuelvictor.stock.application.usecases.insertnewproduct;

import com.emanuelvictor.common.application.usecases.UseCase;

public interface InsertNewProductUseCase extends UseCase<InsertNewProductUseCase.Input, InsertNewProductUseCase.Output> {

    record Input(String name, Integer quantityAvailable) {

    }

    record Output(String name, Integer quantityAvailable) {

    }
}
