package com.emanuelvictor.erp.stocks.application.adapters.secundaries;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TProductRepository extends JpaRepository<TProduct, UUID> {
}
