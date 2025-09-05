package com.emanuelvictor.common.application.usecases;

@FunctionalInterface
public interface UnitUseCase<Input> {
    void execute(Input input);
}
