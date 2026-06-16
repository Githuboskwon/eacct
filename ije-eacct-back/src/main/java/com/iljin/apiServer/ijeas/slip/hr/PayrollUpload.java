package com.iljin.apiServer.ijeas.slip.hr;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table(name ="CBO_SP_PAYROLL_UPLOAD")
@IdClass(PayrollUploadKey.class)
@Entity
public class PayrollUpload {
    @Id
    @Column(name = "ORG_ID")
    private Integer orgId;

    @Id
    @Column(name = "LEDGER_ID")
    private Integer ledgerId;

    @Id
    @Column(name = "PAYROLL_BATCH_ID")
    private Integer payrollBatchId;

    @Id
    @Column(name = "PAYROLL_LINE_ID")
    private Integer payrollLineId;

    @Column(name = "SLIP_DATE")
    private LocalDateTime slipDate;

    @Column(name = "PAYROLL_TYPE_CODE")
    private String payrollTypeCode;

    @Column(name = "PAYROLL_TYPE_NAME")
    private String payrollTypeName;

    @Column(name = "UPLOAD_TITLE")
    private String uploadTitle;

    @Column(name = "RETIRE_EMP_NO")
    private String retireEmpNo;

    @Column(name = "UPLOAD_EMP_NO")
    private String uploadEmpNo;

    @Column(name = "COA_SEGMENT3")
    private String coaSegment3;

    @Column(name = "COA_SEGMENT4")
    private String coaSegment4;

    @Column(name = "COA_SEGMENT5")
    private String coaSegment5;

    @Column(name = "PAYROLL_AMOUNT")
    private BigDecimal payrollAmount;

    @Column(name = "LINE_DESCRIPTION")
    private String lineDescription;

    @Column(name = "SLIP_IF_FLAG")
    private String slipIfFlag;

    @Column(name = "SLIP_HEADER_ID")
    private Integer slipHeaderId;

    @Column(name = "SLIP_IF_DATE")
    private LocalDateTime slipIfDate;

    @Column(name = "VALIDATION_FLAG")
    private String validationFlag;

    @Column(name = "ERROR_MSG")
    private String errorMsg;

    @Column(name = "PAYROLL_TYPE_ID")
    private Integer payrollTypeId;

    @Column(name = "PAYROLL_TYPE")
    private String payrollType;

    @Column(name = "PAYROLL_ITEM")
    private String payrollItem;

    @Column(name = "DEDUCTION_TYPE")
    private String deductionType;

    @Column(name = "INT_VENDOR_NUM")
    private String intVendorNum;

    @Column(name = "PAYMENT_TERMS_ID")
    private Integer payTermId;

    @Column(name = "ACCOUNT_CR")
    private String accountCr;

    @Column(name = "ATTRIBUTE1")
    private String attribute1;

    @Column(name = "ATTRIBUTE2")
    private String attribute2;

    @Column(name = "ATTRIBUTE3")
    private String attribute3;

    @Column(name = "ATTRIBUTE4")
    private String attribute4;

    @Column(name = "ATTRIBUTE5")
    private String attribute5;

    @Column(name = "ATTRIBUTE6")
    private String attribute6;

    @Column(name = "ATTRIBUTE7")
    private String attribute7;

    @Column(name = "ATTRIBUTE8")
    private String attribute8;

    @Column(name = "ATTRIBUTE9")
    private String attribute9;

    @Column(name = "ATTRIBUTE10")
    private String attribute10;

    @Column(name = "CREATED_EMP_NO")
    private String createdEmpNo;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "LAST_UPDATED_EMP_NO")
    private String lastUpdatedEmpNo;

    @Column(name = "LAST_UPDATED_DATE")
    private LocalDateTime lastUpdatedDate;

    @Column(name = "TERM_NAME")
    private String termName;

    @Column(name = "BANK_ACCOUNT_NAME")
    private String bankAccountName;

    @Column(name = "DUE_DATE")
    private LocalDateTime dueDate;

    @Builder
    public PayrollUpload(Integer orgId, Integer ledgerId, Integer  payrollBatchId, Integer payrollLineId, LocalDateTime slipDate, String payrollTypeCode,
                         String payrollTypeName, String uploadTitle, String retireEmpNo, String uploadEmpNo, String coaSegment3, String coaSegment4, String coaSegment5,
                         BigDecimal payrollAmount, String lineDescription, String slipIfFlag, Integer slipHeaderId, LocalDateTime slipIfDate, String validationFlag,
                         String errorMsg, Integer payrollTypeId, String payrollType, String payrollItem, String deductionType, String intVendorNum, Integer payTermId,
                         String accountCr, String attribute1, String attribute2, String attribute3, String attribute4, String attribute5, String attribute6,
                         String attribute7, String attribute8, String attribute9, String attribute10, String createdEmpNo, LocalDateTime creationDate,
                         String lastUpdatedEmpNo, LocalDateTime lastUpdatedDate, String termName, String bankAccountName, LocalDateTime dueDate) {
        this.orgId = orgId;
        this.ledgerId = ledgerId;
        this.payrollBatchId = payrollBatchId;
        this.payrollLineId = payrollLineId;
        this.slipDate = slipDate;
        this.payrollTypeCode = payrollTypeCode;
        this.payrollTypeName = payrollTypeName;
        this.uploadTitle = uploadTitle;
        this.retireEmpNo = retireEmpNo;
        this.uploadEmpNo = uploadEmpNo;
        this.coaSegment3 = coaSegment3;
        this.coaSegment4 = coaSegment4;
        this.coaSegment5 = coaSegment5;
        this.payrollAmount = payrollAmount;
        this.lineDescription = lineDescription;
        this.slipIfFlag = slipIfFlag;
        this.slipHeaderId = slipHeaderId;
        this.slipIfDate = slipIfDate;
        this.validationFlag = validationFlag;
        this.errorMsg = errorMsg;
        this.payrollTypeId = payrollTypeId;
        this.payrollType = payrollType;
        this.payrollItem = payrollItem;
        this.deductionType = deductionType;
        this.intVendorNum = intVendorNum;
        this.payTermId = payTermId;
        this.accountCr = accountCr;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
        this.attribute6 = attribute6;
        this.attribute7 = attribute7;
        this.attribute8 = attribute8;
        this.attribute9 = attribute9;
        this.attribute10 = attribute10;
        this.createdEmpNo = createdEmpNo;
        this.creationDate = creationDate;
        this.lastUpdatedEmpNo = lastUpdatedEmpNo;
        this.lastUpdatedDate = lastUpdatedDate;
        this.termName = termName;
        this.bankAccountName = bankAccountName;
        this.dueDate = dueDate;
    }

    @Builder
    public PayrollUpload(Integer orgId, Integer ledgerId, Integer  payrollBatchId, Integer payrollLineId, LocalDateTime slipDate, String payrollTypeCode,
                         String payrollTypeName, String uploadTitle, String retireEmpNo, String uploadEmpNo, String coaSegment3, String coaSegment4, String coaSegment5,
                         BigDecimal payrollAmount, String lineDescription, String slipIfFlag, Integer slipHeaderId, LocalDateTime slipIfDate, String validationFlag,
                         String errorMsg, Integer payrollTypeId, String payrollType, String payrollItem, String deductionType, String intVendorNum, Integer payTermId,
                         String accountCr, String createdEmpNo, LocalDateTime creationDate, String lastUpdatedEmpNo, LocalDateTime lastUpdatedDate, String termName,
                         String bankAccountName, LocalDateTime dueDate) {
        this.orgId = orgId;
        this.ledgerId = ledgerId;
        this.payrollBatchId = payrollBatchId;
        this.payrollLineId = payrollLineId;
        this.slipDate = slipDate;
        this.payrollTypeCode = payrollTypeCode;
        this.payrollTypeName = payrollTypeName;
        this.uploadTitle = uploadTitle;
        this.retireEmpNo = retireEmpNo;
        this.uploadEmpNo = uploadEmpNo;
        this.coaSegment3 = coaSegment3;
        this.coaSegment4 = coaSegment4;
        this.coaSegment5 = coaSegment5;
        this.payrollAmount = payrollAmount;
        this.lineDescription = lineDescription;
        this.slipIfFlag = slipIfFlag;
        this.slipHeaderId = slipHeaderId;
        this.slipIfDate = slipIfDate;
        this.validationFlag = validationFlag;
        this.errorMsg = errorMsg;
        this.payrollTypeId = payrollTypeId;
        this.payrollType = payrollType;
        this.payrollItem = payrollItem;
        this.deductionType = deductionType;
        this.intVendorNum = intVendorNum;
        this.payTermId = payTermId;
        this.accountCr = accountCr;
        this.createdEmpNo = createdEmpNo;
        this.creationDate = creationDate;
        this.lastUpdatedEmpNo = lastUpdatedEmpNo;
        this.lastUpdatedDate = lastUpdatedDate;
        this.termName = termName;
        this.bankAccountName = bankAccountName;
        this.dueDate = dueDate;
    }
}
