package com.iljin.apiServer.ijeas.system.trx;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Table(name = "CBO_SP_LOCATION_V")
@Entity
public class CboSpLocationV implements Serializable {
    private static final long serialVersionUID = 3659363962681451981L;

    @Id
    @Column(name = "LOCATION_ID")
    Integer locationId;

    @Column(name = "LOCATION_CODE")
    String locationCode;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "INVENTORY_ORGANIZATION_ID")
    Integer inventoryOrganizationId;

    @Column(name = "VAT_REGISTRATION_NUM")
    String vatRegistrationNum;

    @Column(name = "ORG_ID")
    Integer orgId;

    @Column(name = "LOCATION_TYPE")
    String locationType;

    @Column(name = "REPRESENTATIVE_NAME")
    String representativeName;

    @Column(name = "BUSINESS_TYPE")
    String businessType;

    @Column(name = "BUSINESS_CONDITION")
    String businessCondition;

    @Column(name = "LEGAL_ADDRESS_FLAG")
    String legalAddressFlag;

    @Column(name = "GLOBAL_ATTRIBUTE10")
    String globalAttribute10;
}
