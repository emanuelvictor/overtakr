package com.emanuelvictor.erp.common.web;

import com.emanuelvictor.erp.common.filters.TenantFilter;
import com.emanuelvictor.erp.tenants.infrastructure.multitenant.schema.TenantIdentifierResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

    @Bean
    public FilterRegistrationBean<?> tenantFilter(final TenantIdentifierResolver tenantIdentifierResolver) {
        final FilterRegistrationBean<TenantFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantFilter(tenantIdentifierResolver));
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

}