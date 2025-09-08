package com.emanuelvictor.stock.infrastructure.jpa.repository.revisions;

import com.emanuelvictor.common.infrastructure.audit.repository.AbstractRevisionRepository;
import com.emanuelvictor.stock.infrastructure.jpa.entities.ProductJPA;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRevisionImpl extends AbstractRevisionRepository<ProductJPA> {

}
