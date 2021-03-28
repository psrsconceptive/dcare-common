package com.conceptive.dcare.common.dao;

import com.conceptive.dcare.common.dao.master.ProviderSchemaEntity;
import com.conceptive.dcare.common.model.ProviderSchema;

public class ProviderSchemaMapper {

    public static ProviderSchema newProviderSchema(ProviderSchemaEntity entity){
        return ProviderSchema.builder()
                .providerId(entity.getProviderId())
                .schema(entity.getSchema())
                .build();
    }
}
