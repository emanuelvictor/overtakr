package com.emanuelvictor.erp.tenants.application.adapters.primaries.insertnewtenant;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class InsertNewTenantRest {

    private final InsertNewTenant insertNewTenant;

    @Transactional
    @PostMapping("api/tenants")
    public ResponseEntity<TenantDTO> insertNewTenant(@RequestBody TenantDTO tenantDTO) {
        return new ResponseEntity<>(insertNewTenant.insertNewTenant(tenantDTO), CREATED);
    }
}
