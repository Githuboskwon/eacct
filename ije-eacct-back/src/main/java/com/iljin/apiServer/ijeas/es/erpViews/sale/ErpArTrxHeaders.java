package com.iljin.apiServer.ijeas.es.erpViews.sale;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(ErpArTrxHeadersKey.class)
@Table(name = "CBO_SP_AR_TRX_HEADER_V")
@Entity
public class ErpArTrxHeaders {

    @Id
    @Column(name = "SLIP_TYPE")
    String slipType;

    @Id
    @Column(name = "SLIP_ID")
    BigDecimal slipId;

    @Column(name = "SLIP_NUMBER" , nullable = false)
    String slipNumber;

    @Column(name = "GL_DATE" , nullable = false)
    LocalDateTime glDate;

    @Column(name = "INTEGRATION_VENDOR_NUM" , nullable = false)
    String integrationVendorNum;

    @Column(name = "INTEGRATION_VENDOR_NAME")
    String integrationVendorName;

    @Column(name = "SLIP_CREATED_BY" , nullable = false)
    String slipCreatedBy;

    @Column(name = "SLIP_CREATED_NAME")
    String slipCreatedName;

    @Column(name = "COA_SEGMENT5" , nullable = false)
    String coaSegment5;

    @Column(name = "PROJECT_CODE" , nullable = false)
    String projectCode;

    @Column(name = "PROJECT_NAME")
    String projectName;

    @Column(name = "CURRENCY_CODE" , nullable = false)
    String currencyCode;

    @Column(name = "ENTERED_AMT")
    BigDecimal enteredAmt;

    @Column(name = "ACCOUNTED_AMT")
    BigDecimal accountedAmt;

    @Column(name = "PERIOD_NAME" , nullable = false)
    String periodName;

    @Column(name = "LEDGER_ID" , nullable = false)
    Integer ledgerId;

    @Column(name = "PARTITION_KEY" , nullable = false)
    String partitionKey;

    @Column(name = "ORG_ID")
    Integer orgId;

    @Column(name = "COA_SEGMENT4")
    String coaSegment4;

    @Column(name = "COA_SEGMENT4_NAME")
    String coaSegment4Name;

    @Column(name = "TAX_CODE")
    String taxCode;

    @Column(name = "INCOTERMS")
    String incoterms;

    @Column(name = "SLIP_DEPT_CODE")
    String slipDeptCode;

    @Column(name = "SLIP_DEPT_NAME")
    String slipDeptName;

    @Column(name = "TASK_CODE")
    String taskCode;

    @Column(name = "TASK_NAME")
    String taskName;
}
