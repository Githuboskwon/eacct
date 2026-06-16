package com.iljin.apiServer.ijeas.es.gl;

import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.es.SlipType;
import com.iljin.apiServer.ijeas.es.erpViews.gl.ErpGlHeaders;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@IdClass(GlHeaderKey.class)
@Table(name = "TB_GL_HD")
@Entity
public class GlHeader extends BaseEntity {

    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    @Id
    @Column(name = "SLIP_NO", nullable = false)
    String slipNo;

    @Id
    @Column(name = "SLIP_TYPE", nullable = false)
    String slipType;

    @Column(name = "SLIP_HEADER_ID")
    BigDecimal slipHeaderId;

    @Column(name = "JE_HEADER_ID")
    BigDecimal jeHeaderId;

    @Column(name = "HEADER_NM")
    String headerNm;

    @Column(name = "LEDGER_ID")
    BigDecimal ledgerId;

    @Column(name = "SOURCE_NM")
    String sourceNm;

    @Column(name = "CATEGORY_NM")
    String categoryNm;

    @Column(name = "GL_SLIP_TYPE")
    String glSlipType;

    @Column(name = "TRX_TYPE_CD")
    String trxTypeCd;

    @Column(name = "TRX_TYPE_NM")
    String trxTypeNm;

    @Column(name = "JE_BATCH_ID")
    BigDecimal jeBatchId;

    @Column(name = "BATCH_NM")
    String batchNm;

    @Column(name = "BATCH_STATUS")
    String batchStatus;

    @Column(name = "REVERSE_FLAG")
    String reverseFlag;

    @Column(name = "REVERSE_HEADER_NM")
    String reverseHeaderNm;

    @Column(name = "HEADER_REMARK")
    String headerRemark;

    @Column(name = "HEADER_STATUS")
    String headerStatus;

    @Column(name = "POSTED_DT")
    LocalDateTime postedDt;

    @Column(name = "CURRENCY_CD")
    String currencyCd;

    @Column(name = "CURRENCY_RATE")
    BigDecimal currencyRate;

    @Column(name = "CURRENCY_RATE_TYPE")
    String currencyRateType;

    @Column(name = "CURRENCY_DT")
    LocalDateTime currencyDt;

    @Column(name = "GL_DT")
    LocalDateTime glDt;

    @Column(name = "FOR_TOTAL_DR")
    BigDecimal forTotalDr;

    @Column(name = "FOR_TOTAL_CR")
    BigDecimal forTotalCr;

    @Column(name = "KRW_TOTAL_DR")
    BigDecimal krwTotalDr;

    @Column(name = "KRW_TOTAL_CR")
    BigDecimal krwTotalCr;

    @Builder
    public GlHeader(String compCd, String slipNo, String slipType, BigDecimal slipHeaderId,
        BigDecimal jeHeaderId, String headerNm, BigDecimal ledgerId, String sourceNm,
        String categoryNm,
        String glSlipType, String trxTypeCd, String trxTypeNm, BigDecimal jeBatchId, String batchNm,
        String batchStatus, String reverseFlag, String reverseHeaderNm, String headerRemark,
        String headerStatus, LocalDateTime postedDt, String currencyCd, BigDecimal currencyRate,
        String currencyRateType, LocalDateTime currencyDt, LocalDateTime glDt,
        BigDecimal forTotalDr,
        BigDecimal forTotalCr, BigDecimal krwTotalDr, BigDecimal krwTotalCr) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipHeaderId = slipHeaderId;
        this.jeHeaderId = jeHeaderId;
        this.headerNm = headerNm;
        this.ledgerId = ledgerId;
        this.sourceNm = sourceNm;
        this.categoryNm = categoryNm;
        this.glSlipType = glSlipType;
        this.trxTypeCd = trxTypeCd;
        this.trxTypeNm = trxTypeNm;
        this.jeBatchId = jeBatchId;
        this.batchNm = batchNm;
        this.batchStatus = batchStatus;
        this.reverseFlag = reverseFlag;
        this.reverseHeaderNm = reverseHeaderNm;
        this.headerRemark = headerRemark;
        this.headerStatus = headerStatus;
        this.postedDt = postedDt;
        this.currencyCd = currencyCd;
        this.currencyRate = currencyRate;
        this.currencyRateType = currencyRateType;
        this.currencyDt = currencyDt;
        this.glDt = glDt;
        this.forTotalDr = forTotalDr;
        this.forTotalCr = forTotalCr;
        this.krwTotalDr = krwTotalDr;
        this.krwTotalCr = krwTotalCr;
    }

    public static GlHeader from(ErpGlHeaders erpGlHeaders, String slipNo, BigDecimal slipHeaderId) {
        return GlHeader.builder()
            .compCd(String.valueOf(erpGlHeaders.getOrgId()))
            .slipNo(slipNo)
            .slipType(SlipType.GL.getCode())
            .slipHeaderId(slipHeaderId)
            .jeHeaderId(erpGlHeaders.getJeHeaderId())
            .headerNm(erpGlHeaders.getHeaderName())
            .ledgerId(erpGlHeaders.getLedgerId())
            .sourceNm(erpGlHeaders.getSourceName())
            .categoryNm(erpGlHeaders.getCategoryName())
            .glSlipType(erpGlHeaders.getGlSlipType())
            .trxTypeCd(erpGlHeaders.getTrxTypeCode())
            .trxTypeNm(erpGlHeaders.getTrxTypeName())
            .jeBatchId(erpGlHeaders.getJeBatchId())
            .batchNm(erpGlHeaders.getBatchName())
            .batchStatus(erpGlHeaders.getBatchStatus())
            .reverseFlag(erpGlHeaders.getReverseFlag())
            .reverseHeaderNm(erpGlHeaders.getReverseHeaderName())
            .headerRemark(erpGlHeaders.getHeaderDescription())
            .headerStatus(erpGlHeaders.getHeaderStatus())
            .postedDt(erpGlHeaders.getPostedDate())
            .currencyCd(erpGlHeaders.getCurrencyCode())
            .currencyRate(erpGlHeaders.getCurrencyRate())
            .currencyRateType(erpGlHeaders.getCurrencyRateType())
            .currencyDt(erpGlHeaders.getCurrencyDate())
            .glDt(erpGlHeaders.getGlDate())
            .forTotalDr(erpGlHeaders.getForTotalDr())
            .forTotalCr(erpGlHeaders.getForTotalCr())
            .krwTotalDr(erpGlHeaders.getKrwTotalDr())
            .krwTotalCr(erpGlHeaders.getKrwTotalCr())
            .build();
    }
}
