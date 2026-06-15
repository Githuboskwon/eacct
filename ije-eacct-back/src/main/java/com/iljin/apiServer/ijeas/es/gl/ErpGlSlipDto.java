package com.iljin.apiServer.ijeas.es.gl;

import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ErpGlSlipDto implements Serializable {

    private static final long serialVersionUID = -6017295099589709184L;

    String compCd;

    String slipNo;

    String status;

    String slipType;

    BigDecimal slipHeaderId;

    BigDecimal jeHeaderId;

    String headerNm;

    BigDecimal ledgerId;

    String sourceNm;

    String categoryNm;

    String glSlipType;

    String trxTypeCd;

    String trxTypeNm;

    BigDecimal jeBatchId;

    String batchNm;

    String batchStatus;

    String reverseFlag;

    String reverseHeaderNm;

    String headerRemark;

    String headerStatus;

    LocalDateTime postedDt;

    String postingDt;

    String currencyCd;

    BigDecimal currencyRate;

    String currencyRateType;

    LocalDateTime currencyDt;

    LocalDateTime glDt;

    BigDecimal forTotalDr;

    BigDecimal forTotalCr;

    BigDecimal krwTotalDr;

    BigDecimal krwTotalCr;

    String regId;
    String regNm;

    String deptCd;
    String deptNm;

    LocalDateTime regDtm;

    String search;


    Long uFileCnt;

    Long jiniCnt;
    String docTitle;

    List<GlDetailDto> detailList;


    @QueryProjection
    public ErpGlSlipDto(String compCd, String slipNo, String status, String slipType, BigDecimal slipHeaderId,
        BigDecimal jeHeaderId, String headerNm, BigDecimal ledgerId, String sourceNm,
        String categoryNm,
        String glSlipType, String trxTypeCd, String trxTypeNm, BigDecimal jeBatchId, String batchNm,
        String batchStatus, String reverseFlag, String reverseHeaderNm, String headerRemark,
        String headerStatus, LocalDateTime postedDt, String postingDt, String currencyCd, BigDecimal currencyRate,
        String currencyRateType, LocalDateTime currencyDt, LocalDateTime glDt,
        BigDecimal forTotalDr,
        BigDecimal forTotalCr, BigDecimal krwTotalDr, BigDecimal krwTotalCr, String regId,
        String regNm, String deptCd, String deptNm,
        LocalDateTime regDtm, Long uFileCnt, String docTitle) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.status = status;
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
        this.postingDt = postingDt;
        this.currencyCd = currencyCd;
        this.currencyRate = currencyRate;
        this.currencyRateType = currencyRateType;
        this.currencyDt = currencyDt;
        this.glDt = glDt;
        this.forTotalDr = forTotalDr;
        this.forTotalCr = forTotalCr;
        this.krwTotalDr = krwTotalDr;
        this.krwTotalCr = krwTotalCr;
        this.regId = regId;
        this.regNm = regNm;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.regDtm = regDtm;
        this.uFileCnt = uFileCnt;
        this.docTitle = docTitle;
    }


    public void changeDetailList(List<GlDetailDto> dtoList) {
        this.detailList = dtoList;
    }
}
