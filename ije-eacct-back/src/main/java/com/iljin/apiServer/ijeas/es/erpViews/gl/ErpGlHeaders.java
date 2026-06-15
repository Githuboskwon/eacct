package com.iljin.apiServer.ijeas.es.erpViews.gl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(ErpGlHeadersKey.class)
@Table(name = "CBO_EA_GL_HEADERS_V")
@Entity
public class ErpGlHeaders {

    @Id
    @Column(name = "ORG_ID", nullable = false)
    Integer orgId;

    @Column(name = "LEDGER_ID", nullable = false)
    BigDecimal ledgerId;

    @Column(name = "SOURCE_NAME", nullable = false)
    String sourceName;

    @Column(name = "CATEGORY_NAME")
    String categoryName;

    @Column(name = "GL_SLIP_TYPE")
    String glSlipType;

    @Column(name = "TRX_TYPE_CODE")
    String trxTypeCode;

    @Column(name = "TRX_TYPE_NAME")
    String trxTypeName;

    @Column(name = "JE_BATCH_ID", nullable = false)
    BigDecimal jeBatchId;

    @Column(name = "BATCH_NAME", nullable = false)
    String batchName;

    @Column(name = "BATCH_STATUS")
    String batchStatus;

    @Id
    @Column(name = "JE_HEADER_ID", nullable = false)
    BigDecimal jeHeaderId;

    @Column(name = "HEADER_NAME")
    String headerName;

    @Column(name = "REVERSE_FLAG")
    String reverseFlag;

    @Column(name = "REVERSE_HEADER_NAME")
    String reverseHeaderName;

    @Column(name = "HEADER_DESCRIPTION")
    String headerDescription;

    @Column(name = "HEADER_STATUS")
    String headerStatus;

    @Column(name = "POSTED_DATE")
    LocalDateTime postedDate;

    @Column(name = "CURRENCY_CODE", nullable = false)
    String currencyCode;

    @Column(name = "CURRENCY_RATE")
    BigDecimal currencyRate;

    @Column(name = "CURRENCY_RATE_TYPE")
    String currencyRateType;

    @Column(name = "CURRENCY_DATE")
    LocalDateTime currencyDate;

    @Column(name = "GL_DATE", nullable = false)
    LocalDateTime glDate;

    @Column(name = "FOR_TOTAL_DR")
    BigDecimal forTotalDr;

    @Column(name = "FOR_TOTAL_CR")
    BigDecimal forTotalCr;

    @Column(name = "KRW_TOTAL_DR")
    BigDecimal krwTotalDr;

    @Column(name = "KRW_TOTAL_CR")
    BigDecimal krwTotalCr;

    @Column(name = "CREATION_DATE")
    LocalDateTime creationDate;

    @Column(name = "USER_NAME", nullable = false)
    String userName;

    @Column(name = "FULL_NAME")
    String fullName;

    @Column(name = "EA_SLIP_NO")
    String eaSlipNo;

}
