package com.emanuelvictor.erp.common.filters;

import com.emanuelvictor.erp.tenants.infrastructure.multitenant.schema.TenantIdentifierResolver;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import static com.emanuelvictor.erp.tenants.application.adapters.secundaries.TenantDAO.CENTRAL_TENANT;


@RequiredArgsConstructor
public class TenantFilter implements Filter {

    private final TenantIdentifierResolver tenantIdentifierResolver;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String schema = ((HttpServletRequest) request).getHeader("schema");
        if (schema != null) {
            tenantIdentifierResolver.setTenant(schema);
        } else
            tenantIdentifierResolver.setTenant(CENTRAL_TENANT.getSchema());
        chain.doFilter(request, response);
    }
}