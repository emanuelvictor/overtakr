package com.emanuelvictor.stock.application.usecases.getlesssoldproducts;

import com.emanuelvictor.common.application.usecases.UseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetLessSoldProductsUseCase extends UseCase<Pageable, Page<GetLessSoldProductsUseCase.Output>> {

    public record Output(String productId, String name) {

    }
}
