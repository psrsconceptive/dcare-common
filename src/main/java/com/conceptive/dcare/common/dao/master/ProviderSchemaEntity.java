package com.conceptive.dcare.common.dao.master;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ProviderSchema")
@Data
@Builder
public class ProviderSchemaEntity {

    @Id
    @Column(name="PROVIDER_ID")
    @Size(max=30)
    private String providerId;

    @Id
    @Column(name="SCHEMA")
    @Size(max=30)
    private String schema;


}
