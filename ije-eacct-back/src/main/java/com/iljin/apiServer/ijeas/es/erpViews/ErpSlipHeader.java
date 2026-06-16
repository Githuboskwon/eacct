package com.iljin.apiServer.ijeas.es.erpViews;

import static java.util.Objects.nonNull;

import com.iljin.apiServer.ijeas.slip.SlipDto;
import com.iljin.apiServer.ijeas.system.emp.Employee;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@IdClass(ErpSlipHeaderKey.class)
@Table(name = "CBO_SP_SLIP_HEADER")
@Entity
public class ErpSlipHeader {

    @Id
    @Column(name = "SLIP_HEADER_ID")
    BigDecimal slipHeaderId;

    @Column(name = "SLIP_NUMBER")
    String slipNumber;

    @Column(name = "SLIP_GROUP_NUMBER")
    String slipGroupNumber;

    @Column(name = "INVOICE_TYPE_LOOKUP_CODE")
    String invoiceTypeLookupCode;

    @Column(name = "BATCH_SOURCE_NAME")
    String batchSourceName;

    @Column(name = "CATEGORY_NAME")
    String categoryName;

    @Column(name = "TRX_TYPE_CODE")
    String trxTypeCode;

    @Column(name = "TTYPE_INPUT_MODULE")
    String ttypeInputModule;

    @Column(name = "TTYPE_INTERFACE_MODULE")
    String ttypeInterfaceModule;

    @Column(name = "TTYPE_INTERFACE_SLIP_TYPE")
    String ttypeInterfaceSlipType;

    @Column(name = "TTYPE_PREPAYMENT_FLAG")
    String ttypePrepaymentFlag;

    @Column(name = "TTYPE_CLEARING_ACCT_CODE")
    String ttypeClearingAcctCode;

    @Column(name = "TTYPE_ADD_INFO_TYPE")
    String ttypeAddInfoType;

    @Column(name = "TTYPE_INTEGRATION_VENDOR_NUM")
    String ttypeIntegrationVendorNum;

    @Column(name = "TTYPE_PAYMENT_RECEIPT_TERM_ID")
    Integer ttypePaymentReceiptTermId;

    @Id
    @Column(name = "ORG_ID")
    Integer orgId;

    @Column(name = "LEDGER_ID")
    Integer ledgerId;

    @Column(name = "COMP_CODE")
    String compCode;

    @Column(name = "BUDGET_DEPT_CODE")
    String budgetDeptCode;

    @Column(name = "PROJECT_CODE")
    String projectCode;

    @Column(name = "PROJECT_ID")
    BigDecimal projectId;

    @Column(name = "TASK_CODE")
    String taskCode;

    @Column(name = "TASK_ID")
    String taskId;

    @Column(name = "ITEM_GROUP_CODE")
    String itemGroupCode;

    @Column(name = "CODE_COMBINATION_ID")
    BigDecimal codeCombinationId;

    @Column(name = "DR_CR")
    String drCr;

    @Column(name = "ACCT_CODE")
    String acctCode;

    @Column(name = "GL_DATE")
    LocalDateTime glDate;

    @Column(name = "SLIP_DATE")
    LocalDateTime slipDate;

    @Column(name = "TAX_EVIDENCE_TYPE")
    String taxEvidenceType;

    @Column(name = "SLIP_CURRENCY_CODE")
    String slipCurrencyCode;

    @Column(name = "EXCHANGE_RATE_TYPE")
    String exchangeRateType;

    @Column(name = "EXCHANGE_DATE")
    LocalDateTime exchangeDate;

    @Column(name = "EXCHANGE_RATE")
    BigDecimal exchangeRate;

    @Column(name = "ENTERED_AMOUNT")
    BigDecimal enteredAmount;

    @Column(name = "ACCOUNTED_AMOUNT")
    BigDecimal accountedAmount;

    @Column(name = "REFERENCE_SLIP_MODULE")
    String referenceSlipModule;

    @Column(name = "REFERENCE_SLIP_NUMBER")
    String referenceSlipNumber;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "INTEGRATION_VENDOR_NUM")
    String integrationVendorNum;

    @Column(name = "VENDOR_ID")
    BigDecimal vendorId;

    @Column(name = "VENDOR_SITE_ID")
    BigDecimal vendorSiteId;

    @Column(name = "VENDOR_PARTY_ID")
    BigDecimal vendorPartyId;

    @Column(name = "VENDOR_PARTY_SITE_ID")
    BigDecimal vendorPartySiteId;

    @Column(name = "CUSTOMER_ID")
    BigDecimal customerId;

    @Column(name = "CUSTOMER_SITE_ID")
    BigDecimal customerSiteId;

    @Column(name = "CUSTOMER_PARTY_ID")
    BigDecimal customerPartyId;

    @Column(name = "CUSTOMER_PARTY_SITE_ID")
    BigDecimal customerPartySiteId;

    @Column(name = "TERM_NAME")
    String termName;

    @Column(name = "TERM_ID")
    Integer termId;

    @Column(name = "TERM_DUE_DATE")
    LocalDateTime termDueDate;

    @Column(name = "PREPAYMENT_APPLY_FLAG")
    String prepaymentApplyFlag;

    @Column(name = "PAYMENT_RECEIPT_METHOD_CODE")
    String paymentReceiptMethodCode;

    @Column(name = "PAY_GROUP_LOOKUP_CODE")
    String payGroupLookupCode;

    @Column(name = "NOTE_FLAG")
    String noteFlag;

    @Column(name = "MATURITY_DATE")
    LocalDateTime maturityDate;

    @Column(name = "AWT_GROUP_ID")
    Integer awtGroupId;

    @Column(name = "AWT_LOCATION_CODE")
    String awtLocationCode;

    @Column(name = "TAX_LOCATION_CODE")
    String taxLocationCode;

    @Column(name = "TAX_ISSUE_TYPE_CODE")
    String taxIssueTypeCode;

    @Column(name = "TAX_CODE")
    String taxCode;

    @Column(name = "VAT_TAX_ID")
    Integer vatTaxId;

    @Column(name = "TAX_ACCT_CODE")
    String taxAcctCode;

    @Column(name = "TAX_CODE_COMBINATION_ID")
    BigDecimal taxCodeCombinationId;

    @Column(name = "EVIDENCE_VENDOR_NUM")
    String evidenceVendorNum;

    @Column(name = "EVIDENCE_VENDOR_CUST_SITE_ID")
    BigDecimal evidenceVendorCustSiteId;

    @Column(name = "EVIDENCE_DATE")
    LocalDateTime evidenceDate;

    @Column(name = "TAX_SMARTBILL_NO")
    String taxSmartbillNo;

    @Column(name = "SUPPLY_AMOUNT")
    BigDecimal supplyAmount;

    @Column(name = "TAX_AMOUNT")
    BigDecimal taxAmount;

    @Column(name = "ACTUAL_DEPT_CODE")
    String actualDeptCode;

    @Column(name = "TR_BANK_ACCT_CODE")
    String trBankAcctCode;

    @Column(name = "SLIP_TYPE_CODE")
    String slipTypeCode;

    @Column(name = "SEGMENT9_CODE")
    String segment9Code;

    @Column(name = "SEGMENT10_CODE")
    String segment10Code;

    @Column(name = "BANK_ACCOUNT_ID")
    Integer bankAccountId;

    @Column(name = "BANK_ACCOUNT_NAME")
    String bankAccountName;

    @Column(name = "SOURCE_SLIP_HEADER_ID")
    BigDecimal sourceSlipHeaderId;

    @Column(name = "PO_NUMBER")
    String poNumber;

    @Column(name = "SLIP_DISPLAY_FLAG")
    String slipDisplayFlag;

    @Column(name = "SLIP_CREATION_TARGET_FLAG")
    String slipCreationTargetFlag;

    @Column(name = "SLIP_STATUS")
    String slipStatus;

    @Column(name = "VALIDATION_FLAG")
    String validationFlag;

    @Column(name = "SLIP_DATA_FIX_FLAG")
    String slipDataFixFlag;

    @Column(name = "APPROVAL_GROUP_ID")
    BigDecimal approvalGroupId;

    @Column(name = "APPROVAL_COMPLETE_FLAG")
    String approvalCompleteFlag;

    @Column(name = "CREATE_VALIDATION_FLAG")
    String createValidationFlag;

    @Column(name = "CREATE_VALIDATION_ERR_MSG")
    String createValidationErrMsg;

    @Column(name = "SLIP_IF_FLAG")
    String slipIfFlag;

    @Column(name = "STD_INVOICE_TRX_ID")
    BigDecimal stdInvoiceTrxId;

    @Column(name = "SLIP_IF_DATE")
    LocalDateTime slipIfDate;

    @Column(name = "SLIP_IF_LAST_APPROVAL_USER")
    String slipIfLastApprovalUser;

    @Column(name = "SLIP_INTERFACE_ERROR_MSG")
    String slipInterfaceErrorMsg;

    @Column(name = "POSTING_FLAG")
    String postingFlag;

    @Column(name = "POSTING_DATE")
    LocalDateTime postingDate;

    @Column(name = "DUE_DATE_UPDATE_FLAG")
    String dueDateUpdateFlag;

    @Column(name = "SLIP_DELETE_FLAG")
    String slipDeleteFlag;

    @Column(name = "SLIP_DELETE_DATE")
    LocalDateTime slipDeleteDate;

    @Column(name = "ERP_SLIP_CANCEL_DATE")
    LocalDateTime erpSlipCancelDate;

    @Column(name = "ERP_SLIP_CANCEL_USER")
    String erpSlipCancelUser;

    @Column(name = "ATTRIBUTE_CATEGORY")
    String attributeCategory;

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

    @Column(name = "ATTRIBUTE6")
    String attribute6;

    @Column(name = "ATTRIBUTE7")
    String attribute7;

    @Column(name = "ATTRIBUTE8")
    String attribute8;

    @Column(name = "ATTRIBUTE9")
    String attribute9;

    @Column(name = "ATTRIBUTE10")
    String attribute10;

    @Column(name = "ATTRIBUTE11")
    String attribute11;

    @Column(name = "ATTRIBUTE12")
    String attribute12;

    @Column(name = "ATTRIBUTE13")
    String attribute13;

    @Column(name = "ATTRIBUTE14")
    String attribute14;

    @Column(name = "ATTRIBUTE15")
    String attribute15;

    @Column(name = "GLOBAL_ATTRIBUTE_CATEGORY")
    String globalAttributeCategory;

    @Column(name = "GLOBAL_ATTRIBUTE1")
    String globalAttribute1;

    @Column(name = "GLOBAL_ATTRIBUTE2")
    String globalAttribute2;

    @Column(name = "GLOBAL_ATTRIBUTE3")
    String globalAttribute3;

    @Column(name = "GLOBAL_ATTRIBUTE4")
    String globalAttribute4;

    @Column(name = "GLOBAL_ATTRIBUTE5")
    String globalAttribute5;

    @Column(name = "GLOBAL_ATTRIBUTE6")
    String globalAttribute6;

    @Column(name = "GLOBAL_ATTRIBUTE7")
    String globalAttribute7;

    @Column(name = "GLOBAL_ATTRIBUTE8")
    String globalAttribute8;

    @Column(name = "GLOBAL_ATTRIBUTE9")
    String globalAttribute9;

    @Column(name = "GLOBAL_ATTRIBUTE10")
    String globalAttribute10;

    @Column(name = "GLOBAL_ATTRIBUTE11")
    String globalAttribute11;

    @Column(name = "GLOBAL_ATTRIBUTE12")
    String globalAttribute12;

    @Column(name = "GLOBAL_ATTRIBUTE13")
    String globalAttribute13;

    @Column(name = "GLOBAL_ATTRIBUTE14")
    String globalAttribute14;

    @Column(name = "GLOBAL_ATTRIBUTE15")
    String globalAttribute15;

    @Column(name = "HEADER_ATTRIBUTE1")
    String headerAttribute1;

    @Column(name = "HEADER_ATTRIBUTE2")
    String headerAttribute2;

    @Column(name = "HEADER_ATTRIBUTE3")
    String headerAttribute3;

    @Column(name = "HEADER_ATTRIBUTE4")
    String headerAttribute4;

    @Column(name = "HEADER_ATTRIBUTE5")
    String headerAttribute5;

    @Column(name = "HEADER_ATTRIBUTE6")
    String headerAttribute6;

    @Column(name = "HEADER_ATTRIBUTE7")
    String headerAttribute7;

    @Column(name = "HEADER_ATTRIBUTE8")
    String headerAttribute8;

    @Column(name = "HEADER_ATTRIBUTE9")
    String headerAttribute9;

    @Column(name = "HEADER_ATTRIBUTE10")
    String headerAttribute10;

    @Column(name = "CREATED_PERSON_ID")
    Integer createdPersonId;

    @Column(name = "CREATED_EMP_NO")
    String createdEmpNo;

    @Column(name = "CREATION_DATE")
    LocalDateTime creationDate;

    @Column(name = "LAST_UPDATED_PERSON_ID")
    Integer lastUpdatedPersonId;

    @Column(name = "LAST_UPDATED_EMP_NO")
    String lastUpdatedEmpNo;

    @Column(name = "LAST_UPDATE_DATE")
    LocalDateTime lastUpdateDate;

    @Column(name = "LAST_UPDATE_LOGIN")
    Integer lastUpdateLogin;

    @Column(name = "SCAN_NO")
    String scanNo;

    @Column(name = "VAT_AMT_EXCEPTION_FLAG")
    String vatAmtExceptionFlag;

    @Column(name = "SLIP_FORCED_IF_FLAG")
    String slipForcedIfFlag;

    @Column(name = "GLOBAL_ATTRIBUTE16")
    String globalAttribute16;

    @Builder
    public ErpSlipHeader(BigDecimal slipHeaderId, String slipNumber, String slipGroupNumber,
        String invoiceTypeLookupCode, String batchSourceName, String categoryName, String trxTypeCode,
        String ttypeInputModule, String ttypeInterfaceModule, String ttypeInterfaceSlipType,
        String ttypePrepaymentFlag, String ttypeClearingAcctCode, String ttypeAddInfoType,
        String ttypeIntegrationVendorNum, Integer ttypePaymentReceiptTermId, Integer orgId,
        Integer ledgerId, String compCode, String budgetDeptCode, String projectCode, BigDecimal projectId,
        String taskCode, String taskId, String itemGroupCode, BigDecimal codeCombinationId, String drCr,
        String acctCode, LocalDateTime glDate, LocalDateTime slipDate, String taxEvidenceType,
        String slipCurrencyCode, String exchangeRateType, LocalDateTime exchangeDate,
        BigDecimal exchangeRate, BigDecimal enteredAmount, BigDecimal accountedAmount,
        String referenceSlipModule, String referenceSlipNumber, String description,
        String integrationVendorNum, BigDecimal vendorId, BigDecimal vendorSiteId, BigDecimal vendorPartyId,
        BigDecimal vendorPartySiteId, BigDecimal customerId, BigDecimal customerSiteId, BigDecimal customerPartyId,
        BigDecimal customerPartySiteId, String termName, Integer termId, LocalDateTime termDueDate,
        String prepaymentApplyFlag, String paymentReceiptMethodCode, String payGroupLookupCode,
        String noteFlag, LocalDateTime maturityDate, Integer awtGroupId, String awtLocationCode,
        String taxLocationCode, String taxIssueTypeCode, String taxCode, Integer vatTaxId,
        String taxAcctCode, BigDecimal taxCodeCombinationId, String evidenceVendorNum,
        BigDecimal evidenceVendorCustSiteId, LocalDateTime evidenceDate, String taxSmartbillNo,
        BigDecimal supplyAmount, BigDecimal taxAmount, String actualDeptCode, String trBankAcctCode,
        String slipTypeCode, String segment9Code, String segment10Code, Integer bankAccountId,
        String bankAccountName, BigDecimal sourceSlipHeaderId, String poNumber, String slipDisplayFlag,
        String slipCreationTargetFlag, String slipStatus, String validationFlag,
        String slipDataFixFlag, BigDecimal approvalGroupId, String approvalCompleteFlag,
        String createValidationFlag, String createValidationErrMsg, String slipIfFlag,
        BigDecimal stdInvoiceTrxId, LocalDateTime slipIfDate, String slipIfLastApprovalUser,
        String slipInterfaceErrorMsg, String postingFlag, LocalDateTime postingDate,
        String dueDateUpdateFlag, String slipDeleteFlag, LocalDateTime slipDeleteDate,
        LocalDateTime erpSlipCancelDate, String erpSlipCancelUser, String attributeCategory,
        String attribute1, String attribute2, String attribute3, String attribute4, String attribute5,
        String attribute6, String attribute7, String attribute8, String attribute9, String attribute10,
        String attribute11, String attribute12, String attribute13, String attribute14,
        String attribute15, String globalAttributeCategory, String globalAttribute1,
        String globalAttribute2, String globalAttribute3, String globalAttribute4,
        String globalAttribute5, String globalAttribute6, String globalAttribute7,
        String globalAttribute8, String globalAttribute9, String globalAttribute10,
        String globalAttribute11, String globalAttribute12, String globalAttribute13,
        String globalAttribute14, String globalAttribute15, String headerAttribute1,
        String headerAttribute2, String headerAttribute3, String headerAttribute4,
        String headerAttribute5, String headerAttribute6, String headerAttribute7,
        String headerAttribute8, String headerAttribute9, String headerAttribute10,
        Integer createdPersonId, String createdEmpNo, LocalDateTime creationDate,
        Integer lastUpdatedPersonId, String lastUpdatedEmpNo, LocalDateTime lastUpdateDate,
        Integer lastUpdateLogin, String scanNo, String vatAmtExceptionFlag, String slipForcedIfFlag,
        String globalAttribute16) {
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

    public static ErpSlipHeader from(SlipDto slipDto) {
        String invoiceTypeLookUpCode = "STANDARD";
        if(slipDto.getTtypePrepaymentFlag().trim().equals("Y")) {
            invoiceTypeLookUpCode = "PREPAYMENT";
        } else {
            if(slipDto.getEnteredAmount().compareTo(new BigDecimal(0)) < 0) {
                invoiceTypeLookUpCode = "CREDIT";
            }
        }
        return ErpSlipHeader.builder()
            .slipHeaderId(slipDto.getSlipHeaderId())
            .slipNumber(slipDto.getSlipNo())
            .slipGroupNumber(slipDto.getSlipGroupNo())
            .invoiceTypeLookupCode(invoiceTypeLookUpCode)
            .batchSourceName("SP")
            .trxTypeCode(slipDto.getTrxTypeCode())
            .ttypeInputModule(slipDto.getTtypeInputModule())
            .ttypeInterfaceModule(slipDto.getTtypeInterfaceModule())
            .ttypeInterfaceSlipType(slipDto.getTtypeInterfaceSlipType())
            .ttypePrepaymentFlag(slipDto.getTtypePrepaymentFlag())
            .ttypeClearingAcctCode(slipDto.getTtypeClearingAcctCode())
            .ttypeAddInfoType(slipDto.getTtypeAddInfoType())
            .ttypeIntegrationVendorNum(slipDto.getTtypeIntegrationVendorNum())
            .ttypePaymentReceiptTermId(slipDto.getTtypePaymentReceiptTermId())
            .orgId(Integer.parseInt(slipDto.getCompCd()))
            .ledgerId(slipDto.getLedgerId().intValue())
            .compCode(slipDto.getCompCode())
            .budgetDeptCode(slipDto.getBudgetDeptCode())
            .projectCode(slipDto.getProjectCode())
            .projectId(slipDto.getProjectId())
            .taskCode(slipDto.getTaskCode())
            .taskId(slipDto.getTaskId())
            .itemGroupCode(slipDto.getItemGroupCode())
            .codeCombinationId(slipDto.getCodeCombinationId())
            .acctCode(slipDto.getAcctCode())
            .glDate(parseDate(slipDto.getPostingDate()))
            .slipDate(nonNull(slipDto.getSlipDate()) ? parseDate(slipDto.getSlipDate()) : parseDate(slipDto.getPostingDate()))
            .taxEvidenceType(slipDto.getTaxEvidenceType())
            .slipCurrencyCode(slipDto.getSlipCurrencyCode())
            .exchangeRateType(slipDto.getExchangeRateType())
            .exchangeDate(parseDate(slipDto.getExchangeDate()))
            .exchangeRate(slipDto.getExchangeRate())
            .enteredAmount(slipDto.getEnteredAmount())
            .accountedAmount(slipDto.getAccountedAmount())
            .referenceSlipModule(slipDto.getReferenceSlipModule())
            .referenceSlipNumber(slipDto.getReferenceSlipNumber())
            .description(slipDto.getDescription())
            .integrationVendorNum(slipDto.getIntegrationVendorNum())
            .vendorId(slipDto.getVendorId())
            .vendorSiteId(slipDto.getVendorSiteId())
            .vendorPartyId(slipDto.getVendorPartyId())
            .vendorPartySiteId(slipDto.getVendorPartySiteId())
            .customerId(slipDto.getCustomerId())
            .customerSiteId(slipDto.getCustomerSiteId())
            .customerPartyId(slipDto.getCustomerPartyId())
            .customerPartySiteId(slipDto.getCustomerPartySiteId())
            .termName(slipDto.getTermName())
            .termId(slipDto.getTermId())
            .termDueDate(parseDate(slipDto.getTermDueDate()))
            .prepaymentApplyFlag(slipDto.getPrepaymentApplyFlag())
            .paymentReceiptMethodCode(slipDto.getPaymentReceiptMethodCode())
            .payGroupLookupCode(slipDto.getPayGroupLookupCode())
            .noteFlag(slipDto.getNoteFlag())
            .maturityDate(parseDate(slipDto.getMaturityDate()))
            .awtGroupId(slipDto.getAwtGroupId())
            .awtLocationCode(slipDto.getAwtLocationCode())
            .taxLocationCode(slipDto.getTaxLocationCode())
            .taxIssueTypeCode(slipDto.getTaxIssueTypeCode())
            .taxCode(slipDto.getTaxRateCode())
            .vatTaxId(slipDto.getTaxRateId())
            .taxAcctCode(slipDto.getTaxAcctCode())
            .taxCodeCombinationId(slipDto.getTaxCodeCombinationId())
            .evidenceVendorNum(slipDto.getEvidenceVendorNum())
            .evidenceVendorCustSiteId(slipDto.getEvidenceVendorCustSiteId())
            .evidenceDate(parseDate(slipDto.getEvidenceDate()))
            .taxSmartbillNo(slipDto.getTaxSmartbillNo())
            .supplyAmount(slipDto.getSupplyAmount())
            .taxAmount(slipDto.getTaxAmount())
            .actualDeptCode(slipDto.getActualDeptCode())
            .trBankAcctCode(slipDto.getTrBankAcctCode())
            .slipTypeCode(slipDto.getSlipTypeCode())
            .segment9Code(slipDto.getSegment9Code())
            .segment10Code(slipDto.getSegment10Code())
            .bankAccountId(slipDto.getBankAccountId())
            .bankAccountName(slipDto.getBankAccountName())
            .sourceSlipHeaderId(slipDto.getSourceSlipHeaderId())
            .poNumber(slipDto.getPoNumber())
            .slipDisplayFlag(slipDto.getSlipDisplayFlag())
            .slipCreationTargetFlag(slipDto.getSlipCreationTargetFlag())
            .slipStatus("SV")
            .validationFlag("N")
            .slipDataFixFlag("N")
            .approvalGroupId(nonNull(slipDto.getApprovalGroupId()) ? slipDto.getApprovalGroupId() : slipDto.getSlipHeaderId())
            .approvalCompleteFlag("N")
            .createValidationFlag("N")
            .createValidationErrMsg(slipDto.getCreateValidationErrMsg())
            .slipIfFlag("N")
            .stdInvoiceTrxId(slipDto.getStdInvoiceTrxId())
            .slipIfDate(slipDto.getSlipIfDate())
            .slipIfLastApprovalUser(slipDto.getSlipIfLastApprovalUser())
            .slipInterfaceErrorMsg(slipDto.getSlipInterfaceErrorMsg())
            .postingFlag("N")
            .drCr(slipDto.getDrCr())
            .postingDate(null)
            .dueDateUpdateFlag("N")
            .slipDeleteFlag("N")
            .slipForcedIfFlag("N")
//            .slipDeleteDate(erpSlipHeaderDto.getSlipDeleteDate())
//            .erpSlipCancelDate(erpSlipHeaderDto.getErpSlipCancelDate())
//            .erpSlipCancelUser(erpSlipHeaderDto.getErpSlipCancelUser())
            .attributeCategory(slipDto.getAttributeCategory())
            .attribute1(slipDto.getAttribute1())
            .attribute2(slipDto.getAttribute2())
            .attribute3(slipDto.getAttribute3())
            .attribute4(slipDto.getAttribute4())
            .attribute5(slipDto.getAttribute5())
            .attribute6(slipDto.getAttribute6())
            .attribute7(slipDto.getAttribute7())
            .attribute8(slipDto.getAttribute8())
            .attribute9(slipDto.getAttribute9())
            .attribute10(slipDto.getAttribute10())
            .attribute11(slipDto.getAttribute11())
            .attribute12(slipDto.getAttribute12())
            .attribute13(slipDto.getAttribute13())
            .attribute14(slipDto.getAttribute14())
            .attribute15(slipDto.getRepaymentDueDate())
            .globalAttributeCategory(slipDto.getGlobalAttributeCategory())
            .globalAttribute1(slipDto.getGlobalAttribute1())
            .globalAttribute2(slipDto.getGlobalAttribute2())
            .globalAttribute3(slipDto.getGlobalAttribute3())
            .globalAttribute4(slipDto.getGlobalAttribute4())
            .globalAttribute5(slipDto.getGlobalAttribute5())
            .globalAttribute6(slipDto.getGlobalAttribute6())
            .globalAttribute7(slipDto.getGlobalAttribute7())
            .globalAttribute8(slipDto.getGlobalAttribute8())
            .globalAttribute9(slipDto.getGlobalAttribute9())
            .globalAttribute10(slipDto.getGlobalAttribute10())
            .globalAttribute11(slipDto.getGlobalAttribute11())
            .globalAttribute12(slipDto.getGlobalAttribute12())
            .globalAttribute13(slipDto.getGlobalAttribute13())
            .globalAttribute14(slipDto.getGlobalAttribute14())
            .globalAttribute15(slipDto.getGlobalAttribute15())
            .headerAttribute1(slipDto.getHeaderAttribute1())
            .headerAttribute2(slipDto.getHeaderAttribute2())
            .headerAttribute3(slipDto.getHeaderAttribute3())
            .headerAttribute4(slipDto.getHeaderAttribute4())
            .headerAttribute5(slipDto.getHeaderAttribute5())
            .headerAttribute6(slipDto.getHeaderAttribute6())
            .headerAttribute7(slipDto.getHeaderAttribute7())
            .headerAttribute8(slipDto.getHeaderAttribute8())
            .headerAttribute9(slipDto.getHeaderAttribute9())
            .headerAttribute10(slipDto.getHeaderAttribute10())
            .createdPersonId(slipDto.getPersonId())
            .createdEmpNo(slipDto.getEmpNo())
            .creationDate(LocalDateTime.now())
            .lastUpdatedPersonId(slipDto.getPersonId())
            .lastUpdatedEmpNo(slipDto.getEmpNo())
            .lastUpdateDate(LocalDateTime.now())
            .scanNo(slipDto.getScanNo())
            .build();
    }

    public void update(SlipDto slipDto) {
        String invoiceTypeLookUpCode = "STANDARD";
        if(slipDto.getTtypePrepaymentFlag().trim().equals("Y")) {
            invoiceTypeLookUpCode = "PREPAYMENT";
        } else {
            if(slipDto.getEnteredAmount().compareTo(new BigDecimal(0)) < 0) {
                invoiceTypeLookUpCode = "CREDIT";
            }
        }
        this.invoiceTypeLookupCode = invoiceTypeLookUpCode;
        this.batchSourceName = "SP";
        this.trxTypeCode = slipDto.getTrxTypeCode();
        this.ttypeInputModule = slipDto.getTtypeInputModule();
        this.ttypeInterfaceModule = slipDto.getTtypeInterfaceModule();
        this.ttypePrepaymentFlag = slipDto.getTtypePrepaymentFlag();
        this.ttypeClearingAcctCode = slipDto.getTtypeClearingAcctCode();
        this.ttypeAddInfoType = slipDto.getTtypeAddInfoType();
        this.ttypeIntegrationVendorNum = slipDto.getTtypeIntegrationVendorNum();
        this.ttypePaymentReceiptTermId = slipDto.getTtypePaymentReceiptTermId();
        this.drCr = slipDto.getDrCr();
        this.taxSmartbillNo = slipDto.getTaxSmartbillNo();
        this.slipDisplayFlag = slipDto.getSlipDisplayFlag();
        this.slipCreationTargetFlag = slipDto.getSlipCreationTargetFlag();
        this.orgId = Integer.parseInt(slipDto.getCompCd());
        this.ledgerId = slipDto.getLedgerId().intValue();
        this.compCode = slipDto.getCompCode();
        this.budgetDeptCode = slipDto.getBudgetDeptCode();
        this.projectCode = slipDto.getProjectCode();
        this.projectId = slipDto.getProjectId();
        this.taskCode = slipDto.getTaskCode();
        this.taskId = slipDto.getTaskId();
        this.itemGroupCode = slipDto.getItemGroupCode();
        this.codeCombinationId = slipDto.getCodeCombinationId();
        this.acctCode = slipDto.getAcctCode();
        this.glDate = parseDate(slipDto.getPostingDate());
        this.slipDate = nonNull(slipDto.getSlipDate()) ? parseDate(slipDto.getSlipDate()) : parseDate(slipDto.getPostingDate());
        this.taxEvidenceType = slipDto.getTaxEvidenceType();
        this.slipCurrencyCode = slipDto.getSlipCurrencyCode();
        this.exchangeRateType = slipDto.getExchangeRateType();
        this.exchangeDate = parseDate(slipDto.getExchangeDate());
        this.exchangeRate = slipDto.getExchangeRate();
        this.enteredAmount = slipDto.getEnteredAmount();
        this.accountedAmount = slipDto.getAccountedAmount();
        this.referenceSlipModule = slipDto.getReferenceSlipModule();
        this.referenceSlipNumber = slipDto.getReferenceSlipNumber();
        this.description = slipDto.getDescription();
        this.integrationVendorNum = slipDto.getIntegrationVendorNum();
        this.vendorId = slipDto.getVendorId();
        this.vendorSiteId = slipDto.getVendorSiteId();
        this.vendorPartyId = slipDto.getVendorPartyId();
        this.vendorPartySiteId = slipDto.getVendorPartySiteId();
        this.customerId = slipDto.getCustomerId();
        this.customerSiteId = slipDto.getCustomerSiteId();
        this.customerPartyId = slipDto.getCustomerPartyId();
        this.customerPartySiteId = slipDto.getCustomerPartySiteId();
        this.termName = slipDto.getTermName();
        this.termId = slipDto.getTermId();
        this.termDueDate = parseDate(slipDto.getTermDueDate());
        this.prepaymentApplyFlag = slipDto.getPrepaymentApplyFlag();
        this.paymentReceiptMethodCode = slipDto.getPaymentReceiptMethodCode();
        this.payGroupLookupCode = slipDto.getPayGroupLookupCode();
        this.noteFlag = slipDto.getNoteFlag();
        this.maturityDate = parseDate(slipDto.getMaturityDate());
        this.awtGroupId = slipDto.getAwtGroupId();
        this.awtLocationCode = slipDto.getAwtLocationCode();
        this.taxLocationCode = slipDto.getTaxLocationCode();
        this.taxIssueTypeCode = slipDto.getTaxIssueTypeCode();
        this.taxCode = slipDto.getTaxRateCode();
        this.vatTaxId = slipDto.getTaxRateId();
        this.taxAcctCode = slipDto.getTaxAcctCode();
        this.taxCodeCombinationId = slipDto.getTaxCodeCombinationId();
        this.evidenceVendorNum = slipDto.getEvidenceVendorNum();
        this.evidenceVendorCustSiteId = slipDto.getEvidenceVendorCustSiteId();
        this.evidenceDate = parseDate(slipDto.getEvidenceDate());
        this.supplyAmount = slipDto.getSupplyAmount();
        this.taxAmount = slipDto.getTaxAmount();
        this.actualDeptCode = slipDto.getActualDeptCode();
        this.trBankAcctCode = slipDto.getTrBankAcctCode();
        this.slipTypeCode = slipDto.getSlipTypeCode();
        this.segment9Code = slipDto.getSegment9Code();
        this.segment10Code = slipDto.getSegment10Code();
        this.bankAccountId = slipDto.getBankAccountId();
        this.bankAccountName = slipDto.getBankAccountName();
        this.poNumber = slipDto.getPoNumber();
        this.slipStatus = "SV";
        this.validationFlag = "N";
        this.slipDataFixFlag = "N";
        this.approvalGroupId = nonNull(slipDto.getApprovalGroupId()) ? slipDto.getApprovalGroupId() : slipDto.getSlipHeaderId();
        this.postingDate = null;
        this.stdInvoiceTrxId = slipDto.getStdInvoiceTrxId();
        this.slipIfDate = slipDto.getSlipIfDate();
        this.slipIfLastApprovalUser = slipDto.getSlipIfLastApprovalUser();
        this.slipInterfaceErrorMsg = slipDto.getSlipInterfaceErrorMsg();
        this.createValidationErrMsg = slipDto.getCreateValidationErrMsg();
        this.approvalCompleteFlag = "N";
        this.createValidationFlag = "N";
        this.slipIfFlag = "N";
        this.postingFlag = "N";
        this.dueDateUpdateFlag = "N";
        this.slipDeleteFlag = "N";
        this.slipForcedIfFlag = "N";
        this.attributeCategory = slipDto.getAttributeCategory();
        this.attribute1 = slipDto.getAttribute1();
        this.attribute2 = slipDto.getAttribute2();
        this.attribute3 = slipDto.getAttribute3();
        this.attribute4 = slipDto.getAttribute4();
        this.attribute5 = slipDto.getAttribute5();
        this.attribute6 = slipDto.getAttribute6();
        this.attribute7 = slipDto.getAttribute7();
        this.attribute8 = slipDto.getAttribute8();
        this.attribute9 = slipDto.getAttribute9();
        this.attribute10 = slipDto.getAttribute10();
        this.attribute11 = slipDto.getAttribute11();
        this.attribute12 = slipDto.getAttribute12();
        this.attribute13 = slipDto.getAttribute13();
        this.attribute14 = slipDto.getAttribute14();
        this.attribute15 = slipDto.getRepaymentDueDate();
        this.globalAttributeCategory = slipDto.getGlobalAttributeCategory();
        this.globalAttribute1 = slipDto.getGlobalAttribute1();
        this.globalAttribute2 = slipDto.getGlobalAttribute2();
        this.globalAttribute3 = slipDto.getGlobalAttribute3();
        this.globalAttribute4 = slipDto.getGlobalAttribute4();
        this.globalAttribute5 = slipDto.getGlobalAttribute5();
        this.globalAttribute6 = slipDto.getGlobalAttribute6();
        this.globalAttribute7 = slipDto.getGlobalAttribute7();
        this.globalAttribute8 = slipDto.getGlobalAttribute8();
        this.globalAttribute9 = slipDto.getGlobalAttribute9();
        this.globalAttribute10 = slipDto.getGlobalAttribute10();
        this.globalAttribute11 = slipDto.getGlobalAttribute11();
        this.globalAttribute12 = slipDto.getGlobalAttribute12();
        this.globalAttribute13 = slipDto.getGlobalAttribute13();
        this.globalAttribute14 = slipDto.getGlobalAttribute14();
        this.globalAttribute15 = slipDto.getGlobalAttribute15();
        this.headerAttribute1 = slipDto.getHeaderAttribute1();
        this.headerAttribute2 = slipDto.getHeaderAttribute2();
        this.headerAttribute3 = slipDto.getHeaderAttribute3();
        this.headerAttribute4 = slipDto.getHeaderAttribute4();
        this.headerAttribute5 = slipDto.getHeaderAttribute5();
        this.headerAttribute6 = slipDto.getHeaderAttribute6();
        this.headerAttribute7 = slipDto.getHeaderAttribute7();
        this.headerAttribute8 = slipDto.getHeaderAttribute8();
        this.headerAttribute9 = slipDto.getHeaderAttribute9();
        this.headerAttribute10 = slipDto.getHeaderAttribute10();
        this.lastUpdatedPersonId = slipDto.getPersonId();
        this.lastUpdatedEmpNo = slipDto.getEmpNo();
        this.lastUpdateDate = LocalDateTime.now();
    }

    public void reset() {
        this.invoiceTypeLookupCode = null;
        this.batchSourceName = null;
        this.categoryName = null;
        this.trxTypeCode = " ";
        this.ttypeInputModule = " ";
        this.ttypeInterfaceModule = " ";
        this.ttypeInterfaceSlipType = " ";
        this.ttypePrepaymentFlag = "N";
        this.ttypeClearingAcctCode = null;
        this.ttypeAddInfoType = null;
        this.ttypeIntegrationVendorNum = null;
        this.ttypePaymentReceiptTermId = null;
        this.ledgerId = 0;
        this.compCode = " ";
        this.budgetDeptCode = " ";
        this.projectCode = " ";
        this.projectId = new BigDecimal(0);
        this.taskCode = " ";
        this.taskId = " ";
        this.itemGroupCode = " ";
        this.codeCombinationId = null;
        this.drCr = " ";
        this.acctCode = " ";
        this.glDate = LocalDateTime.now();
        this.slipDate = LocalDateTime.now();
        this.taxEvidenceType = " ";
        this.slipCurrencyCode = " ";
        this.exchangeRateType = null;
        this.exchangeDate =LocalDateTime.now();
        this.exchangeRate = null;
        this.enteredAmount = null;
        this.accountedAmount = null;
        this.referenceSlipModule = null;
        this.referenceSlipNumber = null;
        this.description = null;
        this.integrationVendorNum = " ";
        this.vendorId = null;
        this.vendorSiteId = null;
        this.vendorPartyId = null;
        this.vendorPartySiteId = null;
        this.customerId = null;
        this.customerSiteId = null;
        this.customerPartyId = null;
        this.customerPartySiteId = null;
        this.termName = null;
        this.termId = null;
        this.termDueDate = null;
        this.prepaymentApplyFlag = "N";
        this.paymentReceiptMethodCode = null;
        this.payGroupLookupCode = null;
        this.noteFlag = "N";
        this.maturityDate = null;
        this.awtGroupId = null;
        this.awtLocationCode = null;
        this.taxLocationCode = null;
        this.taxIssueTypeCode = null;
        this.taxCode = null;
        this.vatTaxId = null;
        this.taxAcctCode = null;
        this.taxCodeCombinationId = null;
        this.evidenceVendorNum = null;
        this.evidenceVendorCustSiteId = null;
        this.evidenceDate = null;
        this.taxSmartbillNo = null;
        this.supplyAmount = null;
        this.taxAmount = null;
        this.actualDeptCode = " ";
        this.trBankAcctCode = " ";
        this.slipTypeCode = " ";
        this.segment9Code = " ";
        this.segment10Code = " ";
        this.bankAccountId = null;
        this.bankAccountName = null;
        this.sourceSlipHeaderId = null;
        this.poNumber = null;
        this.slipDisplayFlag = "Y";
        this.slipCreationTargetFlag = "Y";
        this.slipStatus = " ";
        this.validationFlag = "N";
        this.slipDataFixFlag = "N";
        this.approvalGroupId = new BigDecimal(0);
        this.approvalCompleteFlag = "N";
        this.createValidationFlag = "N";
        this.createValidationErrMsg = null;
        this.slipIfFlag = "N";
        this.stdInvoiceTrxId = null;
        this.slipIfDate = null;
        this.slipIfLastApprovalUser = null;
        this.slipInterfaceErrorMsg = null;
        this.postingFlag = "N";
        this.postingDate = null;
        this.dueDateUpdateFlag = "N";
        this.slipDeleteFlag = "N";
        this.slipDeleteDate = null;
        this.erpSlipCancelDate = null;
        this.erpSlipCancelUser = null;
        this.attributeCategory = null;
        this.attribute1 = null;
        this.attribute2 = null;
        this.attribute3 = null;
        this.attribute4 = null;
        this.attribute5 = null;
        this.attribute6 = null;
        this.attribute7 = null;
        this.attribute8 = null;
        this.attribute9 = null;
        this.attribute10 = null;
        this.attribute11 = null;
        this.attribute12 = null;
        this.attribute13 = null;
        this.attribute14 = null;
        this.attribute15 = null;
        this.globalAttributeCategory = null;
        this.globalAttribute1 = null;
        this.globalAttribute2 = null;
        this.globalAttribute3 = null;
        this.globalAttribute4 = null;
        this.globalAttribute5 = null;
        this.globalAttribute6 = null;
        this.globalAttribute7 = null;
        this.globalAttribute8 = null;
        this.globalAttribute9 = null;
        this.globalAttribute10 = null;
        this.globalAttribute11 = null;
        this.globalAttribute12 = null;
        this.globalAttribute13 = null;
        this.globalAttribute14 = null;
        this.globalAttribute15 = null;
        this.headerAttribute1 = null;
        this.headerAttribute2 = null;
        this.headerAttribute3 = null;
        this.headerAttribute4 = null;
        this.headerAttribute5 = null;
        this.headerAttribute6 = null;
        this.headerAttribute7 = null;
        this.headerAttribute8 = null;
        this.headerAttribute9 = null;
        this.headerAttribute10 = null;
        this.createdPersonId = 0;
        this.createdEmpNo = null;
        this.creationDate = LocalDateTime.now();
        this.lastUpdatedPersonId = 0;
        this.lastUpdatedEmpNo = null;
        this.lastUpdateDate = LocalDateTime.now();
        this.lastUpdateLogin = null;
        this.scanNo = null;
        this.vatAmtExceptionFlag = "N";
        this.slipForcedIfFlag = "N";
        this.globalAttribute16 = null;
    }

    public void updateApprovalFlag(String slipStatus, String validationFlag, String slipDataFixFlag) {
        this.slipStatus = slipStatus;
        this.validationFlag = validationFlag;
        this.slipDataFixFlag = slipDataFixFlag;
        this.lastUpdateDate = LocalDateTime.now();
    }

//    public void changeSlipDataFixFlag(String status) {
//        this.slipDataFixFlag = status;
//    }

//    public void setSlipIfFlagING(String status) {
//        this.slipIfFlag = status;
//    }

    public void setDeleteFlag() {
        this.slipStatus = "SD";
        this.slipDeleteFlag = "Y";
        this.taxSmartbillNo = null;
        this.globalAttribute14 = null;
        this.slipDeleteDate = LocalDateTime.now();
    }

    public void setApprovalCompleteFlagY() {
        this.approvalCompleteFlag = "Y";
    }

    public void updateTaxLocationCode(String taxLocationCode) {
        this.taxLocationCode = taxLocationCode;
    }

    public void updateStatus(String status) {
        this.slipStatus = status;
    }

    public static ErpSlipHeader copy(String slipNo, BigDecimal slipHeaderId, String slipGroupNo, ErpSlipHeader erpSlipHeader, Employee emp) {
        return ErpSlipHeader.builder()
            .slipHeaderId(slipHeaderId)
            .slipNumber(slipNo)
            .slipGroupNumber(slipGroupNo)
            .invoiceTypeLookupCode(erpSlipHeader.getInvoiceTypeLookupCode())
            .batchSourceName(erpSlipHeader.getBatchSourceName())
            .categoryName(erpSlipHeader.getCategoryName())
            .ttypeInterfaceModule(erpSlipHeader.getTtypeInterfaceModule())
            .ttypeInterfaceSlipType(erpSlipHeader.getTtypeInterfaceSlipType())
            .ttypePrepaymentFlag(erpSlipHeader.getTtypePrepaymentFlag())
            .ttypeClearingAcctCode(erpSlipHeader.getTtypeClearingAcctCode())
            .ttypeAddInfoType(erpSlipHeader.getTtypeAddInfoType())
            .ttypeIntegrationVendorNum(erpSlipHeader.getTtypeIntegrationVendorNum())
            .ttypePaymentReceiptTermId(erpSlipHeader.getTtypePaymentReceiptTermId())
            .ttypeInputModule(erpSlipHeader.getTtypeInputModule())
            .trxTypeCode(erpSlipHeader.getTrxTypeCode())
            .orgId(erpSlipHeader.getOrgId())
            .ledgerId(erpSlipHeader.getLedgerId())
            .compCode(erpSlipHeader.getCompCode())
            .budgetDeptCode(erpSlipHeader.getBudgetDeptCode())
            .projectCode(erpSlipHeader.getProjectCode())
            .projectId(erpSlipHeader.getProjectId())
            .taskCode(erpSlipHeader.getTaskCode())
            .taskId(erpSlipHeader.getTaskId())
            .itemGroupCode(erpSlipHeader.getItemGroupCode())
            .codeCombinationId(erpSlipHeader.getCodeCombinationId())
            .drCr(erpSlipHeader.getDrCr())
            .acctCode(erpSlipHeader.getAcctCode())
            .glDate(erpSlipHeader.getGlDate())
            .slipDate(erpSlipHeader.getSlipDate())
            .taxEvidenceType(erpSlipHeader.getTaxEvidenceType())
            .slipCurrencyCode(erpSlipHeader.getSlipCurrencyCode())
            .exchangeRateType(erpSlipHeader.getExchangeRateType())
            .exchangeDate(erpSlipHeader.getExchangeDate())
            .exchangeRate(erpSlipHeader.getExchangeRate())
            .enteredAmount(erpSlipHeader.getEnteredAmount())
            .accountedAmount(erpSlipHeader.getAccountedAmount())
            .referenceSlipModule(erpSlipHeader.getReferenceSlipModule())
            .referenceSlipNumber(erpSlipHeader.getReferenceSlipNumber())
            .description(erpSlipHeader.getDescription())
            .integrationVendorNum(erpSlipHeader.getIntegrationVendorNum())
            .vendorId(erpSlipHeader.getVendorId())
            .vendorSiteId(erpSlipHeader.getVendorSiteId())
            .vendorPartyId(erpSlipHeader.getVendorPartyId())
            .vendorPartySiteId(erpSlipHeader.getVendorPartySiteId())
            .vatAmtExceptionFlag(erpSlipHeader.getVatAmtExceptionFlag())
            .termName(erpSlipHeader.getTermName())
            .termId(erpSlipHeader.getTermId())
            .termDueDate(erpSlipHeader.getTermDueDate())
            .prepaymentApplyFlag(erpSlipHeader.getPrepaymentApplyFlag())
            .paymentReceiptMethodCode(erpSlipHeader.getPaymentReceiptMethodCode())
            .payGroupLookupCode(erpSlipHeader.getPayGroupLookupCode())
            .noteFlag(erpSlipHeader.getNoteFlag())
            .maturityDate(erpSlipHeader.getMaturityDate())
            .awtGroupId(erpSlipHeader.getAwtGroupId())
            .awtLocationCode(erpSlipHeader.getAwtLocationCode())
            .taxLocationCode(erpSlipHeader.getTaxLocationCode())
            .taxIssueTypeCode(erpSlipHeader.getTaxIssueTypeCode())
            .taxCode(erpSlipHeader.getTaxCode())
            .customerId(erpSlipHeader.getCustomerId())
            .customerSiteId(erpSlipHeader.getCustomerSiteId())
            .customerPartyId(erpSlipHeader.getCustomerPartyId())
            .customerPartySiteId(erpSlipHeader.getCustomerPartySiteId())
            .vatTaxId(erpSlipHeader.getVatTaxId())
            .taxAcctCode(erpSlipHeader.getTaxAcctCode())
            .taxCodeCombinationId(erpSlipHeader.getTaxCodeCombinationId())
            .evidenceVendorNum(erpSlipHeader.getEvidenceVendorNum())
            .evidenceVendorCustSiteId(erpSlipHeader.getEvidenceVendorCustSiteId())
            .evidenceDate(erpSlipHeader.getEvidenceDate())
            .taxSmartbillNo(erpSlipHeader.getTaxSmartbillNo())
            .supplyAmount(erpSlipHeader.getSupplyAmount())
            .taxAmount(erpSlipHeader.getTaxAmount())
            .actualDeptCode(erpSlipHeader.getActualDeptCode())
            .trBankAcctCode(erpSlipHeader.getTrBankAcctCode())
            .slipTypeCode(erpSlipHeader.getSlipTypeCode())
            .segment9Code(erpSlipHeader.getSegment9Code())
            .segment10Code(erpSlipHeader.getSegment10Code())
            .bankAccountId(erpSlipHeader.getBankAccountId())
            .bankAccountName(erpSlipHeader.getBankAccountName())
            .sourceSlipHeaderId(erpSlipHeader.getSourceSlipHeaderId())
            .poNumber(erpSlipHeader.getPoNumber())
            .slipDisplayFlag(erpSlipHeader.getSlipDisplayFlag())
            .slipCreationTargetFlag(erpSlipHeader.getSlipCreationTargetFlag())
            .slipStatus("SV")
            .validationFlag("N")
            .slipDataFixFlag("N")
            .approvalGroupId(slipHeaderId)
            .approvalCompleteFlag("N")
            .createValidationFlag("N")
            .createValidationErrMsg(erpSlipHeader.getCreateValidationErrMsg())
            .slipIfFlag("N")
            .stdInvoiceTrxId(null)
            .slipIfDate(null)
            .slipIfLastApprovalUser(erpSlipHeader.getSlipIfLastApprovalUser())
            .postingFlag("N")
            .drCr(erpSlipHeader.getDrCr())
            .postingDate(null)
            .dueDateUpdateFlag("N")
            .slipDeleteFlag("N")
            .slipForcedIfFlag("N")
            .slipInterfaceErrorMsg(null)
            .dueDateUpdateFlag(erpSlipHeader.getDueDateUpdateFlag())
            .attributeCategory(erpSlipHeader.getAttributeCategory())
            .attribute1(erpSlipHeader.getAttribute1())
            .attribute2(erpSlipHeader.getAttribute2())
            .attribute3(erpSlipHeader.getAttribute3())
            .attribute4(erpSlipHeader.getAttribute4())
            .attribute5(erpSlipHeader.getAttribute5())
            .attribute6(erpSlipHeader.getAttribute6())
            .attribute7(erpSlipHeader.getAttribute7())
            .attribute8(erpSlipHeader.getAttribute8())
            .attribute9(erpSlipHeader.getAttribute9())
            .attribute10(erpSlipHeader.getAttribute10())
            .attribute11(erpSlipHeader.getAttribute11())
            .attribute12(erpSlipHeader.getAttribute12())
            .attribute13(erpSlipHeader.getAttribute13())
            .attribute14(erpSlipHeader.getAttribute14())
            .attribute15(erpSlipHeader.getAttribute15())
            .globalAttributeCategory(erpSlipHeader.getGlobalAttributeCategory())
            .globalAttribute1(erpSlipHeader.getGlobalAttribute1())
            .globalAttribute2(erpSlipHeader.getGlobalAttribute2())
            .globalAttribute3(erpSlipHeader.getGlobalAttribute3())
            .globalAttribute4(erpSlipHeader.getGlobalAttribute4())
            .globalAttribute5(erpSlipHeader.getGlobalAttribute5())
            .globalAttribute6(erpSlipHeader.getGlobalAttribute6())
            .globalAttribute7(erpSlipHeader.getGlobalAttribute7())
            .globalAttribute8(erpSlipHeader.getGlobalAttribute8())
            .globalAttribute9(erpSlipHeader.getGlobalAttribute9())
            .globalAttribute10(erpSlipHeader.getGlobalAttribute10())
            .globalAttribute11(erpSlipHeader.getGlobalAttribute11())
            .globalAttribute12(erpSlipHeader.getGlobalAttribute12())
            .globalAttribute13(erpSlipHeader.getGlobalAttribute13())
            .globalAttribute14(erpSlipHeader.getGlobalAttribute14())
            .globalAttribute15(erpSlipHeader.getGlobalAttribute15())
            .headerAttribute1(erpSlipHeader.getHeaderAttribute1())
            .headerAttribute2(erpSlipHeader.getHeaderAttribute2())
            .headerAttribute3(erpSlipHeader.getHeaderAttribute3())
            .headerAttribute4(erpSlipHeader.getHeaderAttribute4())
            .headerAttribute5(erpSlipHeader.getHeaderAttribute5())
            .headerAttribute6(erpSlipHeader.getHeaderAttribute6())
            .headerAttribute7(erpSlipHeader.getHeaderAttribute7())
            .headerAttribute8(erpSlipHeader.getHeaderAttribute8())
            .headerAttribute9(erpSlipHeader.getHeaderAttribute9())
            .headerAttribute10(erpSlipHeader.getHeaderAttribute10())
            .createdPersonId(erpSlipHeader.getCreatedPersonId())
            .createdEmpNo(erpSlipHeader.getCreatedEmpNo())
            .creationDate(LocalDateTime.now())
            .lastUpdatedPersonId(erpSlipHeader.getLastUpdatedPersonId())
            .lastUpdatedEmpNo(erpSlipHeader.getLastUpdatedEmpNo())
            .lastUpdateDate(LocalDateTime.now())
            .scanNo(erpSlipHeader.getScanNo())
            .build();
    }

    private static LocalDateTime parseDate(LocalDateTime date) {
        if(nonNull(date)) {
            return LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 0, 0, 0);
        }
        return null;
    }

    public void changeTaxSmartbillNoAndGlobalAttribute14(String taxSmartbillNo, String globalAttribute14){
        this.taxSmartbillNo = taxSmartbillNo;
        this.globalAttribute14 = globalAttribute14;
    }
}
