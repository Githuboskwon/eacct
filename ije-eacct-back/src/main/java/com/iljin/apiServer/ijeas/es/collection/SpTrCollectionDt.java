package com.iljin.apiServer.ijeas.es.collection;

import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.es.SlipType;
import com.iljin.apiServer.ijeas.es.erpViews.collection.ErpTrFundTrn;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(SpTrCollectionDtKey.class)
@Table(name = "TB_TR_COLLECTION_DT")
@Entity
public class SpTrCollectionDt extends BaseEntity {

    @Id
    @Column(name = "COMP_CD" , nullable = false)
    String compCd;

    @Column(name = "SLIP_NO" , nullable = false)
    String slipNo;

    @Column(name = "SLIP_TYPE" , nullable = false)
    String slipType;

    @Column(name = "SLIP_HEADER_ID")
    BigDecimal slipHeaderId;

    @Id
    @Column(name = "SLIP_SEQ" , nullable = false)
    BigDecimal slipSeq;

    @Column(name = "XTR_SLIP_TYPE")
    String xtrSlipType;

    @Column(name = "TRANSACTION_NUM")
    BigDecimal transactionNum;

    @Column(name = "TRANSACTION_TYPE")
    String transactionType;

    @Column(name = "ORIGIN_TRX_NUM")
    String originTrxNum;

    @Column(name = "DEAL_TYPE")
    String dealType;

    @Column(name = "GL_DT")
    LocalDateTime glDt;

    @Id
    @Column(name = "SEGMENT2_3")
    String segment2_3;

    @Column(name = "REMARK")
    String remark;

    @Column(name = "AMT_DR")
    BigDecimal amtDr;

    @Column(name = "AMT_CR")
    BigDecimal amtCr;

    @Id
    @Column(name = "FUNCTIONAL_AMT_DR")
    BigDecimal functionalAmtDr;

    @Id
    @Column(name = "FUNCTIONAL_AMT_CR")
    BigDecimal functionalAmtCr;

    @Column(name = "BANK_SHORT_CD")
    String bankShortCd;

    @Id
    @Column(name = "ACCOUNT_NUM")
    String accountNum;

    @Column(name = "CURRENCY_CD")
    String currencyCd;

    @Id
    @Column(name = "REMIT_ID")
    BigDecimal remitId;

    @Column(name = "REF_TRANSACTION_NUM")
    BigDecimal refTransactionNum;

    @Column(name = "ERP_REG_ID")
    String erpRegId;


    @Builder
    public SpTrCollectionDt(String compCd,String slipNo,String slipType,BigDecimal slipHeaderId,
                            BigDecimal slipSeq,String xtrSlipType,BigDecimal transactionNum,String transactionType,
                            String originTrxNum,String dealType,LocalDateTime glDt,String segment2_3,
                            String remark,BigDecimal amtDr,BigDecimal amtCr,BigDecimal functionalAmtDr,
                            BigDecimal functionalAmtCr,String bankShortCd,String accountNum,String currencyCd,
                            BigDecimal remitId,BigDecimal refTransactionNum,String erpRegId){
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipHeaderId = slipHeaderId;
        this.slipSeq = slipSeq;
        this.xtrSlipType = xtrSlipType;
        this.transactionNum = transactionNum;
        this.transactionType = transactionType;
        this.originTrxNum = originTrxNum;
        this.dealType = dealType;
        this.glDt = glDt;
        this.segment2_3 = segment2_3;
        this.remark = remark;
        this.amtDr = amtDr;
        this.amtCr = amtCr;
        this.functionalAmtDr = functionalAmtDr;
        this.functionalAmtCr = functionalAmtCr;
        this.bankShortCd = bankShortCd;
        this.accountNum = accountNum;
        this.currencyCd = currencyCd;
        this.remitId = remitId;
        this.refTransactionNum = refTransactionNum;
        this.erpRegId = erpRegId;
    }

    public static SpTrCollectionDt from(ErpTrFundTrn erpTrFundTrn, String slipNo, BigDecimal slipHeaderId , BigDecimal slipSeq){
        return SpTrCollectionDt.builder()
                .compCd(String.valueOf(erpTrFundTrn.getOrgId()))
                .slipNo(slipNo)
                .slipType(SlipType.CLCT.getCode())
                .slipSeq(slipSeq)
                .slipHeaderId(slipHeaderId)
                .xtrSlipType(erpTrFundTrn.getXtrSlipType())
                .transactionNum(erpTrFundTrn.getTransactionNumber())
                .transactionType(erpTrFundTrn.getTransactionType())
                .originTrxNum(erpTrFundTrn.getOriginTrxNumber())
                .dealType(erpTrFundTrn.getDealType())
                .glDt(erpTrFundTrn.getGlDate())
                .segment2_3(erpTrFundTrn.getSegment2_3())
                .remark(erpTrFundTrn.getRemark())
                .amtDr(erpTrFundTrn.getAmountDr())
                .amtCr(erpTrFundTrn.getAmountCr())
                .functionalAmtDr(erpTrFundTrn.getFunctionalAmountDr())
                .functionalAmtCr(erpTrFundTrn.getFunctionalAmountCr())
                .bankShortCd(erpTrFundTrn.getBankShortCode())
                .accountNum(erpTrFundTrn.getAccountNumber())
                .currencyCd(erpTrFundTrn.getCurrencyCode())
                .remitId(erpTrFundTrn.getRemitId())
                .refTransactionNum(erpTrFundTrn.getRefTransactionNumber())
                .erpRegId(erpTrFundTrn.getCreatedBy())
                .build();
    }

}
