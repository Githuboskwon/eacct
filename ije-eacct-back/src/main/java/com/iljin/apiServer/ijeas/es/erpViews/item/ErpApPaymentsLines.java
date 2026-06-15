package com.iljin.apiServer.ijeas.es.erpViews.item;

import java.math.BigDecimal;
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
@IdClass(ErpApPaymentsLinesKey.class)
@Table(name = "CBO_SP_AP_PAYMENT_LINES_V")
@Entity
public class ErpApPaymentsLines {

    @Id
    @Column(name = "CHECK_ID")
    BigDecimal checkId;

    @Id
    @Column(name = "AE_HEADER_ID")
    Long aeHeaderId;

    @Column(name = "AE_LINE_ID")
    Long aeLineId;

    @Id
    @Column(name = "AE_LINE_NUMBER")
    Long aeLineNumber;

    @Column(name = "SEGMENT3")
    String segment3;

    @Column(name = "SEGMENT4")
    String segment4;

    @Column(name = "SEGMENT5")
    String segment5;

    @Column(name = "SEGMENT3_DESC")
    String segment3Desc;

    @Column(name = "SEGMENT4_DESC")
    String segment4Desc;

    @Column(name = "SEGMENT5_DESC")
    String segment5Desc;

    @Column(name = "ENTERED_DR")
    Double enteredDr;

    @Column(name = "ENTERED_CR")
    Double enteredCr;

    @Column(name = "ACCOUNTED_DR")
    Double accountedDr;

    @Column(name = "ACCOUNTED_CR")
    Double accountedCr;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "CURRENCY_CODE")
    String currencyCode;

    @Column(name = "CURRENCY_CONVERSION_RATE")
    Long currencyConversionRate;

    @Column(name = "CURRENCY_CONVERSION_TYPE")
    String currencyConversionType;

    @Column(name = "ORG_ID")
    Integer orgId;

    @Column(name = "ATTRIBUTE1")
    String attribute1;

    @Column(name = "ATTRIBUTE2")
    String attribute2;

    @Column(name = "ATTRIBUTE3")
    String attribute3;

    @Column(name = "ATTRIBUTE4")
    String attribute4;

    @Column(name = "ATTRIBUTE5")
    String attribute5;

}
