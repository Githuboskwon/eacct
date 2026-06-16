package com.iljin.apiServer.ijeas.es.sale;

import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.es.SlipType;
import com.iljin.apiServer.ijeas.es.erpViews.sale.ErpArTrxHeaders;
import com.iljin.apiServer.ijeas.es.erpViews.frgn.ErpSpOsSlip;
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
@IdClass(ErpSalesOverseasKey.class)
@Table(name = "TB_SALES_OVERSEAS")
@Entity
public class ErpSalesOverseas extends BaseEntity {

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

    @Column(name = "XTR_SLIP_TYPE")
    String xtrSlipType;

    @Column(name = "BATCH_ID")
    BigDecimal batchId;

    @Column(name = "TRANSACTION_NUM")
    String transactionNum;

    @Column(name = "GL_DT")
    LocalDateTime glDt;

    @Column(name = "INTEGRATION_VENDOR_NUM")
    String integrationVendorNum;

    @Column(name = "INTEGRATION_VENDOR_NM")
    String integrationVendorNm;

    @Column(name = "ERP_REG_ID" )
    String erpRegId;

    @Column(name = "ERP_REG_NM" )
    String erpRegNm;

    @Column(name = "COA_SEGMENT5")
    String coaSegment5;

    @Column(name = "PROJECT_CD")
    String projectCd;

    @Column(name = "PROJECT_NM")
    String projectNm;

    @Column(name = "CURRENCY_CD")
    String currencyCd;

    @Column(name = "ENTERED_AMT")
    BigDecimal enteredAmt;

    @Column(name = "ACCOUNTED_AMT")
    BigDecimal accountedAmt;

    @Column(name = "PERIOD_NM")
    String periodNm;

    @Column(name = "LEDGER_ID")
    Integer ledgerId;

    @Column(name = "PARTITION_KEY")
    String partitionKey;

    @Column(name = "REMARK")
    String remark;

    @Column(name = "COA_SEGMENT4")
    String coaSegment4;

    @Column(name = "COA_SEGMENT4_NAME")
    String coaSegment4Name;

    @Column(name = "TAX_CODE")
    String taxCode;

    @Column(name = "INCOTERMS")
    String incoterms;

    @Column(name = "SLIP_DEPT_CODE")
    String slipDeptCode;

    @Column(name = "SLIP_DEPT_NAME")
    String slipDeptName;

    @Column(name = "TASK_CODE")
    String taskCode;

    @Column(name = "TASK_NAME")
    String taskName;

    @Column(name = "CERT_AMEND_YN")
    String certAmendYn;


    @Builder
    public ErpSalesOverseas(String compCd, String slipNo, String slipType,
                                BigDecimal slipHeaderId,String xtrSlipType, BigDecimal batchId,
                                String transactionNum, LocalDateTime glDt,
                                String integrationVendorNum, String integrationVendorNm,
                                String erpRegId, String erpRegNm, String coaSegment5,
                                String projectCd, String projectNm, String currencyCd,
                                BigDecimal enteredAmt,  BigDecimal accountedAmt,
                                String periodNm, Integer ledgerId, String partitionKey,
                                String remark, String coaSegment4, String coaSegment4Name,
                                String taxCode, String incoterms, String slipDeptCode, String slipDeptName,
                                String taskCode, String taskName, String certAmendYn){
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
        this.remark = remark;
        this.coaSegment4 = coaSegment4;
        this.coaSegment4Name = coaSegment4Name;
        this.taxCode = taxCode;
        this.incoterms = incoterms;
        this.slipDeptCode = slipDeptCode;
        this.slipDeptName = slipDeptName;
        this.taskCode = taskCode;
        this.taskName = taskName;
        this.certAmendYn = certAmendYn;
    }

    public static ErpSalesOverseas from(ErpArTrxHeaders erpArTrxHeaders, String slipNo, BigDecimal slipHeaderId, String compCd) {
        return ErpSalesOverseas.builder()
                .compCd(compCd)
                .slipNo(slipNo)
                .slipType(SlipType.SALE.getCode())
                .slipHeaderId(slipHeaderId)
                .xtrSlipType(erpArTrxHeaders.getSlipType())
                .batchId(erpArTrxHeaders.getSlipId())
                .transactionNum(erpArTrxHeaders.getSlipNumber())
                .glDt(erpArTrxHeaders.getGlDate())
                .integrationVendorNum(erpArTrxHeaders.getIntegrationVendorNum())
                .integrationVendorNm(erpArTrxHeaders.getIntegrationVendorName())
                .erpRegId(erpArTrxHeaders.getSlipCreatedBy())
                .erpRegNm(erpArTrxHeaders.getSlipCreatedName())
                .coaSegment5(erpArTrxHeaders.getCoaSegment5())
                .projectCd(erpArTrxHeaders.getProjectCode())
                .projectNm(erpArTrxHeaders.getProjectName())
                .currencyCd(erpArTrxHeaders.getCurrencyCode())
                .enteredAmt(erpArTrxHeaders.getEnteredAmt())
                .accountedAmt(erpArTrxHeaders.getAccountedAmt())
                .periodNm(erpArTrxHeaders.getPeriodName())
                .ledgerId(erpArTrxHeaders.getLedgerId())
                .partitionKey(erpArTrxHeaders.getPartitionKey())
                .build();
    }

    public static ErpSalesOverseas from(ErpSpOsSlip erpSpOsSlip, String slipNo, BigDecimal slipHeaderId, String compCd) {
        return ErpSalesOverseas.builder()
                .compCd(compCd)
                .slipNo(slipNo)
                .slipType(SlipType.FRGN.getCode())
                .slipHeaderId(slipHeaderId)
                .xtrSlipType(erpSpOsSlip.getSlipType())
                .batchId(erpSpOsSlip.getSlipId())
                .transactionNum(erpSpOsSlip.getSlipNumber())
                .glDt(erpSpOsSlip.getGlDate())
                .integrationVendorNum(erpSpOsSlip.getIntegrationVendorNum())
                .integrationVendorNm(erpSpOsSlip.getIntegrationVendorName())
                .erpRegId(erpSpOsSlip.getSlipCreatedBy())
                .erpRegNm(erpSpOsSlip.getSlipCreatedName())
                .coaSegment5(erpSpOsSlip.getCoaSegment5())
                .projectCd(erpSpOsSlip.getProjectCode())
                .projectNm(erpSpOsSlip.getProjectName())
                .currencyCd(erpSpOsSlip.getCurrencyCode())
                .enteredAmt(erpSpOsSlip.getEnteredAmt())
                .accountedAmt(erpSpOsSlip.getAccountedAmt())
                .periodNm(erpSpOsSlip.getPeriodName())
                .ledgerId(erpSpOsSlip.getLedgerId())
                .partitionKey(erpSpOsSlip.getPartitionKey())
                .build();
    }

    // 수출 전표 추가
    public static ErpSalesOverseas from(ErpArTrxHeaders erpArTrxHeaders, String slipNo, BigDecimal slipHeaderId, String compCd, String remark) {
        return ErpSalesOverseas.builder()
                .compCd(compCd)
                .slipNo(slipNo)
                .slipType(SlipType.EXPT.getCode())
                .slipHeaderId(slipHeaderId)
                .xtrSlipType(erpArTrxHeaders.getSlipType())
                .batchId(erpArTrxHeaders.getSlipId())
                .transactionNum(erpArTrxHeaders.getSlipNumber())
                .glDt(erpArTrxHeaders.getGlDate())
                .integrationVendorNum(erpArTrxHeaders.getIntegrationVendorNum())
                .integrationVendorNm(erpArTrxHeaders.getIntegrationVendorName())
                .erpRegId(erpArTrxHeaders.getSlipCreatedBy())
                .erpRegNm(erpArTrxHeaders.getSlipCreatedName())
                .coaSegment5(erpArTrxHeaders.getCoaSegment5())
                .projectCd(erpArTrxHeaders.getProjectCode())
                .projectNm(erpArTrxHeaders.getProjectName())
                .currencyCd(erpArTrxHeaders.getCurrencyCode())
                .enteredAmt(erpArTrxHeaders.getEnteredAmt())
                .accountedAmt(erpArTrxHeaders.getAccountedAmt())
                .periodNm(erpArTrxHeaders.getPeriodName())
                .ledgerId(erpArTrxHeaders.getLedgerId())
                .partitionKey(erpArTrxHeaders.getPartitionKey())
                .coaSegment4(erpArTrxHeaders.getCoaSegment4())
                .coaSegment4Name(erpArTrxHeaders.getCoaSegment4Name())
                .taxCode(erpArTrxHeaders.getTaxCode())
                .incoterms(erpArTrxHeaders.getIncoterms())
                .slipDeptCode(erpArTrxHeaders.getSlipDeptCode())
                .slipDeptName(erpArTrxHeaders.getSlipDeptName())
                .taskCode(erpArTrxHeaders.getTaskCode())
                .taskName(erpArTrxHeaders.getTaskName())
                .build();
    }
}
