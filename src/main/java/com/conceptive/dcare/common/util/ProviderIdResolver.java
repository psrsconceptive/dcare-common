package com.conceptive.dcare.common.util;

import com.conceptive.dcare.common.model.ProviderContext;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class ProviderIdResolver implements CurrentTenantIdentifierResolver {
    private static final String DEFAULT_TENANT_ID="";
    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = ProviderContext.getProviderId();
        return tenantId!=null?tenantId:DEFAULT_TENANT_ID;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
