package com.emanuelvictor.erp.tenants.application.adapters.primaries.getalltenants;

import com.emanuelvictor.erp.tenants.application.adapters.secundaries.TenantDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetAllTenantsRest {

    @GetMapping("api/tenants")
    @Transactional(readOnly = true)
    public List<TenantDTO> getAllTenants() {
        final List<TenantDTO> tenantDTOs = new ArrayList<>();
        TenantDAO.getAllCostumerTenants().forEach((schema, tenantDetails) ->
                tenantDTOs.add(new TenantDTO(schema, tenantDetails.getDatabase(), tenantDetails.getAddress())));
        return tenantDTOs;
    }
}
