package com.emanuelvictor.stock.application.usecases;

import com.emanuelvictor.common.application.usecases.UseCase;

import java.util.UUID;

public interface UpdateProductUseCase extends UseCase<UpdateProductUseCase.Input, UpdateProductUseCase.Output> {

    record Input(UUID id, String name, Integer quantityAvailable) {

    }

    record Output(UUID id, String name, Integer quantityAvailable) {

    }
}
