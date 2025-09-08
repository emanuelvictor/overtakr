package com.emanuelvictor.stock.application.usecases;

import com.emanuelvictor.common.application.usecases.UnitUseCase;
import com.emanuelvictor.common.application.usecases.UseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface RemoveProductUseCase extends UnitUseCase<UUID> {

}
