package com.emanuelvictor.common.application.usecases;

@FunctionalInterface
public interface UseCase<Input, Output> {
    Output execute(Input input);
}
