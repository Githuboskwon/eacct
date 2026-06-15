package com.iljin.apiServer.ijeas.es.fund;

import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.es.SlipType;
import com.iljin.apiServer.ijeas.es.erpViews.fund.ErpTrTrxHeadersDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(SpTrFundHdKey.class)
@Table(name = "TB_TR_FUND_HD")
@Entity
public class SpTrFundHd extends BaseEntity {

    @Id
    @Column(name = "COMP_CD" , nullable = false)
    String compCd;

    @Id
    @Column(name = "SLIP_NO" , nullable = false)
    String slipNo;

    @Id
    @Column(name = "SLIP_TYPE" , nullable = false)
    String slipType;

    @Column(name = "SLIP_HEADER_ID")
    BigDecimal slipHeaderId;

    @Column(name = "TRX_UNIQUE_ID")
    BigDecimal trxUniqueId;

    @Column(name = "TRX_UNIQUE_NUM")
    String trxUniqueNum;

    @Column(name = "XTR_SLIP_TYPE")
    String xtrSlipType;

    @Column(name = "BATCH_ID")
    Integer batchId;

    @Column(name = "DEAL_NUM")
    String dealNum;

    @Column(name = "TRANSACTION_NUM")
    String transactionNum;

    @Column(name = "DEAL_TYPE")
    String dealType;

    @Column(name = "PRODUCT_TYPE")
    String productType;

    @Column(name = "ORIGIN_TRX_NUM")
    String originTrxNum;

    @Column(name = "DEAL_SUBTYPE")
    String dealSubtype;

    @Column(name = "JOURNAL_DT")
    LocalDateTime journalDt;

    @Column(name = "TRANSACTION_RATE")
    BigDecimal transactionRate;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "CURRENCY_CD_HEADER")
    String currencyCdHeader;

    @Column(name = "BUY_AMT")
    BigDecimal buyAmt;

    @Column(name = "SELL_AMT")
    BigDecimal sellAmt;

    @Column(name = "PROFIT_AMT")
    BigDecimal profitAmt;

    @Column(name = "SLIP_AMT")
    BigDecimal slipAmt;

    @Column(name = "AMOUNT_TYPE")
    String amountType;

    @Column(name = "TRANSACTION_TYPE")
    String transactionType;

    @Column(name = "DAILY_RATE")
    BigDecimal dailyRate;

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

    @Column(name = "ERP_REG_ID")
    String erpRegId;

    @Builder
    public SpTrFundHd(String compCd,String slipNo,String slipType,BigDecimal slipHeaderId,
                      String xtrSlipType,Integer batchId,String dealNum,String transactionNum,
                      String dealType,String productType,String originTrxNum,String dealSubtype,
                      LocalDateTime journalDt,BigDecimal transactionRate,String description,
                      String currencyCdHeader,BigDecimal buyAmt,BigDecimal sellAmt,
                      BigDecimal profitAmt,BigDecimal slipAmt,String amountType,
                      String transactionType,BigDecimal dailyRate,String attribute1,
                      String attribute2,String attribute3,String attribute4,String attribute5 ,String erpRegId){
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipHeaderId = slipHeaderId;
        this.xtrSlipType = xtrSlipType;
        this.batchId = batchId;
        this.dealNum = dealNum;
        this.transactionNum = transactionNum;
        this.dealType = dealType;
        this.productType = productType;
        this.originTrxNum = originTrxNum;
        this.dealSubtype = dealSubtype;
        this.journalDt = journalDt;
        this.transactionRate = transactionRate;
        this.description = description;
        this.currencyCdHeader = currencyCdHeader;
        this.buyAmt = buyAmt;
        this.sellAmt = sellAmt;
        this.profitAmt = profitAmt;
        this.slipAmt = slipAmt;
        this.amountType = amountType;
        this.transactionType = transactionType;
        this.dailyRate = dailyRate;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
        this.erpRegId = erpRegId;
    }

    public static SpTrFundHd from(ErpTrTrxHeadersDto erpTrTrxHeadersDto, String slipNo, BigDecimal slipHeaderId){
        return SpTrFundHd.builder()
                .compCd(String.valueOf(erpTrTrxHeadersDto.getOrgId()))
                .slipNo(slipNo)
                .slipType(SlipType.FUND.getCode())
                .slipHeaderId(slipHeaderId)
                .xtrSlipType(erpTrTrxHeadersDto.getXtrSlipType())
                .batchId(erpTrTrxHeadersDto.getBatchId())
                .dealNum(erpTrTrxHeadersDto.getDealNumber())
                .transactionNum(erpTrTrxHeadersDto.getTransactionNumber())
                .dealType(erpTrTrxHeadersDto.getDealType())
                .productType(erpTrTrxHeadersDto.getProductType())
                .originTrxNum(erpTrTrxHeadersDto.getOriginTrxNumber())
                .dealSubtype(erpTrTrxHeadersDto.getDealSubtype())
                .journalDt(erpTrTrxHeadersDto.getJournalDate())
                .transactionRate(erpTrTrxHeadersDto.getTransactionRate())
                .description(erpTrTrxHeadersDto.getDescription())
                .currencyCdHeader(erpTrTrxHeadersDto.getCurrencyCodeHeader())
                .buyAmt(erpTrTrxHeadersDto.getBuyAmount())
                .sellAmt(erpTrTrxHeadersDto.getSellAmount())
                .profitAmt(erpTrTrxHeadersDto.getProfitAmount())
                .slipAmt(erpTrTrxHeadersDto.getSlipAmount())
                .amountType(erpTrTrxHeadersDto.getAmountType())
                .transactionType(erpTrTrxHeadersDto.getTransactionType())
                .dailyRate(erpTrTrxHeadersDto.getDailyRate())
                .attribute1(erpTrTrxHeadersDto.getAttribute1())
                .attribute2(erpTrTrxHeadersDto.getAttribute2())
                .attribute3(erpTrTrxHeadersDto.getAttribute3())
                .attribute4(erpTrTrxHeadersDto.getAttribute4())
                .attribute5(erpTrTrxHeadersDto.getAttribute5())
                .erpRegId(erpTrTrxHeadersDto.getCreatedBy())
                .build();
    }

}
