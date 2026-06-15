package com.iljin.apiServer.ijeas.es.bulk;

import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.es.SlipType;
import com.iljin.apiServer.ijeas.es.erpViews.bond.ErpApFuturesBatch;
import com.iljin.apiServer.ijeas.es.erpViews.bulk.ErpApPaymentsBatch;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@IdClass(ApPaymentsHdKey.class)
@Table(name = "TB_AP_PAYMENTS_HD")
@Entity
public class ApPaymentsHd extends BaseEntity {

    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    @Id
    @Column(name = "SLIP_TYPE", nullable = false)
    String slipType;

    @Id
    @Column(name = "ESLIP_TRANSFER_BATCH_ID")
    String eslipTransferBatchId;

    @Column(name = "ESLIP_TRANSFER")
    String eslipTransfer;

    @Id
    @Column(name = "SLIP_NO", nullable = false)
    String slipNo;

    @Column(name = "SLIP_HEADER_ID")
    Integer slipHeaderId;

    @Column(name = "TRANSFER_TYPE")
    String transferType;

    @Column(name = "TRANSFER_DT")
    String transferDt;

    @Column(name = "TRANSFERRED_BY")
    String transferredBy;

    @Column(name = "BANK_NM")
    String bankNm;

    @Column(name = "BANK_BRANCH_NM")
    String bankBranchNm;

    @Column(name = "BANK_ACCT_NM")
    String bankAcctNm;

    @Column(name = "BANK_ACCT_NUM")
    String bankAcctNum;

    @Column(name = "BANK_ACCT_ID")
    Integer bankAcctId;

    @Column(name = "SEGMENT3")
    String segment3;

    @Column(name = "SEGMENT4")
    String segment4;

    @Column(name = "SEGMENT3_DESC")
    String segment3Desc;

    @Column(name = "SEGMENT4_DESC")
    String segment4Desc;

    @Column(name = "FUTURE_PAY_DUE_DT")
    LocalDateTime futurePayDueDt;

    @Column(name = "SUM_AMT")
    BigDecimal sumAmt;

    @Column(name = "CHECK_DT")
    LocalDateTime checkDt;

    @Column(name = "PAYMENT_METHOD_LOOKUP_CODE")
    String paymentMethodLookupCode;

    @Column(name = "REMARK")
    String remark;

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
    public ApPaymentsHd(String compCd,String slipType,String eslipTransferBatchId,String eslipTransfer,String slipNo,
                        Integer slipHeaderId,String transferType,String transferDt,String transferredBy,String bankNm,
                        String bankBranchNm,String bankAcctNm,String bankAcctNum,Integer bankAcctId,String segment3,String segment4,
                        String segment3Desc,String segment4Desc,LocalDateTime futurePayDueDt,BigDecimal sumAmt,LocalDateTime checkDt,
                        String paymentMethodLookupCode,String remark,String attribute1,String attribute2,String attribute3,String attribute4,String attribute5) {
        this.compCd = compCd;
        this.slipType = slipType;
        this.eslipTransferBatchId = eslipTransferBatchId;
        this.eslipTransfer = eslipTransfer;
        this.slipNo = slipNo;
        this.slipHeaderId = slipHeaderId;
        this.transferType = transferType;
        this.transferDt = transferDt;
        this.transferredBy = transferredBy;
        this.bankNm = bankNm;
        this.bankBranchNm = bankBranchNm;
        this.bankAcctNm = bankAcctNm;
        this.bankAcctNum = bankAcctNum;
        this.bankAcctId = bankAcctId;
        this.segment3 = segment3;
        this.segment4 = segment4;
        this.segment3Desc = segment3Desc;
        this.segment4Desc = segment4Desc;
        this.futurePayDueDt = futurePayDueDt;
        this.sumAmt = sumAmt;
        this.checkDt = checkDt;
        this.paymentMethodLookupCode = paymentMethodLookupCode;
        this.remark = remark;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
    }

    public static ApPaymentsHd from(ErpApPaymentsBatch erpBatch, String slipNo, Integer slipHeaderId) {
        return ApPaymentsHd.builder()
            .slipNo(slipNo)
            .slipType(SlipType.BULK.getCode())
            .slipHeaderId(slipHeaderId)
            .compCd(String.valueOf(erpBatch.getOrgId()))
            .eslipTransferBatchId(String.valueOf(erpBatch.getEslipTransferBatchId()))
            .eslipTransfer(erpBatch.getEslipTransfer())
            .transferType(erpBatch.getTransferType())
            .transferDt(erpBatch.getTransferDate())
            .transferredBy(erpBatch.getTransferredBy())
            .bankNm(erpBatch.getBankName())
            .bankBranchNm(erpBatch.getBankBranchName())
            .bankAcctNm(erpBatch.getBankAccountName())
            .bankAcctNum(erpBatch.getBankAccountNum())
            .bankAcctId(erpBatch.getBankAccountId())
            .checkDt(erpBatch.getCheckDate())
            .paymentMethodLookupCode(erpBatch.getPaymentMethodLookupCode())
            .remark(erpBatch.getRemark())
            .build();
    }

    public static ApPaymentsHd from(ErpApFuturesBatch erpBatch, String slipNo, Integer slipHeaderId) {
        return ApPaymentsHd.builder()
                .slipNo(slipNo)
                .slipType(SlipType.BOND.getCode())
                .slipHeaderId(slipHeaderId)
                .compCd(String.valueOf(erpBatch.getOrgId()))
                .eslipTransferBatchId(String.valueOf(erpBatch.getEslipTransferBatchId()))
                .eslipTransfer(erpBatch.getEslipTransfer())
                .transferType(erpBatch.getTransferType())
                .transferDt(erpBatch.getTransferDate())
                .transferredBy(erpBatch.getTransferredBy())
                .bankNm(erpBatch.getBankName())
                .bankBranchNm(erpBatch.getBankBranchName())
                .bankAcctNm(erpBatch.getBankAccountName())
                .bankAcctNum(erpBatch.getBankAccountNum())
                .segment3(erpBatch.getSegment3())
                .segment3Desc(erpBatch.getSegment3Desc())
                .segment4(erpBatch.getSegment4())
                .segment4Desc(erpBatch.getSegment4Desc())
                .futurePayDueDt(erpBatch.getFuturePayDueDate())
                .sumAmt(erpBatch.getSumAmount())
                .remark(erpBatch.getRemark())
                .paymentMethodLookupCode(erpBatch.getPaymentMethodCode())
                .build();
    }

}
