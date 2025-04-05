package com.emanuelvictor.erp.tenants.application.adapters.primaries.insertnewtenant;

import com.emanuelvictor.erp.tenants.application.adapters.secundaries.TTenant;
import com.emanuelvictor.erp.tenants.application.adapters.secundaries.TenantDAO;
import com.emanuelvictor.erp.tenants.application.adapters.secundaries.TenantDetails;
import com.emanuelvictor.erp.tenants.domain.Tenant;
import com.emanuelvictor.erp.tenants.infrastructure.multitenant.database.RoutingDataSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.emanuelvictor.erp.tenants.application.adapters.secundaries.TenantDAO.CENTRAL_TENANT;
import static com.emanuelvictor.erp.tenants.infrastructure.migration.MigrationService.migrate;

@Service
@RequiredArgsConstructor
public class InsertNewTenant {

    private final RoutingDataSourceService routingDataSourceService;

    public TenantDTO insertNewTenant(TenantDTO tenantDTO) {
        if (tenantDTO.database().equals(CENTRAL_TENANT.getDatabase()))
            return insertNewTenantToCentralDatabase(tenantDTO);
        return insertNewTenantWithNewDatabase(tenantDTO);
    }

    private TenantDTO insertNewTenantToCentralDatabase(TenantDTO tenantDTO) {
        Tenant tenant = createTenantInstance(tenantDTO);
        final TenantDetails tenantDetails = new TTenant(tenant.getSchema(), tenant.getDatabase(), tenant.getAddress(), CENTRAL_TENANT.getDataSource());
        TenantDAO.addNewTenant(tenantDetails);
        migrate(tenantDetails);
        routingDataSourceService.configureRoutingDataSources();
        return tenantDTO;
    }

    private TenantDTO insertNewTenantWithNewDatabase(TenantDTO tenantDTO) {
        Tenant tenant = createTenantInstance(tenantDTO);
        final TenantDetails tenantDetails = new TTenant(tenant.getSchema(), tenant.getDatabase(), tenant.getAddress(), null);
        TenantDAO.addNewTenant(tenantDetails);
        migrate(tenantDetails);
        routingDataSourceService.configureRoutingDataSources();
        return tenantDTO;
    }

    static private Tenant createTenantInstance(TenantDTO tenantDTO) {
        return Tenant.create(tenantDTO.schema(), tenantDTO.database(), tenantDTO.address());
    }
}
