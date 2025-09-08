package com.emanuelvictor.stock.application.usecases;

import com.emanuelvictor.common.application.usecases.UseCase;

import java.util.UUID;

public interface InsertNewProductUseCase extends UseCase<InsertNewProductUseCase.Input, InsertNewProductUseCase.Output> {

    record Input(String name, Integer quantityAvailable) {

    }

    record Output(UUID id, String name, Integer quantityAvailable) {

    }
}
