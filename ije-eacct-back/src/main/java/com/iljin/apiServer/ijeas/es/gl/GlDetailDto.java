package com.iljin.apiServer.ijeas.es.gl;

import com.iljin.apiServer.ijeas.es.erpViews.gl.ErpGlLines;
import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GlDetailDto implements Serializable {

    private static final long serialVersionUID = 9164858038570634238L;

    String compCd;

    String slipNo;

    String slipType;

    Integer slipSeq;

    BigDecimal jeHeaderId;

    String status;

    String sourceNm;

    String categoryNm;

    BigDecimal jeBatchId;

    String batchNm;

    String headerNm;

    String currencyCd;

    BigDecimal currencyRate;

    LocalDateTime glDt;

    BigDecimal ccid;

    String accountCd;

    String accountNm;

    BigDecimal enteredDr;

    BigDecimal enteredCr;

    BigDecimal accountedDr;

    BigDecimal accountedCr;

    String remark;

    String coaCd;

    String coaDesc;


    @Builder
    @QueryProjection
    public GlDetailDto(String compCd, String slipNo, String slipType, Integer slipSeq,
        BigDecimal jeHeaderId, String status, String sourceNm, String categoryNm,
        BigDecimal jeBatchId,
        String batchNm, String headerNm, String currencyCd, BigDecimal currencyRate,
        LocalDateTime glDt, BigDecimal ccid, String accountCd,
        String accountNm, BigDecimal enteredDr, BigDecimal enteredCr, BigDecimal accountedDr,
        BigDecimal accountedCr, String remark, String coaCd, String coaDesc) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipSeq = slipSeq;
        this.jeHeaderId = jeHeaderId;
        this.status = status;
        this.sourceNm = sourceNm;
        this.categoryNm = categoryNm;
        this.jeBatchId = jeBatchId;
        this.batchNm = batchNm;
        this.headerNm = headerNm;
        this.currencyCd = currencyCd;
        this.currencyRate = currencyRate;
        this.glDt = glDt;
        this.ccid = ccid;
        this.accountCd = accountCd;
        this.accountNm = accountNm;
        this.enteredDr = enteredDr;
        this.enteredCr = enteredCr;
        this.accountedDr = accountedDr;
        this.accountedCr = accountedCr;
        this.remark = remark;
        this.coaCd = coaCd;
        this.coaDesc = coaDesc;
    }


    public static GlDetailDto from(ErpGlLines glDetail) {
        return GlDetailDto.builder()
            .jeHeaderId(glDetail.getJeHeaderId())
            .status(glDetail.getStatus())
            .sourceNm(glDetail.getSourceName())
            .categoryNm(glDetail.getCategoryName())
            .jeBatchId(glDetail.getJeBatchId())
            .batchNm(glDetail.getBatchName())
            .headerNm(glDetail.getHeaderName())
            .currencyCd(glDetail.getCurrencyCode())
            .currencyRate(glDetail.getCurrencyRate())
            .glDt(glDetail.getGlDate())
            .ccid(glDetail.getCcid())
            .accountCd(glDetail.getAccountCode())
            .accountNm(glDetail.getAccountName())
            .enteredDr(glDetail.getEnteredDr())
            .enteredCr(glDetail.getEnteredCr())
            .accountedDr(glDetail.getAccountedDr())
            .accountedCr(glDetail.getAccountedCr())
            .remark(glDetail.getDescription())
            .coaCd(glDetail.getCoaCode())
            .coaDesc(glDetail.getCoaDesc())
            .build();
    }
}
