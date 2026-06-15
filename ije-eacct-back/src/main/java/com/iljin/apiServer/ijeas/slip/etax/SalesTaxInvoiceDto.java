package com.iljin.apiServer.ijeas.slip.etax;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SalesTaxInvoiceDto implements Serializable {
    private static final long serialVersionUID = 5729842227739508470L;
    BigDecimal customerTrxId;
    String completeFlag;
    String trxNumber;
    String groupNumber;
    LocalDateTime trxDate;
    String glDate;
    BigDecimal orgId;
    BigDecimal customerId;
    String customerName;
    String integrationVendorNum;
    String taxReference;
    String representativeName;
    String custBusinessCondition;
    String custBusinessType;
    String corporationNumber;
    String customerAddress;
    String postalCode;
    String phoneNumber;
    String faxNumber;
    String eMail;
    String comments;
    String currencyCode;
    String taxEvidenceType;
    String taxEvidenceTypeName;
    String trxType;
    String supbuyType;
    String etaxIssueIdYn;
    String etaxIssueId;
    BigDecimal etaxIssueIdCnt;
    String dtiStatus;
    String dtiStatusText;
    String taxCode;
    String dtiType;
    BigDecimal totalAmount;
    BigDecimal supplyAmount;
    BigDecimal taxAmount;
    String createDeptCode;
    String createDeptName;
    String createEmpNo;
    String createEmpName;
    String etaxExcludeFlag;
    String taxLocation;
    String direction;

    public SalesTaxInvoiceDto(BigDecimal customerTrxId, String completeFlag, String trxNumber, String groupNumber, Timestamp trxDate, String glDate, BigDecimal orgId,
                              BigDecimal customerId, String customerName, String integrationVendorNum, String taxReference, String representativeName,
                              String custBusinessCondition, String custBusinessType, String corporationNumber, String customerAddress, String postalCode, String phoneNumber,
                              String faxNumber, String eMail, String comments, String currencyCode, String taxEvidenceType, String taxEvidenceTypeName, String trxType,
                              String supbuyType, Character etaxIssueIdYn, String etaxIssueId, BigDecimal etaxIssueIdCnt, Character dtiStatus, String dtiStatusText,
                              String taxCode, String dtiType, BigDecimal totalAmount, BigDecimal supplyAmount, BigDecimal taxAmount, String createDeptCode,
                              String createDeptName, String createEmpNo, String createEmpName, String etaxExcludeFlag, String taxLocation, String direction) {
        this.customerTrxId = customerTrxId;
        this.completeFlag = completeFlag;
        this.trxNumber = trxNumber;
        this.groupNumber = groupNumber;
        this.trxDate = trxDate == null ? null : trxDate.toLocalDateTime();
        this.glDate = glDate;
        this.orgId = orgId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.integrationVendorNum = integrationVendorNum;
        this.taxReference = taxReference;
        this.representativeName = representativeName;
        this.custBusinessCondition = custBusinessCondition;
        this.custBusinessType = custBusinessType;
        this.corporationNumber = corporationNumber;
        this.customerAddress = customerAddress;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.faxNumber = faxNumber;
        this.eMail = eMail;
        this.comments = comments;
        this.currencyCode = currencyCode;
        this.taxEvidenceType = taxEvidenceType;
        this.taxEvidenceTypeName = taxEvidenceTypeName;
        this.trxType = trxType;
        this.supbuyType = supbuyType;
        this.etaxIssueIdYn = etaxIssueIdYn == null ? null : String.valueOf(etaxIssueIdYn);
        this.etaxIssueId = etaxIssueId;
        this.etaxIssueIdCnt = etaxIssueIdCnt;
        this.dtiStatus = dtiStatus == null ? null : String.valueOf(dtiStatus);
        this.dtiStatusText = dtiStatusText;
        this.taxCode = taxCode;
        this.dtiType = dtiType;
        this.totalAmount = totalAmount;
        this.supplyAmount = supplyAmount;
        this.taxAmount = taxAmount;
        this.createDeptCode = createDeptCode;
        this.createDeptName = createDeptName;
        this.createEmpNo = createEmpNo;
        this.createEmpName = createEmpName;
        this.etaxExcludeFlag = etaxExcludeFlag;
        this.taxLocation = taxLocation;
        this.direction = direction;
    }

    String fromDate;
    String toDate;

    public SalesTaxInvoiceDto(BigDecimal orgId, String fromDate, String toDate){
        this.orgId = orgId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    BigDecimal lineNumber;
    String description;
    String taxClassificationCode;
    //BigDecimal supplyAmount;
    //BigDecimal taxAmount;
    BigDecimal quantityInvoiced;
    BigDecimal unitSellingPrice;
    String uomCode;
    //BigDecimal customerTrxId;
    BigDecimal customerTrxLineId;

    public SalesTaxInvoiceDto(BigDecimal lineNumber, String description, String taxClassificationCode, BigDecimal supplyAmount, BigDecimal taxAmount,
                              BigDecimal quantityInvoiced, BigDecimal unitSellingPrice, String uomCode, BigDecimal customerTrxId, BigDecimal customerTrxLineId) {
        this.customerTrxId = customerTrxId;
        this.supplyAmount = supplyAmount;
        this.taxAmount = taxAmount;
        this.lineNumber = lineNumber;
        this.description = description;
        this.taxClassificationCode = taxClassificationCode;
        this.quantityInvoiced = quantityInvoiced;
        this.unitSellingPrice = unitSellingPrice;
        this.uomCode = uomCode;
        this.customerTrxLineId = customerTrxLineId;
    }

    String completeYn;
    public SalesTaxInvoiceDto(String completeYn){
        this.completeYn = completeYn;
    }

    BigDecimal newEtaxIssueId;
    public SalesTaxInvoiceDto(BigDecimal newEtaxIssueId){
        this.newEtaxIssueId = newEtaxIssueId;
    }

    String createdPersonId;
    String deptCd;
    String modifySupplyAmount;
    String modifyTaxAmount;

    public SalesTaxInvoiceDto(BigDecimal customerTrxId, String createdPersonId, String taxEvidenceType, String deptCd, String modifySupplyAmount, String modifyTaxAmount){
        this.customerTrxId = customerTrxId;
        this.createdPersonId = createdPersonId;
        this.taxEvidenceType = taxEvidenceType;
        this.deptCd = deptCd;
        this.modifySupplyAmount = modifySupplyAmount;
        this.modifyTaxAmount = modifyTaxAmount;
    }


    //매출세금계산서 발행

    String dtiWdate;
    //String glDate
    String byrComRegno;
    //String customerName;
    String conversationId;
    //Character dtiStatus;
    //String dtiStatusText;
    String byrEmail;
    //String taxCode;
    //BigDecimal supplyAmount;
    //BigDecimal taxAmount;
    //BigDecimal totalAmount;
    String remark;
    //BigDecimal orgId;
    //String etaxIssueId;
    String byrTelNum;
    //BigDecimal customerId;
    //String integrationVendorNum;
    String byrRepName;
    String byrComType;
    String byrComClassify;
    //String corporationNumber;
    String byrComAddr;
    //String currencyCode;
    //String supbuyType;
    String supbuyTypeText;
    //String direction;
    BigDecimal interfaceBatchId;
    String taxDemand;
    String supEmpName;
    String supEmail;
    String supTelNum;
    String supComName;
    String supComRegno;
    String supRepName;
    String supComClassify;
    String supComType;
    String supComAddr;
    String dtiStatusMeaning;
    String approveId;
    //String dtiType;
    String dtiTypeText;
    String itemName;
    String returnDescription;
    String returnCode;
    String amendCode;
    String oriIssueId;
    BigDecimal oriEtaxIssueId;
    String oriDtiWdate;
    String localLcOpenDate;
    //String createDeptCode;
    //String createDeptName;
    //String createEmpNo;
    //String createEmpName;
    String supBizplaceCode;
    String byrBizplaceCode;
    String sendRequest;

    public SalesTaxInvoiceDto(String dtiWdate, String glDate, String byrComRegno, String customerName, String conversationId, Character dtiStatus, String dtiStatusText,
                              String byrEmail, String taxCode, BigDecimal supplyAmount, BigDecimal taxAmount, BigDecimal totalAmount, String remark, BigDecimal orgId,
                              String etaxIssueId, String byrTelNum, BigDecimal customerId, String integrationVendorNum, String byrRepName, String byrComType,
                              String byrComClassify, String corporationNumber, String byrComAddr, String currencyCode, String supbuyType, String supbuyTypeText,
                              String direction, BigDecimal interfaceBatchId, String taxDemand, String supEmpName, String supEmail, String supTelNum, String supComName,
                              String supComRegno, String supRepName, String supComClassify, String supComType, String supComAddr, String dtiStatusMeaning, String approveId,
                              String dtiType, String dtiTypeText, String itemName, String returnDescription, String returnCode, String amendCode, String oriIssueId,
                              BigDecimal oriEtaxIssueId, String oriDtiWdate, String localLcOpenDate, String createDeptCode, String createDeptName, String createEmpNo,
                              String createEmpName, String supBizplaceCode, String byrBizplaceCode, String sendRequest){
        this.dtiWdate = dtiWdate;
        this.glDate = glDate;
        this.byrComRegno = byrComRegno;
        this.customerName = customerName;
        this.conversationId = conversationId;
        this.dtiStatus = dtiStatus == null ? null : String.valueOf(dtiStatus);
        this.dtiStatusText = dtiStatusText;
        this.byrEmail = byrEmail;
        this.taxCode = taxCode;
        this.supplyAmount = supplyAmount;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.remark = remark;
        this.orgId = orgId;
        this.etaxIssueId = etaxIssueId;
        this.byrTelNum = byrTelNum;
        this.customerId = customerId;
        this.integrationVendorNum = integrationVendorNum;
        this.byrRepName = byrRepName;
        this.byrComType = byrComType;
        this.byrComClassify = byrComClassify;
        this.corporationNumber = corporationNumber;
        this.byrComAddr = byrComAddr;
        this.currencyCode = currencyCode;
        this.supbuyType = supbuyType;
        this.supbuyTypeText = supbuyTypeText;
        this.direction = direction;
        this.interfaceBatchId = interfaceBatchId;
        this.taxDemand = taxDemand;
        this.supEmpName = supEmpName;
        this.supEmail = supEmail;
        this.supTelNum = supTelNum;
        this.supComName = supComName;
        this.supComRegno = supComRegno;
        this.supRepName = supRepName;
        this.supComClassify = supComClassify;
        this.supComType = supComType;
        this.supComAddr = supComAddr;
        this.dtiStatusMeaning = dtiStatusMeaning;
        this.approveId = approveId;
        this.dtiType = dtiType;
        this.dtiTypeText = dtiTypeText;
        this.itemName = itemName;
        this.returnDescription = returnDescription;
        this.returnCode = returnCode;
        this.amendCode = amendCode;
        this.oriIssueId = oriIssueId;
        this.oriEtaxIssueId = oriEtaxIssueId;
        this.oriDtiWdate = oriDtiWdate;
        this.localLcOpenDate = localLcOpenDate;
        this.createDeptCode = createDeptCode;
        this.createDeptName = createDeptName;
        this.createEmpNo = createEmpNo;
        this.createEmpName = createEmpName;
        this.supBizplaceCode = supBizplaceCode;
        this.byrBizplaceCode = byrBizplaceCode;
        this.sendRequest = sendRequest;

    }

    //매출세금계산서 아이템
    //String etaxIssueId;
    //String conversationId;
    BigDecimal dtiLineNum;
    //String itemName;
    BigDecimal itemQty;
    String itemMd;
    BigDecimal unitPrice;
    //BigDecimal supplyAmount;
    //BigDecimal taxAmount;

    public SalesTaxInvoiceDto(String etaxIssueId, String conversationId, BigDecimal dtiLineNum, String itemName, BigDecimal itemQty, String itemMd,
                              BigDecimal unitPrice, BigDecimal supplyAmount, BigDecimal taxAmount){
        this.etaxIssueId = etaxIssueId;
        this.conversationId = conversationId;
        this.dtiLineNum = dtiLineNum;
        this.itemName = itemName;
        this.itemQty = itemQty;
        this.itemMd = itemMd;
        this.unitPrice = unitPrice;
        this.supplyAmount = supplyAmount;
        this.taxAmount = taxAmount;
    }

    //String trxNumber;
    public SalesTaxInvoiceDto(String trxNumber, String temp){
        this.trxNumber = trxNumber;
    }

    String vatRegistrationNum;
    String vatRegistrationNumNon;
    //String representativeName;
    //String custBusinessCondition;
    //String custBusinessType;
    String corporationName;
    String address;

    public SalesTaxInvoiceDto(String vatRegistrationNum, String vatRegistrationNumNon, String representativeName, String custBusinessCondition,
                              String custBusinessType, String corporationName, String address){
        this.vatRegistrationNum = vatRegistrationNum;
        this.vatRegistrationNumNon = vatRegistrationNumNon;
        this.representativeName = representativeName;
        this.custBusinessCondition = custBusinessCondition;
        this.custBusinessType = custBusinessType;
        this.corporationName = corporationName;
        this.address = address;
    }

    BigDecimal newInterfaceBatchId;
    public SalesTaxInvoiceDto(BigDecimal newInterfaceBatchId, String temp){
        this.newInterfaceBatchId = newInterfaceBatchId;
    }

    public SalesTaxInvoiceDto(BigDecimal etaxIssueId, BigDecimal customerTrxId){
        this.etaxIssueId = etaxIssueId.toString();
        this.customerTrxId = customerTrxId;
    }

    public SalesTaxInvoiceDto(String trxNumber, BigDecimal customerTrxId){
        this.trxNumber = trxNumber;
        this.customerTrxId = customerTrxId;
    }

    BigDecimal count;

    public SalesTaxInvoiceDto(BigDecimal count, String oriIssueId, String amendCode, String temp){
        this.count = count;
        this.oriIssueId = oriIssueId;
        this.amendCode = amendCode;
    }

    public SalesTaxInvoiceDto(String etaxIssueId, BigDecimal interfaceBatchId, String oriIssueId, BigDecimal totalAmount, String taxCode){
        this.etaxIssueId = etaxIssueId;
        this.interfaceBatchId = interfaceBatchId;
        this.oriIssueId = oriIssueId;
        this.totalAmount = totalAmount;
        this.taxCode = taxCode;
    }

    String postDtFrom;
    String postDtTo;
    String issueDtFrom;
    String issueDtTo;
    String wrtDeptCd;
    String wrtId;
    String empNo;

    public SalesTaxInvoiceDto(BigDecimal orgId, String postDtFrom, String postDtTo, String integrationVendorNum, Character dtiStatus, String issueDtFrom,
                              String issueDtTo, String wrtDeptCd, String wrtId, String empNo){
        this.orgId = orgId;
        this.postDtFrom = postDtFrom;
        this.postDtTo = postDtTo;
        this.integrationVendorNum = integrationVendorNum;
        this.dtiStatus = dtiStatus == null ? null : String.valueOf(dtiStatus);
        this.issueDtFrom = issueDtFrom;
        this.issueDtTo = issueDtTo;
        this.wrtDeptCd = wrtDeptCd;
        this.wrtId = wrtId;
        this.empNo = empNo;
    }


    public SalesTaxInvoiceDto(String conversationId, Character dtiStatus, String returnCode){
        this.conversationId = conversationId;
        this.dtiStatus = dtiStatus == null ? null : String.valueOf(dtiStatus);
        this.returnCode = returnCode;

    }
}
