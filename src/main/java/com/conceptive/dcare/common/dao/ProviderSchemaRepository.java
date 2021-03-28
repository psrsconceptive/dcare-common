package com.conceptive.dcare.common.dao;

import com.conceptive.dcare.common.dao.master.ProviderSchemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProviderSchemaRepository extends JpaRepository<ProviderSchemaEntity,String> {

    Optional<ProviderSchemaEntity>findProviderSchemaByProviderId(@Param("providerId") String providerId);
}
