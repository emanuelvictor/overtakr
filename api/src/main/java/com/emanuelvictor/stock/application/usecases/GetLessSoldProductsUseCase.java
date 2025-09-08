package com.emanuelvictor.stock.application.usecases;

import com.emanuelvictor.common.application.usecases.UseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetLessSoldProductsUseCase extends UseCase<Pageable, Page<GetLessSoldProductsUseCase.Output>> {

    record Output(String productId, String name) {

    }
}
