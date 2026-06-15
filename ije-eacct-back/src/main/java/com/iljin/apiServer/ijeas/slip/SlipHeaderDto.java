package com.iljin.apiServer.ijeas.slip;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iljin.apiServer.ijeas.bond.BondHeaderDto;
import com.iljin.apiServer.ijeas.slip.header.SlipBusinessTrip;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
public class SlipHeaderDto  implements Serializable {
    private static final long serialVersionUID = -5667067810352765706L;

    //tb_slip_hd
    String compCd;
    String slipNo;
    BigDecimal slipHeaderId;
    String slipGroupNo;
    String deptNo;
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

    //cbo_sp_slip_header
    //BigDecimal slipHeaderId;
    String slipNumber;
    String slipGroupNumber;
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
    BigDecimal orgId;
    //Integer ledgerId;
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
    LocalDateTime glDate;
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
    LocalDateTime termDueDate;
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
    //String taxSmartbillNo;
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
    String poNumber;
    String slipDisplayFlag;
    String slipCreationTargetFlag;
    String slipStatus;
    //String validationFlag;
    String slipDataFixFlag;
    //Integer approvalGroupId;
    String approvalCompleteFlag;
    String createValidationFlag;
    String createValidationErrMsg;
    String slipIfFlag;
    BigDecimal stdInvoiceTrxId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime slipIfDate;
    String slipIfLastApprovalUser;
    String slipInterfaceErrorMsg;
    String postingFlag;
    LocalDateTime postingDate;
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
    //String scanNo;
    String vatAmtExceptionFlag;
    String slipForcedIfFlag;
    String globalAttribute16;


    public SlipHeaderDto(String compCd, String slipNo, BigDecimal slipHeaderId, String slipGroupNo, String deptNo, String slipType, String slipTypeCd, String slipForm,
                         String status, String postingDt, String headerRemark, String usedCur, String usedAmt, String usedForAmt, String scanNo, String docNo,
                         String evidenceYn, BigDecimal approvalGroupId, BigDecimal taxbillSupplyAmt, BigDecimal taxbillTaxAmt, BigDecimal taxbillTotalAmt,
                         String taxSmartbillNo, BigDecimal ledgerId, String validationFlag, String errorMsg, String docTitle, String docUrl, String erpInvoiceId,
                         String erpAppUserId, String erpVendorNm, String erpXtrSlipType, String erpTransactionNo, String taxbillAmtModifyYn, String taxbillSuId,
                         String transferType, String remark) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipHeaderId = slipHeaderId;
        this.slipGroupNo = slipGroupNo;
        this.deptNo = deptNo;
        this.slipType = slipType;
        this.slipTypeCd = slipTypeCd;
        this.slipForm = slipForm;
        this.status = status;
        this.postingDt = postingDt;
        this.headerRemark = headerRemark;
        this.usedCur = usedCur;
        this.usedAmt = usedAmt;
        this.usedForAmt = usedForAmt;
        this.scanNo = scanNo;
        this.docNo = docNo;
        this.evidenceYn = evidenceYn;
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
        this.erpVendorNm = erpVendorNm;
        this.erpXtrSlipType = erpXtrSlipType;
        this.erpTransactionNo = erpTransactionNo;
        this.taxbillAmtModifyYn = taxbillAmtModifyYn;
        this.taxbillSuId = taxbillSuId;
        this.transferType = transferType;
        this.remark = remark;
    }


    public SlipHeaderDto(BigDecimal slipHeaderId, String slipNumber, String slipGroupNumber, String invoiceTypeLookupCode, String batchSourceName, String categoryName,
                         String trxTypeCode, String ttypeInputModule, String ttypeInterfaceModule, String ttypeInterfaceSlipType, String ttypePrepaymentFlag,
                         String ttypeClearingAcctCode, String ttypeAddInfoType, String ttypeIntegrationVendorNum, BigDecimal ttypePaymentReceiptTermId, BigDecimal orgId,
                         BigDecimal ledgerId, String compCode, String budgetDeptCode, String projectCode, BigDecimal projectId, String taskCode, String taskId,
                         String itemGroupCode, BigDecimal codeCombinationId, String drCr, String acctCode, LocalDateTime glDate, LocalDateTime slipDate,
                         String taxEvidenceType, String slipCurrencyCode, String exchangeRateType, LocalDateTime exchangeDate, BigDecimal exchangeRate,
                         BigDecimal enteredAmount, BigDecimal accountedAmount, String referenceSlipModule, String referenceSlipNumber, String description,
                         String integrationVendorNum, BigDecimal vendorId, BigDecimal vendorSiteId, BigDecimal vendorPartyId, BigDecimal vendorPartySiteId, BigDecimal customerId,
                         BigDecimal customerSiteId, BigDecimal customerPartyId, BigDecimal customerPartySiteId, String termName, BigDecimal termId, LocalDateTime termDueDate,
                         String prepaymentApplyFlag, String paymentReceiptMethodCode, String payGroupLookupCode, String noteFlag, LocalDateTime maturityDate,
                         BigDecimal awtGroupId, String awtLocationCode, String taxLocationCode, String taxIssueTypeCode, String taxCode, BigDecimal vatTaxId,
                         String taxAcctCode, BigDecimal taxCodeCombinationId, String evidenceVendorNum, BigDecimal evidenceVendorCustSiteId, LocalDateTime evidenceDate,
                         String taxSmartbillNo, BigDecimal supplyAmount, BigDecimal taxAmount, String actualDeptCode, String trBankAcctCode, String slipTypeCode,
                         String segment9Code, String segment10Code, BigDecimal bankAccountId, String bankAccountName, BigDecimal sourceSlipHeaderId, String poNumber,
                         String slipDisplayFlag, String slipCreationTargetFlag, String slipStatus, String validationFlag, String slipDataFixFlag,
                         BigDecimal approvalGroupId, String approvalCompleteFlag, String createValidationFlag, String createValidationErrMsg, String slipIfFlag,
                         BigDecimal stdInvoiceTrxId, LocalDateTime slipIfDate, String slipIfLastApprovalUser, String slipInterfaceErrorMsg, String postingFlag,
                         LocalDateTime postingDate, String dueDateUpdateFlag, String slipDeleteFlag, LocalDateTime slipDeleteDate, LocalDateTime erpSlipCancelDate,
                         String erpSlipCancelUser, String attributeCategory, String attribute1, String attribute2, String attribute3, String attribute4,
                         String attribute5, String attribute6, String attribute7, String attribute8, String attribute9, String attribute10, String attribute11,
                         String attribute12, String attribute13, String attribute14, String attribute15, String globalAttributeCategory, String globalAttribute1,
                         String globalAttribute2, String globalAttribute3, String globalAttribute4, String globalAttribute5, String globalAttribute6,
                         String globalAttribute7, String globalAttribute8, String globalAttribute9, String globalAttribute10, String globalAttribute11,
                         String globalAttribute12, String globalAttribute13, String globalAttribute14, String globalAttribute15, String headerAttribute1,
                         String headerAttribute2, String headerAttribute3, String headerAttribute4, String headerAttribute5, String headerAttribute6,
                         String headerAttribute7, String headerAttribute8, String headerAttribute9, String headerAttribute10, BigDecimal createdPersonId,
                         String createdEmpNo, LocalDateTime creationDate, BigDecimal lastUpdatedPersonId, String lastUpdatedEmpNo, LocalDateTime lastUpdateDate,
                         BigDecimal lastUpdateLogin, String scanNo, String vatAmtExceptionFlag, String slipForcedIfFlag, String globalAttribute16
    ){
        this.slipHeaderId = slipHeaderId;
        this.slipNumber = slipNumber;
        this.slipGroupNumber = slipGroupNumber;
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
        this.orgId = orgId;
        this.ledgerId = ledgerId;
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
        this.glDate = glDate;
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
        this.prepaymentApplyFlag = prepaymentApplyFlag;
        this.paymentReceiptMethodCode = paymentReceiptMethodCode;
        this.payGroupLookupCode = payGroupLookupCode;
        this.noteFlag = noteFlag;
        this.maturityDate = maturityDate;
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
        this.evidenceDate = evidenceDate;
        this.taxSmartbillNo = taxSmartbillNo;
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
        this.poNumber = poNumber;
        this.slipDisplayFlag = slipDisplayFlag;
        this.slipCreationTargetFlag = slipCreationTargetFlag;
        this.slipStatus = slipStatus;
        this.validationFlag = validationFlag;
        this.slipDataFixFlag = slipDataFixFlag;
        this.approvalGroupId = approvalGroupId;
        this.approvalCompleteFlag = approvalCompleteFlag;
        this.createValidationFlag = createValidationFlag;
        this.createValidationErrMsg = createValidationErrMsg;
        this.slipIfFlag = slipIfFlag;
        this.stdInvoiceTrxId = stdInvoiceTrxId;
        this.slipIfDate = slipIfDate;
        this.slipIfLastApprovalUser = slipIfLastApprovalUser;
        this.slipInterfaceErrorMsg = slipInterfaceErrorMsg;
        this.postingFlag = postingFlag;
        this.postingDate = postingDate;
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
        this.createdPersonId = createdPersonId;
        this.createdEmpNo = createdEmpNo;
        this.creationDate = creationDate;
        this.lastUpdatedPersonId = lastUpdatedPersonId;
        this.lastUpdatedEmpNo = lastUpdatedEmpNo;
        this.lastUpdateDate = lastUpdateDate;
        this.lastUpdateLogin = lastUpdateLogin;
        this.scanNo = scanNo;
        this.vatAmtExceptionFlag = vatAmtExceptionFlag;
        this.slipForcedIfFlag = slipForcedIfFlag;
        this.globalAttribute16 = globalAttribute16;
    }

    String trxTypeName;
    String budgetDeptName;
    String taskItemGroup;
    String itemGroupType;
    String projectName;
    String taskName;
    String itemGroupName;
    String coaFullCode;
    String coaFullDesc;
    String headerAcctCode;
    String headerAcctName;
    String taxEvidenceTypeName;
    String lineAttribute1;
    String lineAttribute1Name;
    String lineAttribute2;
    String lineAttribute3;
    BigDecimal slipCurrencyPrecision;
    String integrationVendorName;
    String vatRegistrationNum;
    BigDecimal prepayCnt;
    String termDescription;
    String vendorAcctCheck;
    BigDecimal percentageRate;
    String evidenceVendorName;
    String evidenceVatRegistrationNum;
    String deptType;
    String trBankAcctName;
    String slipTypeName;
    String segment9Name;
    String segment10Name;
    //BigDecimal sourceSlipHeaderId;
    String slipStatusName;
    //String slipCreationTargetFlag;
    String trAcctCode;
    String repaymentDueDate;
    String userId;
    String accountYn;
    BigDecimal taxbillSupplyAmount;
    BigDecimal taxbillTaxAmount;
    BigDecimal taxbillTotalAmount;
    String evidenceFlag;
    String userName;
    BigDecimal personId;
    String taxLocationName;
    String taxIssueTypeName;
    String personIntegrationVendorNum;
    String personIntegrationVendorName;
    BigDecimal personCustomerId;
    BigDecimal personCustomerSiteId;
    BigDecimal personVendorId;
    BigDecimal personVendorSiteId;
    BigDecimal personVendorPartyId;
    BigDecimal personVendorPartySiteId;
    BigDecimal personCustomerPartyId;
    BigDecimal personCustomerPartySiteId;
    BigDecimal amountToApply;

    String empNm;
    String taxAcctName;
    String deptCd;
    String deptNm;
    String prepaymentYn;
    String slipReusePossibleYn;
    String slipCopyYn;
    BigDecimal fileCnt;
    BigDecimal jiniCnt;

    BigDecimal jiniFileCnt;

    //전표 조회
    public SlipHeaderDto(BigDecimal slipHeaderId, String slipNumber, String slipGroupNumber, String invoiceTypeLookupCode, String batchSourceName,
                         String trxTypeCode, String trxTypeName, String ttypeInputModule, String ttypeInterfaceModule, String ttypeInterfaceSlipType,
                         String ttypePrepaymentFlag, String ttypeClearingAcctCode, String ttypeAddInfoType, String ttypeIntegrationVendorNum,
                         BigDecimal ttypePaymentReceiptTermId, BigDecimal orgId, BigDecimal ledgerId, String compCode, String budgetDeptCode, String budgetDeptName,
                         String projectCode, BigDecimal projectId, String taskItemGroup, String itemGroupType, String projectName,String taskCode, String taskId,
                         String taskName, String  itemGroupCode, String itemGroupName, BigDecimal codeCombinationId, String coaFullCode, String coaFullDesc,
                         String headerAcctCode, String headerAcctName, Timestamp glDate, Timestamp slipDate, String taxEvidenceType, String taxEvidenceTypeName,
                         String lineAttribute1, String lineAttribute1Name, String lineAttribute2, String lineAttribute3, String slipCurrencyCode,
                         BigDecimal slipCurrencyPrecision, String exchangeRateType, Timestamp exchangeDate, BigDecimal exchangeRate, BigDecimal enteredAmount,
                         BigDecimal accountedAmount, String referenceSlipModule, String referenceSlipNumber, String description, String integrationVendorNum,
                         String integrationVendorName, String vatRegistrationNum, BigDecimal vendorId, BigDecimal vendorSiteId, BigDecimal vendorPartyId,
                         BigDecimal vendorPartySiteId, BigDecimal customerId, BigDecimal customerSiteId, BigDecimal customerPartyId, BigDecimal customerPartySiteId,
                         BigDecimal prepayCnt, String payGroupLookupCode, String termName, BigDecimal termId, String termDescription, String vendorAcctCheck,
                         Timestamp termDueDate, String prepaymentApplyFlag, String paymentReceiptMethodCode, String noteFlag, Timestamp maturityDate,
                         BigDecimal awtGroupId, String awtLocationCode, String taxCode, BigDecimal vatTaxId, String taxAcctCode, String taxAcctName,
                         BigDecimal taxCodeCombinationId, BigDecimal percentageRate, String evidenceVendorNum, String evidenceVendorName, BigDecimal evidenceVendorCustSiteId,
                         String evidenceVatRegistrationNum, Timestamp evidenceDate,  BigDecimal supplyAmount, BigDecimal taxAmount, String deptType, String trBankAcctCode,
                         String trBankAcctName, String slipTypeCode, String slipTypeName, String segment9Code, String segment9Name, String segment10Code,
                         String segment10Name, BigDecimal bankAccountId, String bankAccountName, BigDecimal sourceSlipHeaderId, String poNumber, String slipStatus,
                         String slipStatusName, String drCr, String slipDisplayFlag, String slipCreationTargetFlag, String validationFlag, BigDecimal approvalGroupId,
                         String approvalCompleteFlag, String slipIfFlag, BigDecimal stdInvoiceTrxId, Timestamp slipIfDate, String slipIfLastApprovalUser,
                         String slipInterfaceErrorMsg, String dueDateUpdateFlag, String slipDeleteFlag, Timestamp slipDeleteDate, Timestamp erpSlipCancelDate,
                         String trAcctCode, String taxSmartbillNo, String attributeCategory, String attribute1, String attribute2, String attribute3, String attribute4,
                         String attribute5, String attribute6, String attribute7, String attribute8, String attribute9, String attribute10, String attribute11,
                         String attribute12, String attribute13, String attribute14, String attribute15, String repaymentDueDate, String globalAttributeCategory, String globalAttribute1,
                         String globalAttribute2, String globalAttribute3, String globalAttribute4, String globalAttribute5, String globalAttribute6, String globalAttribute7,
                         String globalAttribute8, String globalAttribute9, String globalAttribute10, String globalAttribute11, String globalAttribute12,
                         String globalAttribute13, String globalAttribute14, String globalAttribute15,  String headerAttribute1, String headerAttribute2,
                         String headerAttribute3, String headerAttribute4, String headerAttribute5, String headerAttribute6, String headerAttribute7, String headerAttribute8,
                         String headerAttribute9, String headerAttribute10, BigDecimal createdPersonId, String empNo, Timestamp creationDate, BigDecimal lastUpdatedPersonId,
                         String lastUpdatedEmpNo, Timestamp lastUpdateDate, BigDecimal lastUpdateLogin, String postingDt, String slipNo, String scanNo, String accountYn,
                         BigDecimal taxbillSupplyAmount, BigDecimal taxbillTaxAmount, BigDecimal taxbillTotalAmount, String remark, String evidenceFlag, String docTitle,
                         String docUrl, String taxbillAmtModifyYn, String taxbillSuId, String empNm, BigDecimal personId, String taxLocationCode, String taxLocationName,
                         String taxIssueTypeCode, String taxIssueTypeName, String personIntegrationVendorNum, String personIntegrationVendorName, BigDecimal personCustomerId,
                         BigDecimal personCustomerSiteId, BigDecimal personVendorId, BigDecimal personVendorSiteId, BigDecimal personVendorPartyId,
                         BigDecimal personVendorPartySiteId, BigDecimal personCustomerPartyId, BigDecimal personCustomerPartySiteId, BigDecimal amountToApply,
                         String deptCd, String deptNm, String slipTypeCd, Character prepaymentYn, BigDecimal fileCnt, BigDecimal jiniCnt, BigDecimal jiniFileCnt
    ){
        this.slipHeaderId = slipHeaderId;
        this.slipNumber = slipNumber;
        this.slipGroupNumber = slipGroupNumber;
        this.invoiceTypeLookupCode = invoiceTypeLookupCode;
        this.batchSourceName = batchSourceName;
        this.trxTypeCode = trxTypeCode;
        this.trxTypeName = trxTypeName;
        this.ttypeInputModule = ttypeInputModule;
        this.ttypeInterfaceModule = ttypeInterfaceModule;
        this.ttypeInterfaceSlipType = ttypeInterfaceSlipType;
        this.ttypePrepaymentFlag = ttypePrepaymentFlag;
        this.ttypeClearingAcctCode = ttypeClearingAcctCode;
        this.ttypeAddInfoType = ttypeAddInfoType;
        this.ttypeIntegrationVendorNum = ttypeIntegrationVendorNum;
        this.ttypePaymentReceiptTermId = ttypePaymentReceiptTermId;
        this.orgId = orgId;
        this.ledgerId = ledgerId;
        this.compCode = compCode;
        this.budgetDeptCode = budgetDeptCode;
        this.budgetDeptName = budgetDeptName;
        this.projectCode = projectCode;
        this.projectId = projectId;
        this.taskItemGroup = taskItemGroup;
        this.itemGroupType = itemGroupType;
        this.projectName = projectName;
        this.taskCode = taskCode;
        this.taskId = taskId;
        this.taskName = taskName;
        this.itemGroupCode = itemGroupCode;
        this.itemGroupName = itemGroupName;
        this.codeCombinationId = codeCombinationId;
        this.coaFullCode = coaFullCode;
        this.coaFullDesc = coaFullDesc;
        this.headerAcctCode = headerAcctCode;
        this.headerAcctName = headerAcctName;
        this.glDate = glDate == null ? null : glDate.toLocalDateTime();
        this.slipDate =  slipDate == null ? null : slipDate.toLocalDateTime();
        this.taxEvidenceType = taxEvidenceType;
        this.taxEvidenceTypeName = taxEvidenceTypeName;
        this.lineAttribute1 = lineAttribute1;
        this.lineAttribute1Name = lineAttribute1Name;
        this.lineAttribute2 = lineAttribute2;
        this.lineAttribute3 = lineAttribute3;
        this.slipCurrencyCode = slipCurrencyCode;
        this.slipCurrencyPrecision = slipCurrencyPrecision;
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
        this.vatRegistrationNum = vatRegistrationNum;
        this.vendorId = vendorId;
        this.vendorSiteId = vendorSiteId;
        this.vendorPartyId = vendorPartyId;
        this.vendorPartySiteId = vendorPartySiteId;
        this.customerId = customerId;
        this.customerSiteId = customerSiteId;
        this.customerPartyId = customerPartyId;
        this.customerPartySiteId = customerPartySiteId;
        this.prepayCnt = prepayCnt;
        this.payGroupLookupCode = payGroupLookupCode;
        this.termName = termName;
        this.termId = termId;
        this.termDescription = termDescription;
        this.vendorAcctCheck = vendorAcctCheck;
        this.termDueDate = termDueDate == null ? null : termDueDate.toLocalDateTime();
        this.prepaymentApplyFlag = prepaymentApplyFlag;
        this.paymentReceiptMethodCode = paymentReceiptMethodCode;
        this.noteFlag = noteFlag;
        this.maturityDate = maturityDate == null ? null : maturityDate.toLocalDateTime();
        this.awtGroupId = awtGroupId;
        this.awtLocationCode = awtLocationCode;
        this.taxCode = taxCode;
        this.vatTaxId = vatTaxId;
        this.taxAcctCode = taxAcctCode;
        this.taxAcctName = taxAcctName;
        this.taxCodeCombinationId = taxCodeCombinationId;
        this.percentageRate = percentageRate;
        this.evidenceVendorNum = evidenceVendorNum;
        this.evidenceVendorName = evidenceVendorName;
        this.evidenceVendorCustSiteId = evidenceVendorCustSiteId;
        this.evidenceVatRegistrationNum = evidenceVatRegistrationNum;
        this.evidenceDate = evidenceDate == null ? null : evidenceDate.toLocalDateTime();
        this.supplyAmount = supplyAmount;
        this.taxAmount = taxAmount;
        this.deptType = deptType;
        this.trBankAcctCode = trBankAcctCode;
        this.trBankAcctName = trBankAcctName;
        this.slipTypeCode = slipTypeCode;
        this.slipTypeName = slipTypeName;
        this.segment9Code = segment9Code;
        this.segment9Name = segment9Name;
        this.segment10Code = segment10Code;
        this.segment10Name = segment10Name;
        this.bankAccountId = bankAccountId;
        this.bankAccountName = bankAccountName;
        this.sourceSlipHeaderId = sourceSlipHeaderId;
        this.poNumber = poNumber;
        this.slipStatus = slipStatus;
        this.slipStatusName = slipStatusName;
        this.drCr = drCr;
        this.slipDisplayFlag = slipDisplayFlag;
        this.slipCreationTargetFlag = slipCreationTargetFlag;
        this.validationFlag = validationFlag;
        this.approvalGroupId = approvalGroupId;
        this.approvalCompleteFlag = approvalCompleteFlag;
        this.slipIfFlag = slipIfFlag;
        this.stdInvoiceTrxId = stdInvoiceTrxId;
        this.slipIfDate = slipIfDate == null ? null : slipIfDate.toLocalDateTime();
        this.slipIfLastApprovalUser = slipIfLastApprovalUser;
        this.slipInterfaceErrorMsg = slipInterfaceErrorMsg;
        this.dueDateUpdateFlag = dueDateUpdateFlag;
        this.slipDeleteFlag = slipDeleteFlag;
        this.slipDeleteDate = slipDeleteDate == null ? null : slipDeleteDate.toLocalDateTime();
        this.erpSlipCancelDate = erpSlipCancelDate == null ? null : erpSlipCancelDate.toLocalDateTime();
        this.trAcctCode = trAcctCode;
        this.taxSmartbillNo = taxSmartbillNo;
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
        this.repaymentDueDate = repaymentDueDate;
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
        this.empNo = empNo;
        this.creationDate = creationDate == null ? null : creationDate.toLocalDateTime();
        this.lastUpdatedPersonId = lastUpdatedPersonId;
        this.lastUpdatedEmpNo = lastUpdatedEmpNo;
        this.lastUpdateDate = lastUpdateDate == null ? null : lastUpdateDate.toLocalDateTime();
        this.lastUpdateLogin = lastUpdateLogin;
        this.postingDt = postingDt;
        this.slipNo = slipNo;
        this.scanNo = scanNo;
        this.accountYn = accountYn;
        this.taxbillSupplyAmount = taxbillSupplyAmount;
        this.taxbillTaxAmount = taxbillTaxAmount;
        this.taxbillTotalAmount = taxbillTotalAmount;
        this.remark = remark;
        this.evidenceFlag = evidenceFlag;
        this.docTitle = docTitle;
        this.docUrl = docUrl;
        this.taxbillAmtModifyYn = taxbillAmtModifyYn;
        this.taxbillSuId = taxbillSuId;
        this.empNm = empNm;
        this.personId = personId;
        this.taxLocationCode = taxLocationCode;
        this.taxLocationName = taxLocationName;
        this.taxIssueTypeCode = taxIssueTypeCode;
        this.taxIssueTypeName = taxIssueTypeName;
        this.personIntegrationVendorNum = personIntegrationVendorNum;
        this.personIntegrationVendorName = personIntegrationVendorName;
        this.personCustomerId = personCustomerId;
        this.personCustomerSiteId = personCustomerSiteId;
        this.personVendorId = personVendorId;
        this.personVendorSiteId = personVendorSiteId;
        this.personVendorPartyId = personVendorPartyId;
        this.personVendorPartySiteId = personVendorPartySiteId;
        this.personCustomerPartyId = personCustomerPartyId;
        this.personCustomerPartySiteId = personCustomerPartySiteId;
        this.amountToApply = amountToApply;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.slipTypeCd = slipTypeCd;
        this.prepaymentYn = prepaymentYn == null ? "N" : String.valueOf(prepaymentYn);
        this.fileCnt = fileCnt;
        this.jiniCnt = jiniCnt;
        this.jiniFileCnt = jiniFileCnt;
    }


    String tripPlace;
    String tripFromDt;
    String tripToDt;
    String tripObj;
    String projectNo;
    String tripCd;
    String temp1;
    String temp2;
    String temp3;
    String temp4;
    String temp5;

    //전표 조회(교통비)
    public SlipHeaderDto(String tripPlace, String tripFromDt, String tripToDt, String tripObj,
                         String projectNo, String tripCd, String temp1, String temp2, String temp3, String temp4, String temp5
    ){
        this.tripPlace = tripPlace;
        this.tripFromDt = tripFromDt;
        this.tripToDt = tripToDt;
        this.tripObj = tripObj;
        this.projectNo = projectNo;
        this.tripCd = tripCd;
        this.temp1 = temp1;
        this.temp2 = temp2;
        this.temp3 = temp3;
        this.temp4 = temp4;
        this.temp5 = temp5;
    }

    String slipGroupYn;
    String slipFormText;
    String slipTypeText;
    String deptCode;
    String deptName;
    String strPostingDate;
    String addDate;
    String headerText;
    String erpVendorName;
    String fromDate;
    String toDate;
    String empNo;

    //항공권 전표 조회 파라미터
    public SlipHeaderDto(String compCd, String fromDate, String toDate, String empNo, BigDecimal slipHeaderId){
        this.compCd = compCd;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.empNo = empNo;
        this.slipHeaderId = slipHeaderId;
    }

    //항공권 전표 조회
    public SlipHeaderDto(String compCd, BigDecimal slipHeaderId, BigDecimal approvalGroupId, String slipGroupYn, String status, String slipStatus, String slipStatusName,
                         String slipForm, String slipFormText, String slipType, String slipTypeText, String userId, String userName, String deptCode, String deptName,
                         String strPostingDate, String slipNo, String slipGroupNumber, String usedCur, String addDate, String headerText, BigDecimal ledgerId,
                         String usedAmt, String ttypeInputModule, String integrationVendorNum, String erpVendorName){
        this.compCd = compCd;
        this.slipHeaderId = slipHeaderId;
        this.approvalGroupId = approvalGroupId;
        this.slipGroupYn = slipGroupYn;
        this.status = status;
        this.slipStatus = slipStatus;
        this.slipStatusName = slipStatusName;
        this.slipForm = slipForm;
        this.slipFormText = slipFormText;
        this.slipType = slipType;
        this.slipTypeText = slipTypeText;
        this.userId = userId;
        this.userName = userName;
        this.deptCode = deptCode;
        this.deptName = deptName;
        this.strPostingDate = strPostingDate;
        this.slipNo = slipNo;
        this.slipGroupNumber = slipGroupNumber;
        this.usedCur = usedCur;
        this.addDate = addDate;
        this.headerText = headerText;
        this.ledgerId = ledgerId;
        this.usedAmt = usedAmt;
        this.ttypeInputModule = ttypeInputModule;
        this.integrationVendorNum = integrationVendorNum;
        this.erpVendorName = erpVendorName;
    }

    // 발생 전표 삭제시, 그룹 전표 상태값 조회
    @QueryProjection
    public SlipHeaderDto(BigDecimal slipHeaderId, BigDecimal approvalGroupId, String compCd,
                         String slipType, BigDecimal ledgerId, String slipIfFlag){
        this.slipHeaderId = slipHeaderId;
        this.approvalGroupId = approvalGroupId;
        this.compCd = compCd;
        this.slipType = slipType;
        this.ledgerId = ledgerId;
        this.slipIfFlag = slipIfFlag;
    }

    // 발생 전표 삭제시, 상태값 조회
    @QueryProjection
    public SlipHeaderDto(BigDecimal approvalGroupId, String compCd,
                         String slipType, BigDecimal ledgerId, String status,
                         String slipNo, String taxSmartbillNo){
        this.approvalGroupId = approvalGroupId;
        this.compCd = compCd;
        this.slipType = slipType;
        this.ledgerId = ledgerId;
        this.status = status;
        this.slipNo = slipNo;
        this.taxSmartbillNo = taxSmartbillNo;
    }

    public SlipHeaderDto(BigDecimal taxbillSupplyAmt, BigDecimal taxbillTaxAmt,
        BigDecimal taxbillTotalAmt, String taxLocationCode) {
        this.taxbillSupplyAmt = taxbillSupplyAmt;
        this.taxbillTaxAmt = taxbillTaxAmt;
        this.taxbillTotalAmt = taxbillTotalAmt;
        this.taxLocationCode = taxLocationCode;
    }

    List<SlipBusinessTrip> slipBusinessTripList;

    public SlipHeaderDto(List<SlipBusinessTrip> slipBusinessTripList){
        this.slipBusinessTripList = slipBusinessTripList;
    }

    BondHeaderDto bond;
    public SlipHeaderDto(BondHeaderDto bond){
        this.bond = bond;
    }

}
