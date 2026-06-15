package com.iljin.apiServer.ijeas.slip.history;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class SlipHistoryDto implements Serializable {
    private static final long serialVersionUID = 4471632061095352947L;

    String compCd;
    String slipNo;
    BigDecimal slipHeaderId;
    String slipGroupNo;
    String deptNo;
    String deptNm;
    String slipType;
    String slipTypeCd;
    String slipForm;
    String status;
    String postingDt;
    String headerRemark;
    String usedCur;
    String usedAmt;
    String usedForAmt;
    String scanNo;
    String docNo;
    String evidenceYn;
    BigDecimal approvalGroupId;
    BigDecimal taxbillSupplyAmt;
    BigDecimal taxbillTaxAmt;
    BigDecimal taxbillTotalAmt;
    String taxSmartbillNo;
    BigDecimal ledgerId;
    String validationFlag;
    String errorMsg;
    String docTitle;
    String docUrl;
    String erpInvoiceId;
    String erpAppUserId;
    String erpVendorNm;
    String erpXtrSlipType;
    String erpTransactionNo;
    String taxbillAmtModifyYn;
    String taxbillSuId;
    String transferType;
    String remark;
    String regId;
    LocalDateTime regDtm;
    String nextAppUserId;
    String nextAppUserNm;
    String apprTypeCd;
    String apprDesc;
    String empNm;
    String empNo;
    String draftId;
    String statusNm;

    Boolean returnFlag;

    // 전표내역조회 / 전표관리
    @QueryProjection
    public SlipHistoryDto(String compCd,
                          String slipNo,
                          String deptNo,
                          String deptNm,
                          String empNo,
                          String empNm,
                          String slipType,
                          String slipTypeNm,
                          String slipTypeCd,
                          String status,
                          String statusNm,
                          String postingDt,
                          String headerRemark,
                          String usedCur,
                          String usedAmt,
                          String usedForAmt,
                          String scanNo,
                          BigDecimal slipHeaderId,
                          String slipGroupNo,
                          String docNo,
                          LocalDateTime regDtm,
                          String regId,
                          LocalDateTime chgDtm,
                          String chgId,
                          String slipForm,
                          String remark,
                          BigDecimal approvalGroupId,
                          BigDecimal taxbillSupplyAmt,
                          BigDecimal taxbillTaxAmt,
                          BigDecimal taxbillTotalAmt,
                          String taxSmartbillNo,
                          BigDecimal ledgerId,
                          String validationFlag,
                          String errorMsg,
                          String docTitle,
                          String docUrl,
                          String erpInvoiceId,
                          String erpAppUserId,
                          String erpXtrSlipType,
                          String erpTransactionNo,
                          String taxbillAmtModifyYn,
                          String taxbillSuId,

                          String invoiceTypeLookupCode,
                          String batchSourceName,
                          String categoryName,
                          String trxTypeCode,
                          String ttypeInputModule,
                          String ttypeInterfaceModule,
                          String ttypeInterfaceSlipType,
                          String ttypePrepaymentFlag,
                          String ttypeClearingAcctCode,
                          String ttypeAddInfoType,
                          String ttypeIntegrationVendorNum,
                          Integer ttypePaymentReceiptTermId,
                          String compCode,
                          String budgetDeptCode,
                          String projectCode,
                          BigDecimal projectId,
                          String taskCode,
                          String taskId,
                          String itemGroupCode,
                          BigDecimal codeCombinationId,
                          String drCr,
                          String acctCode,
                          LocalDateTime slipDate,
                          String taxEvidenceType,
                          String slipCurrencyCode,
                          String exchangeRateType,
                          LocalDateTime exchangeDate,
                          BigDecimal exchangeRate,
                          BigDecimal enteredAmount,
                          BigDecimal accountedAmount,

                          String referenceSlipModule,
                          String referenceSlipNumber,
                          String description,
                          String integrationVendorNum,
                          String integrationVendorName,
                          BigDecimal vendorId,
                          BigDecimal vendorSiteId,
                          BigDecimal vendorPartyId,
                          BigDecimal vendorPartySiteId,
                          BigDecimal customerId,
                          BigDecimal customerSiteId,
                          BigDecimal customerPartyId,
                          BigDecimal customerPartySiteId,
                          String termName,
                          Integer termId,
                          LocalDateTime termDueDate,
                          String poNumber,
                          String prepaymentApplyFlag,
                          String paymentReceiptMethodCode,
                          String payGroupLookupCode,
                          String noteFlag,
                          LocalDateTime maturityDate,
                          Integer awtGroupId,
                          String awtLocationCode,
                          String taxLocationCode,
                          String taxIssueTypeCode,
                          String taxCode,
                          Integer vatTaxId,
                          String taxAcctCode,

                          BigDecimal taxCodeCombinationId,
                          String evidenceVendorNum,
                          BigDecimal evidenceVendorCustSiteId,
                          LocalDateTime evidenceDate,
                          BigDecimal supplyAmount,
                          BigDecimal taxAmount,
                          String actualDeptCode,
                          String trBankAcctCode,
                          String slipTypeCode,
                          String segment9Code,
                          String segment10Code,
                          Integer bankAccountId,
                          String bankAccountName,
                          BigDecimal sourceSlipHeaderId,
                          String slipDisplayFlag,
                          String slipCreationTargetFlag,
                          String slipStatus,
                          String slipDataFixFlag,
                          String approvalCompleteFlag,
                          String createValidationFlag,
                          String createValidationErrMsg,
                          String slipIfFlag,
                          BigDecimal stdInvoiceTrxId,
                          LocalDateTime slipIfDate,

                          String slipIfLastApprovalUser,
                          String slipInterfaceErrorMsg,
                          String postingFlag,
                          String dueDateUpdateFlag,
                          String slipDeleteFlag,
                          LocalDateTime slipDeleteDate,
                          LocalDateTime erpSlipCancelDate,
                          String erpSlipCancelUser,
                          String attributeCategory,
                          String attribute1,
                          String attribute2,
                          String attribute3,
                          String attribute4,
                          String attribute5,
                          String attribute6,
                          String attribute7,
                          String attribute8,
                          String attribute9,
                          String attribute10,
                          String attribute11,
                          String attribute12,
                          String attribute13,
                          String attribute14,
                          String attribute15,
                          String globalAttributeCategory,
                          String globalAttribute1,
                          String globalAttribute2,
                          String globalAttribute3,
                          String globalAttribute4,
                          String globalAttribute5,
                          String globalAttribute6,
                          String globalAttribute7,
                          String globalAttribute8,
                          String globalAttribute9,
                          String globalAttribute10,
                          String globalAttribute11,
                          String globalAttribute12,
                          String globalAttribute13,
                          String globalAttribute14,
                          String globalAttribute15,
                          String headerAttribute1,
                          String headerAttribute2,
                          String headerAttribute3,
                          String headerAttribute4,
                          String headerAttribute5,
                          String headerAttribute6,
                          String headerAttribute7,
                          String headerAttribute8,
                          String headerAttribute9,
                          String headerAttribute10,
                          Integer createdPersonId,
                          String createdEmpNo,
                          LocalDateTime creationDate,
                          Integer lastUpdatedPersonId,
                          String lastUpdatedEmpNo,
                          LocalDateTime lastUpdateDate,
                          Integer lastUpdateLogin,
                          String vatAmtExceptionFlag,
                          String intStatus,
                          String nextAppUserId,
                          String nextAppUserNm,
                          String apprTypeCd,
                          String apprDesc,
                          String draftId){
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.deptNo = deptNo;
        this.deptNm = deptNm;
        this.empNo = empNo;
        this.empNm = empNm;
        this.slipType = slipType;
        this.slipTypeNm = slipTypeNm;
        this.slipTypeCd = slipTypeCd;
        this.status = status;
        this.statusNm = statusNm;
        this.postingDt = postingDt;
        this.headerRemark = headerRemark;
        this.usedCur = usedCur;
        this.usedAmt = usedAmt;
        this.usedForAmt = usedForAmt;
        this.scanNo = scanNo;
        this.slipHeaderId = slipHeaderId;
        this.slipGroupNo = slipGroupNo;
        this.docNo = docNo;
        this.regDtm = regDtm;
        this.regId = regId;
        this.chgDtm = chgDtm;
        this.chgId = chgId;
        this.slipForm = slipForm;
        this.remark = remark;
        this.approvalGroupId = approvalGroupId;
        this.taxbillSupplyAmt = taxbillSupplyAmt;
        this.taxbillTaxAmt = taxbillTaxAmt;
        this.taxbillTotalAmt = taxbillTotalAmt;
        this.taxSmartbillNo = taxSmartbillNo;
        this.ledgerId = ledgerId;
        this.validationFlag = validationFlag;
        this.errorMsg = errorMsg;
        this.docTitle = docTitle;
        this.docUrl = docUrl;
        this.erpInvoiceId = erpInvoiceId;
        this.erpAppUserId = erpAppUserId;
        this.erpXtrSlipType = erpXtrSlipType;
        this.erpTransactionNo = erpTransactionNo;
        this.taxbillAmtModifyYn = taxbillAmtModifyYn;
        this.taxbillSuId = taxbillSuId;

        this.invoiceTypeLookupCode = invoiceTypeLookupCode;
        this.batchSourceName = batchSourceName;
        this.categoryName = categoryName;
        this.trxTypeCode = trxTypeCode;
        this.ttypeInputModule = ttypeInputModule;
        this.ttypeInterfaceModule = ttypeInterfaceModule;
        this.ttypeInterfaceSlipType = ttypeInterfaceSlipType;
        this.ttypePrepaymentFlag = ttypePrepaymentFlag;
        this.ttypeClearingAcctCode = ttypeClearingAcctCode;
        this.ttypeAddInfoType = ttypeAddInfoType;
        this.ttypeIntegrationVendorNum = ttypeIntegrationVendorNum;
        this.ttypePaymentReceiptTermId = ttypePaymentReceiptTermId == null ? null : new BigDecimal(ttypePaymentReceiptTermId);
        this.compCode = compCode;
        this.budgetDeptCode = budgetDeptCode;
        this.projectCode = projectCode;
        this.projectId = projectId;
        this.taskCode = taskCode;
        this.taskId = taskId;
        this.itemGroupCode = itemGroupCode;
        this.codeCombinationId = codeCombinationId;
        this.drCr = drCr;
        this.acctCode = acctCode;
        this.slipDate = slipDate;
        this.taxEvidenceType = taxEvidenceType;
        this.slipCurrencyCode = slipCurrencyCode;
        this.exchangeRateType = exchangeRateType;
        this.exchangeDate = exchangeDate;
        this.exchangeRate = exchangeRate;
        this.enteredAmount = enteredAmount;
        this.accountedAmount = accountedAmount;
        this.referenceSlipModule = referenceSlipModule;
        this.referenceSlipNumber = referenceSlipNumber;
        this.description = description;
        this.integrationVendorNum = integrationVendorNum;
        this.integrationVendorName = integrationVendorName;
        this.vendorId = vendorId;
        this.vendorSiteId = vendorSiteId;
        this.vendorPartyId = vendorPartyId;
        this.vendorPartySiteId = vendorPartySiteId;
        this.customerId = customerId;
        this.customerSiteId = customerSiteId;
        this.customerPartyId = customerPartyId;
        this.customerPartySiteId = customerPartySiteId;
        this.termName = termName;
        this.termId = termId == null ? null : new BigDecimal(termId);
        this.termDueDate = termDueDate == null ? null : termDueDate.toString();
        this.poNumber = poNumber;
        this.prepaymentApplyFlag = prepaymentApplyFlag;
        this.paymentReceiptMethodCode = paymentReceiptMethodCode;
        this.payGroupLookupCode = payGroupLookupCode;
        this.noteFlag = noteFlag;
        this.maturityDate = maturityDate;
        this.awtGroupId = awtGroupId == null ? null : new BigDecimal(awtGroupId);
        this.awtLocationCode = awtLocationCode;
        this.taxLocationCode = taxLocationCode;
        this.taxIssueTypeCode = taxIssueTypeCode;
        this.taxCode = taxCode;
        this.vatTaxId = vatTaxId == null ? null : new BigDecimal(vatTaxId);
        this.taxAcctCode = taxAcctCode;
        this.taxCodeCombinationId = taxCodeCombinationId;
        this.evidenceVendorNum = evidenceVendorNum;
        this.evidenceVendorCustSiteId = evidenceVendorCustSiteId;
        this.evidenceDate = evidenceDate;
        this.supplyAmount = supplyAmount;
        this.taxAmount = taxAmount;
        this.actualDeptCode = actualDeptCode;
        this.trBankAcctCode = trBankAcctCode;
        this.slipTypeCode = slipTypeCode;
        this.segment9Code = segment9Code;
        this.segment10Code = segment10Code;
        this.bankAccountId = bankAccountId == null ? null : new BigDecimal(bankAccountId);
        this.bankAccountName = bankAccountName;
        this.sourceSlipHeaderId = sourceSlipHeaderId;
        this.slipDisplayFlag = slipDisplayFlag;
        this.slipCreationTargetFlag = slipCreationTargetFlag;
        this.slipStatus = slipStatus;
        this.slipDataFixFlag = slipDataFixFlag;
        this.approvalCompleteFlag = approvalCompleteFlag;
        this.createValidationFlag = createValidationFlag;
        this.createValidationErrMsg = createValidationErrMsg;
        this.slipIfFlag = slipIfFlag;
        this.stdInvoiceTrxId = stdInvoiceTrxId;
        this.slipIfDate = slipIfDate;
        this.slipIfLastApprovalUser = slipIfLastApprovalUser;
        this.slipInterfaceErrorMsg = slipInterfaceErrorMsg;
        this.postingFlag = postingFlag;
        this.dueDateUpdateFlag = dueDateUpdateFlag;
        this.slipDeleteFlag = slipDeleteFlag;
        this.slipDeleteDate = slipDeleteDate;
        this.erpSlipCancelDate = erpSlipCancelDate;
        this.erpSlipCancelUser = erpSlipCancelUser;
        this.attributeCategory = attributeCategory;
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
        this.attribute11 = attribute11;
        this.attribute12 = attribute12;
        this.attribute13 = attribute13;
        this.attribute14 = attribute14;
        this.attribute15 = attribute15;
        this.globalAttributeCategory = globalAttributeCategory;
        this.globalAttribute1 = globalAttribute1;
        this.globalAttribute2 = globalAttribute2;
        this.globalAttribute3 = globalAttribute3;
        this.globalAttribute4 = globalAttribute4;
        this.globalAttribute5 = globalAttribute5;
        this.globalAttribute6 = globalAttribute6;
        this.globalAttribute7 = globalAttribute7;
        this.globalAttribute8 = globalAttribute8;
        this.globalAttribute9 = globalAttribute9;
        this.globalAttribute10 = globalAttribute10;
        this.globalAttribute11 = globalAttribute11;
        this.globalAttribute12 = globalAttribute12;
        this.globalAttribute13 = globalAttribute13;
        this.globalAttribute14 = globalAttribute14;
        this.globalAttribute15 = globalAttribute15;
        this.headerAttribute1 = headerAttribute1;
        this.headerAttribute2 = headerAttribute2;
        this.headerAttribute3 = headerAttribute3;
        this.headerAttribute4 = headerAttribute4;
        this.headerAttribute5 = headerAttribute5;
        this.headerAttribute6 = headerAttribute6;
        this.headerAttribute7 = headerAttribute7;
        this.headerAttribute8 = headerAttribute8;
        this.headerAttribute9 = headerAttribute9;
        this.headerAttribute10 = headerAttribute10;
        this.createdPersonId = createdPersonId == null ? null : new BigDecimal(createdPersonId);
        this.createdEmpNo = createdEmpNo;
        this.creationDate = creationDate;
        this.lastUpdatedPersonId = lastUpdatedPersonId == null ? null : new BigDecimal(lastUpdatedPersonId);
        this.lastUpdatedEmpNo = lastUpdatedEmpNo;
        this.lastUpdateDate = lastUpdateDate;
        this.lastUpdateLogin = lastUpdateLogin == null ? null : new BigDecimal(lastUpdateLogin);
        this.vatAmtExceptionFlag = vatAmtExceptionFlag;
        this.intStatus = intStatus;
        this.nextAppUserId = nextAppUserId;
        this.nextAppUserNm = nextAppUserNm;
        this.apprTypeCd = apprTypeCd;
        this.apprDesc = apprDesc;
        this.draftId = draftId;
    }


    LocalDateTime chgDtm;
    String chgId;
    String invoiceTypeLookupCode;
    String batchSourceName;
    String categoryName;
    String trxTypeCode;
    String ttypeInputModule;
    String ttypeInterfaceModule;
    String ttypeInterfaceSlipType;
    String ttypePrepaymentFlag;
    String ttypeClearingAcctCode;
    String ttypeAddInfoType;
    String ttypeIntegrationVendorNum;
    BigDecimal ttypePaymentReceiptTermId;
    String compCode;
    String budgetDeptCode;
    String projectCode;
    BigDecimal projectId;
    String taskCode;
    String taskId;
    String itemGroupCode;
    BigDecimal codeCombinationId;
    String drCr;
    String acctCode;
    LocalDateTime slipDate;
    String taxEvidenceType;
    String slipCurrencyCode;
    String exchangeRateType;
    LocalDateTime exchangeDate;
    BigDecimal exchangeRate;
    BigDecimal enteredAmount;
    BigDecimal accountedAmount;
    String referenceSlipModule;
    String referenceSlipNumber;
    String description;
    String integrationVendorNum;
    String integrationVendorName;
    BigDecimal vendorId;
    BigDecimal vendorSiteId;
    BigDecimal vendorPartyId;
    BigDecimal vendorPartySiteId;
    BigDecimal customerId;
    BigDecimal customerSiteId;
    BigDecimal customerPartyId;
    BigDecimal customerPartySiteId;
    String termName;
    BigDecimal termId;
    String termDueDate;
    String poNumber;
    String prepaymentApplyFlag;
    String paymentReceiptMethodCode;
    String payGroupLookupCode;
    String noteFlag;
    LocalDateTime maturityDate;
    BigDecimal awtGroupId;
    String awtLocationCode;
    String taxLocationCode;
    String taxIssueTypeCode;
    String taxCode;
    BigDecimal vatTaxId;
    String taxAcctCode;
    BigDecimal taxCodeCombinationId;
    String evidenceVendorNum;
    BigDecimal evidenceVendorCustSiteId;
    LocalDateTime evidenceDate;
    BigDecimal supplyAmount;
    BigDecimal taxAmount;
    String actualDeptCode;
    String trBankAcctCode;
    String slipTypeCode;
    String segment9Code;
    String segment10Code;
    BigDecimal bankAccountId;
    String bankAccountName;
    BigDecimal sourceSlipHeaderId;
    String slipDisplayFlag;
    String slipCreationTargetFlag;
    String slipStatus;
    String slipDataFixFlag;
    String approvalCompleteFlag;
    String createValidationFlag;
    String createValidationErrMsg;
    String slipIfFlag;
    BigDecimal stdInvoiceTrxId;
    LocalDateTime slipIfDate;
    String slipIfLastApprovalUser;
    String slipInterfaceErrorMsg;
    String postingFlag;
    String dueDateUpdateFlag;
    String slipDeleteFlag;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime slipDeleteDate;
    LocalDateTime erpSlipCancelDate;
    String erpSlipCancelUser;
    String attributeCategory;
    String attribute1;
    String attribute2;
    String attribute3;
    String attribute4;
    String attribute5;
    String attribute6;
    String attribute7;
    String attribute8;
    String attribute9;
    String attribute10;
    String attribute11;
    String attribute12;
    String attribute13;
    String attribute14;
    String attribute15;
    String globalAttributeCategory;
    String globalAttribute1;
    String globalAttribute2;
    String globalAttribute3;
    String globalAttribute4;
    String globalAttribute5;
    String globalAttribute6;
    String globalAttribute7;
    String globalAttribute8;
    String globalAttribute9;
    String globalAttribute10;
    String globalAttribute11;
    String globalAttribute12;
    String globalAttribute13;
    String globalAttribute14;
    String globalAttribute15;
    String headerAttribute1;
    String headerAttribute2;
    String headerAttribute3;
    String headerAttribute4;
    String headerAttribute5;
    String headerAttribute6;
    String headerAttribute7;
    String headerAttribute8;
    String headerAttribute9;
    String headerAttribute10;
    BigDecimal createdPersonId;
    String createdEmpNo;
    LocalDateTime creationDate;
    BigDecimal lastUpdatedPersonId;
    String lastUpdatedEmpNo;
    LocalDateTime lastUpdateDate;
    BigDecimal lastUpdateLogin;
    String vatAmtExceptionFlag;
    String intStatus;
    String slipTypeNm;


    // 커스텀으로 생성(사용 x)
    @QueryProjection
    public SlipHistoryDto(String compCd,
                          String slipNo,
                          String deptNo,
                          String deptNm,
                          String slipType,
                          String slipTypeNm,
                          String status,
                          String postingDt,
                          String headerRemark,
                          String usedCur,
                          String usedAmt,
                          String scanNo,
                          BigDecimal slipHeaderId,
                          String slipGroupNo,
                          String docNo,
                          Timestamp regDtm,
                          String regId,
                          Timestamp chgDtm,
                          String chgId,
                          String slipForm,
                          String remark,
                          BigDecimal approvalGroupId,
                          BigDecimal taxbillSupplyAmt,
                          BigDecimal taxbillTaxAmt,
                          BigDecimal taxbillTotalAmt,
                          String taxSmartbillNo,
                          String ledgerId,
                          String validationFlag,
                          String errorMsg,
                          String docTitle,
                          String docUrl,
                          String erpInvoiceId,
                          String erpAppUserId,
                          String erpXtrSlipType,
                          String erpTransactionNo,
                          String taxbillAmtModifyYn,
                          String taxbillSuId,

                          String invoiceTypeLookupCode,
                          String batchSourceName,
                          String categoryName,
                          String trxTypeCode,
                          String ttypeInputModule,
                          String ttypeInterfaceModule,
                          String ttypeInterfaceSlipType,
                          String ttypePrepaymentFlag,
                          String ttypeClearingAcctCode,
                          String ttypeAddInfoType,
                          String ttypeIntegrationVendorNum,
                          BigDecimal ttypePaymentReceiptTermId,
                          String compCode,
                          String budgetDeptCode,
                          String projectCode,
                          BigDecimal projectId,
                          String taskCode,
                          String taskId,
                          String itemGroupCode,
                          BigDecimal codeCombinationId,
                          String drCr,
                          String acctCode,
                          Timestamp slipDate,
                          String taxEvidenceType,
                          String slipCurrencyCode,
                          String exchangeRateType,
                          Timestamp exchangeDate,
                          BigDecimal exchangeRate,
                          BigDecimal enteredAmount,
                          BigDecimal accountedAmount,
                          String referenceSlipModule,
                          String referenceSlipNumber,
                          String description,
                          String integrationVendorNum,
                          String integrationVendorName,
                          BigDecimal vendorId,
                          BigDecimal vendorSiteId,
                          BigDecimal vendorPartyId,
                          BigDecimal vendorPartySiteId,
                          BigDecimal customerId,
                          BigDecimal customerSiteId,
                          BigDecimal customerPartyId,
                          BigDecimal customerPartySiteId,
                          String termName,
                          BigDecimal termId,
                          String termDueDate,
                          String poNumber,
                          String prepaymentApplyFlag,
                          String paymentReceiptMethodCode,
                          String payGroupLookupCode,
                          String noteFlag,
                          Timestamp maturityDate,
                          BigDecimal awtGroupId,
                          String awtLocationCode,
                          String taxLocationCode,
                          String taxIssueTypeCode,
                          String taxCode,
                          BigDecimal vatTaxId,
                          String taxAcctCode,
                          BigDecimal taxCodeCombinationId,
                          String evidenceVendorNum,
                          BigDecimal evidenceVendorCustSiteId,
                          Timestamp evidenceDate,
                          BigDecimal supplyAmount,
                          BigDecimal taxAmount,
                          String actualDeptCode,
                          String trBankAcctCode,
                          String slipTypeCode,
                          String segment9Code,
                          String segment10Code,
                          BigDecimal bankAccountId,
                          String bankAccountName,
                          BigDecimal sourceSlipHeaderId,
                          String slipDisplayFlag,
                          String slipCreationTargetFlag,
                          String slipStatus,
                          String slipDataFixFlag,
                          String approvalCompleteFlag,
                          String createValidationFlag,
                          String createValidationErrMsg,
                          String slipIfFlag,
                          BigDecimal stdInvoiceTrxId,
                          Timestamp slipIfDate,
                          String slipIfLastApprovalUser,
                          String slipInterfaceErrorMsg,
                          String postingFlag,
                          String dueDateUpdateFlag,
                          String slipDeleteFlag,
                          Timestamp slipDeleteDate,
                          Timestamp erpSlipCancelDate,
                          String erpSlipCancelUser,
                          String attributeCategory,
                          String attribute1,
                          String attribute2,
                          String attribute3,
                          String attribute4,
                          String attribute5,
                          String attribute6,
                          String attribute7,
                          String attribute8,
                          String attribute9,
                          String attribute10,
                          String attribute11,
                          String attribute12,
                          String attribute13,
                          String attribute14,
                          String attribute15,
                          String globalAttributeCategory,
                          String globalAttribute1,
                          String globalAttribute2,
                          String globalAttribute3,
                          String globalAttribute4,
                          String globalAttribute5,
                          String globalAttribute6,
                          String globalAttribute7,
                          String globalAttribute8,
                          String globalAttribute9,
                          String globalAttribute10,
                          String globalAttribute11,
                          String globalAttribute12,
                          String globalAttribute13,
                          String globalAttribute14,
                          String globalAttribute15,
                          String headerAttribute1,
                          String headerAttribute2,
                          String headerAttribute3,
                          String headerAttribute4,
                          String headerAttribute5,
                          String headerAttribute6,
                          String headerAttribute7,
                          String headerAttribute8,
                          String headerAttribute9,
                          String headerAttribute10,
                          BigDecimal createdPersonId,
                          String createdEmpNo,
                          Timestamp creationDate,
                          BigDecimal lastUpdatedPersonId,
                          String lastUpdatedEmpNo,
                          Timestamp lastUpdateDate,
                          BigDecimal lastUpdateLogin,
                          String vatAmtExceptionFlag,
                          String intStatus,
                          BigDecimal rownum
                          ) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.deptNo = deptNo;
        this.slipType = slipType;
        this.status = status;
        this.postingDt = postingDt;
        this.headerRemark = headerRemark;
        this.usedCur = usedCur;
        this.usedAmt = usedAmt;
        this.scanNo = scanNo;
        this.slipHeaderId = slipHeaderId;
        this.slipGroupNo = slipGroupNo;
        this.docNo = docNo;
        this.regDtm = regDtm == null ? null : regDtm.toLocalDateTime();
        this.regId = regId;
        this.chgDtm = chgDtm == null ? null : chgDtm.toLocalDateTime();
        this.chgId = chgId;
        this.slipForm = slipForm;
        this.remark = remark;
        this.approvalGroupId = approvalGroupId;
        this.taxbillSupplyAmt = taxbillSupplyAmt;
        this.taxbillTaxAmt = taxbillTaxAmt;
        this.taxbillTotalAmt = taxbillTotalAmt;
        this.taxSmartbillNo = taxSmartbillNo;
        this.ledgerId = ledgerId == null ? null : new BigDecimal(ledgerId);
        this.validationFlag = validationFlag;
        this.errorMsg = errorMsg;
        this.docTitle = docTitle;
        this.docUrl = docUrl;
        this.erpInvoiceId = erpInvoiceId;
        this.erpAppUserId = erpAppUserId;
        this.erpXtrSlipType = erpXtrSlipType;
        this.erpTransactionNo = erpTransactionNo;
        this.taxbillAmtModifyYn = taxbillAmtModifyYn;
        this.taxbillSuId = taxbillSuId;

        this.invoiceTypeLookupCode = invoiceTypeLookupCode;
        this.batchSourceName = batchSourceName;
        this.categoryName = categoryName;
        this.trxTypeCode = trxTypeCode;
        this.ttypeInputModule = ttypeInputModule;
        this.ttypeInterfaceModule = ttypeInterfaceModule;
        this.ttypeInterfaceSlipType = ttypeInterfaceSlipType;
        this.ttypePrepaymentFlag = ttypePrepaymentFlag;
        this.ttypeClearingAcctCode = ttypeClearingAcctCode;
        this.ttypeAddInfoType = ttypeAddInfoType;
        this.ttypeIntegrationVendorNum = ttypeIntegrationVendorNum;
        this.ttypePaymentReceiptTermId = ttypePaymentReceiptTermId;
        this.compCode = compCode;
        this.budgetDeptCode = budgetDeptCode;
        this.projectCode = projectCode;
        this.projectId = projectId;
        this.taskCode = taskCode;
        this.taskId = taskId;
        this.itemGroupCode = itemGroupCode;
        this.codeCombinationId = codeCombinationId;
        this.drCr = drCr;
        this.acctCode = acctCode;
        this.slipDate = slipDate == null ? null : slipDate.toLocalDateTime();
        this.taxEvidenceType = taxEvidenceType;
        this.slipCurrencyCode = slipCurrencyCode;
        this.exchangeRateType = exchangeRateType;
        this.exchangeDate = exchangeDate == null ? null : exchangeDate.toLocalDateTime();
        this.exchangeRate = exchangeRate;
        this.enteredAmount = enteredAmount;
        this.accountedAmount = accountedAmount;
        this.referenceSlipModule = referenceSlipModule;
        this.referenceSlipNumber = referenceSlipNumber;
        this.description = description;
        this.integrationVendorNum = integrationVendorNum;
        this.integrationVendorName = integrationVendorName;
        this.vendorId = vendorId;
        this.vendorSiteId = vendorSiteId;
        this.vendorPartyId = vendorPartyId;
        this.vendorPartySiteId = vendorPartySiteId;
        this.customerId = customerId;
        this.customerSiteId = customerSiteId;
        this.customerPartyId = customerPartyId;
        this.customerPartySiteId = customerPartySiteId;
        this.termName = termName;
        this.termId = termId;
        this.termDueDate = termDueDate;
        this.poNumber = poNumber;
        this.prepaymentApplyFlag = prepaymentApplyFlag;
        this.paymentReceiptMethodCode = paymentReceiptMethodCode;
        this.payGroupLookupCode = payGroupLookupCode;
        this.noteFlag = noteFlag;
        this.maturityDate = maturityDate == null ? null : maturityDate.toLocalDateTime();
        this.awtGroupId = awtGroupId;
        this.awtLocationCode = awtLocationCode;
        this.taxLocationCode = taxLocationCode;
        this.taxIssueTypeCode = taxIssueTypeCode;
        this.taxCode = taxCode;
        this.vatTaxId = vatTaxId;
        this.taxAcctCode = taxAcctCode;
        this.taxCodeCombinationId = taxCodeCombinationId;
        this.evidenceVendorNum = evidenceVendorNum;
        this.evidenceVendorCustSiteId = evidenceVendorCustSiteId;
        this.evidenceDate = evidenceDate == null ? null : evidenceDate.toLocalDateTime();
        this.supplyAmount = supplyAmount;
        this.taxAmount = taxAmount;
        this.actualDeptCode = actualDeptCode;
        this.trBankAcctCode = trBankAcctCode;
        this.slipTypeCode = slipTypeCode;
        this.segment9Code = segment9Code;
        this.segment10Code = segment10Code;
        this.bankAccountId = bankAccountId;
        this.bankAccountName = bankAccountName;
        this.sourceSlipHeaderId = sourceSlipHeaderId;
        this.slipDisplayFlag = slipDisplayFlag;
        this.slipCreationTargetFlag = slipCreationTargetFlag;
        this.slipStatus = slipStatus;
        this.slipDataFixFlag = slipDataFixFlag;
        this.approvalCompleteFlag = approvalCompleteFlag;
        this.createValidationFlag = createValidationFlag;
        this.createValidationErrMsg = createValidationErrMsg;
        this.slipIfFlag = slipIfFlag;
        this.stdInvoiceTrxId = stdInvoiceTrxId;
        this.slipIfDate = slipIfDate == null ? null : slipIfDate.toLocalDateTime();
        this.slipIfLastApprovalUser = slipIfLastApprovalUser;
        this.slipInterfaceErrorMsg = slipInterfaceErrorMsg;
        this.postingFlag = postingFlag;
        this.dueDateUpdateFlag = dueDateUpdateFlag;
        this.slipDeleteFlag = slipDeleteFlag;
        this.slipDeleteDate = slipDeleteDate == null ? null : slipDeleteDate.toLocalDateTime();
        this.erpSlipCancelDate = erpSlipCancelDate == null ? null : erpSlipCancelDate.toLocalDateTime();
        this.erpSlipCancelUser = erpSlipCancelUser;
        this.attributeCategory = attributeCategory;
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
        this.attribute11 = attribute11;
        this.attribute12 = attribute12;
        this.attribute13 = attribute13;
        this.attribute14 = attribute14;
        this.attribute15 = attribute15;
        this.globalAttributeCategory = globalAttributeCategory;
        this.globalAttribute1 = globalAttribute1;
        this.globalAttribute2 = globalAttribute2;
        this.globalAttribute3 = globalAttribute3;
        this.globalAttribute4 = globalAttribute4;
        this.globalAttribute5 = globalAttribute5;
        this.globalAttribute6 = globalAttribute6;
        this.globalAttribute7 = globalAttribute7;
        this.globalAttribute8 = globalAttribute8;
        this.globalAttribute9 = globalAttribute9;
        this.globalAttribute10 = globalAttribute10;
        this.globalAttribute11 = globalAttribute11;
        this.globalAttribute12 = globalAttribute12;
        this.globalAttribute13 = globalAttribute13;
        this.globalAttribute14 = globalAttribute14;
        this.globalAttribute15 = globalAttribute15;
        this.headerAttribute1 = headerAttribute1;
        this.headerAttribute2 = headerAttribute2;
        this.headerAttribute3 = headerAttribute3;
        this.headerAttribute4 = headerAttribute4;
        this.headerAttribute5 = headerAttribute5;
        this.headerAttribute6 = headerAttribute6;
        this.headerAttribute7 = headerAttribute7;
        this.headerAttribute8 = headerAttribute8;
        this.headerAttribute9 = headerAttribute9;
        this.headerAttribute10 = headerAttribute10;
        this.createdPersonId = createdPersonId;
        this.createdEmpNo = createdEmpNo;
        this.creationDate = creationDate == null ? null : creationDate.toLocalDateTime();
        this.lastUpdatedPersonId = lastUpdatedPersonId;
        this.lastUpdatedEmpNo = lastUpdatedEmpNo;
        this.lastUpdateDate = lastUpdateDate == null ? null : lastUpdateDate.toLocalDateTime();
        this.lastUpdateLogin = lastUpdateLogin;
        this.vatAmtExceptionFlag = vatAmtExceptionFlag;
        this.intStatus = intStatus;
    }

    String postDtFrom;
    String postDtTo;
    String wrtDeptCd;
    String evdCustNm;
    //String slipTypeCd;
    String wrtId;
    String regDtFrom;
    String regDtTo;
    String slipStatCd;
    //String slipNo;
    String hdSgtxt;
    Integer page;
    Integer pageSize;
    String currency;

    //검색 DTO
    @QueryProjection
    public SlipHistoryDto(String compCd, String postDtFrom, String postDtTo, String wrtDeptCd, String evdCustNm, String slipTypeCd, String wrtId, String regDtFrom,
                          String regDtTo, String slipStatCd, String slipNo, String hdSgtxt, String nextAppUserId, Integer page, Integer pageSize, String currency,
                          BigDecimal termId, String intStatus){
        this.compCd = compCd;
        this.postDtFrom = postDtFrom;
        this.postDtTo = postDtTo;
        this.wrtDeptCd = wrtDeptCd;
        this.evdCustNm = evdCustNm;
        this.slipTypeCd = slipTypeCd;
        this.wrtId = wrtId;
        this.regDtFrom = regDtFrom;
        this.regDtTo = regDtTo;
        this.slipStatCd = slipStatCd;
        this.slipNo = slipNo;
        this.hdSgtxt = hdSgtxt;
        this.nextAppUserId = nextAppUserId;
        this.page = page;
        this.pageSize = pageSize;
        this.currency = currency;
        this.termId = termId;
        this.intStatus = intStatus;
    }

    BigDecimal count;
    public SlipHistoryDto(BigDecimal count){
        this.count = count;
    }

    String changeNo;
    String nextStage;

    public SlipHistoryDto(BigDecimal approvalGroupId, String slipNo, String changeNo, String nextStage){
        this.approvalGroupId = approvalGroupId;
        this.slipNo = slipNo;
        this.changeNo = changeNo;
        this.nextStage = nextStage;
    }
}
