package com.iljin.apiServer.ijeas.system.trx;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Table(name = "APPS.CBO_SP_AWT_TAX_CODE_V")
@Entity
public class CboSpAwtTaxCodeV implements Serializable {
    private static final long serialVersionUID = -5089554689283464287L;

    @Id
    @Column(name = "ORG_ID")
    BigDecimal orgId;

    @Id
    @Column(name = "GROUP_ID")
    BigDecimal groupId;

    @Id
    @Column(name = "TAX_RATE_ID")
    BigDecimal taxRateId;

    @Column(name = "TAX_NAME")
    String taxName;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "AWT_ACCOUNT")
    String awtAccount;

    @Column(name = "AWT_ACCOUNT_NAME")
    String awtAccountName;

    @Column(name = "TAX_RATE")
    BigDecimal taxRate;

    @Column(name = "LEDGER_ID")
    BigDecimal ledgerId;

    @Column(name = "LOCATION_CODE")
    String locationCode;

    @Column(name = "vendorName")
    String vendorName;

    @Column(name = "vendorSiteName")
    String vendorSiteName;

    @Column(name = "TAX_CODE_COA")
    String taxCodeCoa;

}
