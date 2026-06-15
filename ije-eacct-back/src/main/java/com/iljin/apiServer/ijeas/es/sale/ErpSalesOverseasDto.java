package com.iljin.apiServer.ijeas.es.sale;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
public class ErpSalesOverseasDto implements Serializable {

    private static final long serialVersionUID = 5281003613811924734L;

    String compCd;

    String slipNo;

    String slipType;

    BigDecimal slipHeaderId;

    String xtrSlipType;

    BigDecimal batchId;

    String transactionNum;

    LocalDateTime glDt;

    String integrationVendorNum;

    String integrationVendorNm;

    String erpRegId;

    String erpRegNm;

    String coaSegment4;

    String coaSegment4Name;

    String coaSegment5;

    String projectCd;

    String projectNm;

    String currencyCd;

    BigDecimal enteredAmt;

    BigDecimal accountedAmt;

    String periodNm;

    Integer ledgerId;

    String partitionKey;

    Long uFileCnt;

    Long jiniCnt;

    String docTitle;

    String remark;

    String deptCd;

    String deptNm;

    String taskCd;

    String taskNm;

    String incoterms;

    String certAmendYn;

    // list
    @QueryProjection
    public ErpSalesOverseasDto(String compCd, String slipNo, String slipType,
                               BigDecimal slipHeaderId,String xtrSlipType, BigDecimal batchId,
                               String transactionNum, LocalDateTime glDt,
                               String integrationVendorNum, String integrationVendorNm,
                               String erpRegId, String erpRegNm, String coaSegment5,
                               String projectCd, String projectNm, String currencyCd,
                               BigDecimal enteredAmt,  BigDecimal accountedAmt,
                               String periodNm, Integer ledgerId, String partitionKey,
                               Long uFileCnt, String docTitle){
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipHeaderId = slipHeaderId;
        this.xtrSlipType = xtrSlipType;
        this.batchId = batchId;
        this.transactionNum = transactionNum;
        this.glDt = glDt;
        this.integrationVendorNum = integrationVendorNum;
        this.integrationVendorNm = integrationVendorNm;
        this.erpRegId = erpRegId;
        this.erpRegNm = erpRegNm;
        this.coaSegment5 = coaSegment5;
        this.projectCd = projectCd;
        this.projectNm = projectNm;
        this.currencyCd = currencyCd;
        this.enteredAmt = enteredAmt;
        this.accountedAmt = accountedAmt;
        this.periodNm = periodNm;
        this.ledgerId = ledgerId;
        this.partitionKey = partitionKey;
        this.uFileCnt = uFileCnt;
        this.docTitle = docTitle;
    }

    // list
    @QueryProjection
    public ErpSalesOverseasDto(String compCd, String slipNo, String slipType,
                               BigDecimal slipHeaderId,String xtrSlipType, BigDecimal batchId,
                               String transactionNum, LocalDateTime glDt,
                               String integrationVendorNum, String integrationVendorNm,
                               String erpRegId, String erpRegNm, String coaSegment4Name, String coaSegment5,
                               String projectCd, String projectNm, String currencyCd,
                               BigDecimal enteredAmt,  BigDecimal accountedAmt,
                               String periodNm, Integer ledgerId, String partitionKey,
                               Long uFileCnt, String docTitle, String remark, String deptNm,
                               String taskCd, String taskNm, String incoterms, String certAmendYn){
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipHeaderId = slipHeaderId;
        this.xtrSlipType = xtrSlipType;
        this.batchId = batchId;
        this.transactionNum = transactionNum;
        this.glDt = glDt;
        this.integrationVendorNum = integrationVendorNum;
        this.integrationVendorNm = integrationVendorNm;
        this.erpRegId = erpRegId;
        this.erpRegNm = erpRegNm;
        this.coaSegment4Name = coaSegment4Name;
        this.coaSegment5 = coaSegment5;
        this.projectCd = projectCd;
        this.projectNm = projectNm;
        this.currencyCd = currencyCd;
        this.enteredAmt = enteredAmt;
        this.accountedAmt = accountedAmt;
        this.periodNm = periodNm;
        this.ledgerId = ledgerId;
        this.partitionKey = partitionKey;
        this.uFileCnt = uFileCnt;
        this.docTitle = docTitle;
        this.remark = remark;
        this.deptNm = deptNm;
        this.taskCd = taskCd;
        this.taskNm = taskNm;
        this.incoterms = incoterms;
        this.certAmendYn = certAmendYn;
    }

}
