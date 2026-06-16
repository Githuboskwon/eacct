package com.iljin.apiServer.ijeas.es.erpViews.frgn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(ErpSpOsSlipKey.class)
@Table(name = "CBO_SP_OS_SLIP_V")
@Entity
public class ErpSpOsSlip {

    @Id
    @Column(name = "SLIP_TYPE")
    String slipType;

    @Id
    @Column(name = "SLIP_ID")
    BigDecimal slipId;

    @Column(name = "SLIP_NUMBER")
    String slipNumber;

    @Column(name = "GL_DATE")
    LocalDateTime glDate;

    @Column(name = "INTEGRATION_VENDOR_NUM")
    String integrationVendorNum;

    @Column(name = "INTEGRATION_VENDOR_NAME")
    String integrationVendorName;

    @Column(name = "SLIP_CREATED_BY")
    String slipCreatedBy;

    @Column(name = "SLIP_CREATED_NAME")
    String slipCreatedName;

    @Column(name = "COA_SEGMENT5")
    String coaSegment5;

    @Column(name = "PROJECT_CODE")
    String projectCode;

    @Column(name = "PROJECT_NAME")
    String projectName;

    @Column(name = "CURRENCY_CODE")
    String currencyCode;

    @Column(name = "ENTERED_AMT")
    BigDecimal enteredAmt;

    @Column(name = "ACCOUNTED_AMT")
    BigDecimal accountedAmt;

    @Column(name = "PERIOD_NAME")
    String periodName;

    @Column(name = "LEDGER_ID")
    Integer ledgerId;

    @Column(name = "PARTITION_KEY")
    String partitionKey;

}
