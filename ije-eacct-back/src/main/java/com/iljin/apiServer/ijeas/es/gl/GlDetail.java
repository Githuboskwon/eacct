package com.iljin.apiServer.ijeas.es.gl;

import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.es.SlipType;
import com.iljin.apiServer.ijeas.es.erpViews.gl.ErpGlLines;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@IdClass(GlDetailKey.class)
@Table(name = "TB_GL_DT")
@Entity
public class GlDetail extends BaseEntity {

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
    @Column(name = "SLIP_SEQ", nullable = false)
    Integer slipSeq;

    @Column(name = "JE_HEADER_ID")
    BigDecimal jeHeaderId;

    @Column(name = "STATUS")
    String status;

    @Column(name = "SOURCE_NM")
    String sourceNm;

    @Column(name = "CATEGORY_NM")
    String categoryNm;

    @Column(name = "JE_BATCH_ID")
    BigDecimal jeBatchId;


    @Column(name = "BATCH_NM")
    String batchNm;

    @Column(name = "HEADER_NM")
    String headerNm;

    @Column(name = "CURRENCY_CD")
    String currencyCd;

    @Column(name = "CURRENCY_RATE")
    BigDecimal currencyRate;

    @Column(name = "GL_DT")
    LocalDateTime glDt;

    @Column(name = "CCID")
    BigDecimal ccid;

    @Column(name = "ACCOUNT_CD")
    String accountCd;

    @Column(name = "ACCOUNT_NM")
    String accountNm;

    @Column(name = "ENTERED_DR")
    BigDecimal enteredDr;

    @Column(name = "ENTERED_CR")
    BigDecimal enteredCr;

    @Column(name = "ACCOUNTED_DR")
    BigDecimal accountedDr;

    @Column(name = "ACCOUNTED_CR")
    BigDecimal accountedCr;

    @Column(name = "REMARK")
    String remark;

    @Column(name = "COA_CD")
    String coaCd;

    @Column(name = "COA_DESC")
    String coaDesc;

    @Builder
    public GlDetail(String compCd, String slipNo, String slipType, Integer slipSeq,
        BigDecimal jeHeaderId, String status, String sourceNm, String categoryNm,
        BigDecimal jeBatchId, String batchNm, String headerNm, String currencyCd, BigDecimal currencyRate,
        LocalDateTime glDt, BigDecimal ccid, String accountCd, String accountNm, BigDecimal enteredDr,
        BigDecimal enteredCr, BigDecimal accountedDr, BigDecimal accountedCr, String remark, String coaCd,
        String coaDesc) {
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

    public static GlDetail from(ErpGlLines erpGlLines, String slipNo, String compCd, Integer seq) {
        return GlDetail.builder()
            .compCd(compCd)
            .slipNo(slipNo)
            .slipType(SlipType.GL.getCode())
            .slipSeq(seq)
            .jeHeaderId(erpGlLines.getJeHeaderId())
            .status(erpGlLines.getStatus())
            .sourceNm(erpGlLines.getSourceName())
            .categoryNm(erpGlLines.getCategoryName())
            .jeBatchId(erpGlLines.getJeBatchId())
            .headerNm(erpGlLines.getHeaderName())
            .currencyCd(erpGlLines.getCurrencyCode())
            .glDt(erpGlLines.getGlDate())
            .ccid(erpGlLines.getCcid())
            .accountCd(erpGlLines.getAccountCode())
            .accountNm(erpGlLines.getAccountName())
            .enteredDr(erpGlLines.getEnteredDr())
            .enteredCr(erpGlLines.getEnteredCr())
            .accountedDr(erpGlLines.getAccountedDr())
            .accountedCr(erpGlLines.getAccountedCr())
            .remark(erpGlLines.getDescription())
            .coaCd(erpGlLines.getCoaCode())
            .coaDesc(erpGlLines.getCoaDesc())
            .build();
    }
}
