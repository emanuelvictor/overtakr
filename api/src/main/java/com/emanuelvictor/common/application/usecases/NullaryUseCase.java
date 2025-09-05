package com.emanuelvictor.common.application.usecases;

@FunctionalInterface
public interface NullaryUseCase<Output> {
    Output execute();
}
