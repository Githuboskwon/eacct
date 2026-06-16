package com.iljin.apiServer.ijeas.es.item;

import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.es.SlipType;
import com.iljin.apiServer.ijeas.es.erpViews.item.ErpApPaymentsLines;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@IdClass(ApPaymentsItemKey.class)
@Table(name = "TB_AP_PAYMENTS_ITEM")
@Entity
public class ApPaymentsItem extends BaseEntity {

    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    @Id
    @Column(name = "SLIP_NO", nullable = false)
    String slipNo;

    @Id
    @Column(name = "SLIP_TYPE", nullable = false)
    String slipType;

    @Id
    @Column(name = "CHECK_ID", nullable = false)
    BigDecimal checkId;

    @Id
    @Column(name = "AE_HEADER_ID", nullable = false)
    Long aeHeaderId;

    @Id
    @Column(name = "AE_LINE_ID", nullable = false)
    Long aeLineId;

    @Id
    @Column(name = "AE_LINE_NO", nullable = false)
    Long aeLineNo;

    @Column(name = "SLIP_HEADER_ID")
    BigDecimal slipHeaderId;

    @Column(name = "SEGMENT3")
    String segment3;

    @Column(name = "SEGMENT3_DESC")
    String segment3Desc;

    @Column(name = "SEGMENT4")
    String segment4;

    @Column(name = "SEGMENT4_DESC")
    String segment4Desc;

    @Column(name = "SEGMENT5")
    String segment5;

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

    @Column(name = "CURRENCY_CD")
    String currencyCd;

    @Column(name = "CURRENCY_CONVERSION_RATE")
    Long currencyConversionRate;

    @Column(name = "CURRENCY_CONVERSION_TYPE")
    String currencyConversionType;

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

    @Builder
    public ApPaymentsItem(String compCd, String slipNo, String slipType, BigDecimal checkId,
        Long aeHeaderId, Long aeLineId, Long aeLineNo, BigDecimal slipHeaderId, String segment3,
        String segment3Desc, String segment4, String segment4Desc, String segment5,
        String segment5Desc, Double enteredDr, Double enteredCr, Double accountedDr,
        Double accountedCr, String description, String currencyCd, Long currencyConversionRate,
        String currencyConversionType, String attribute1, String attribute2, String attribute3,
        String attribute4, String attribute5) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.checkId = checkId;
        this.aeHeaderId = aeHeaderId;
        this.aeLineId = aeLineId;
        this.aeLineNo = aeLineNo;
        this.slipHeaderId = slipHeaderId;
        this.segment3 = segment3;
        this.segment3Desc = segment3Desc;
        this.segment4 = segment4;
        this.segment4Desc = segment4Desc;
        this.segment5 = segment5;
        this.segment5Desc = segment5Desc;
        this.enteredDr = enteredDr;
        this.enteredCr = enteredCr;
        this.accountedDr = accountedDr;
        this.accountedCr = accountedCr;
        this.description = description;
        this.currencyCd = currencyCd;
        this.currencyConversionRate = currencyConversionRate;
        this.currencyConversionType = currencyConversionType;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
    }

    public static ApPaymentsItem from(ErpApPaymentsLines erpLines, String slipNo, BigDecimal slipHeaderId) {
        return ApPaymentsItem.builder()
            .slipNo(slipNo)
            .compCd(String.valueOf(erpLines.getOrgId()))
            .slipType(SlipType.ITEM.getCode())
            .slipHeaderId(slipHeaderId)
            .checkId(erpLines.getCheckId())
            .aeHeaderId(erpLines.getAeHeaderId())
            .aeLineId(erpLines.getAeLineId())
            .aeLineNo(erpLines.getAeLineNumber())
            .segment3(erpLines.getSegment3())
            .segment3Desc(erpLines.getSegment3Desc())
            .segment4(erpLines.getSegment4())
            .segment4Desc(erpLines.getSegment4Desc())
            .segment5(erpLines.getSegment5())
            .segment5Desc(erpLines.getSegment5Desc())
            .enteredDr(erpLines.getEnteredDr())
            .enteredCr(erpLines.getEnteredCr())
            .accountedDr(erpLines.getAccountedDr())
            .accountedCr(erpLines.getAccountedCr())
            .description(erpLines.getDescription())
            .currencyCd(erpLines.getCurrencyCode())
            .currencyConversionRate(erpLines.getCurrencyConversionRate())
            .currencyConversionType(erpLines.getCurrencyConversionType())
            .attribute1(erpLines.getAttribute1())
            .attribute2(erpLines.getAttribute2())
            .attribute3(erpLines.getAttribute3())
            .attribute4(erpLines.getAttribute4())
            .attribute5(erpLines.getAttribute5())
            .build();
    }
}
