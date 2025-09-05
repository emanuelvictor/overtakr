package com.emanuelvictor.stock.application.usecases.getlesssoldproducts;

import com.emanuelvictor.stock.domain.services.GetLessSoldProducts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetLessSoldProductsUseCaseImpl implements GetLessSoldProductsUseCase {

    private final GetLessSoldProducts getLessSoldProducts;

    @Override
    public Page<GetLessSoldProductsUseCase.Output> execute(Pageable pageable) {
//        getLessSoldProductsUseCase
        return null;
    }


}
