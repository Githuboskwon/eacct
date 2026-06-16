package com.iljin.apiServer.ijeas.slip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@NoArgsConstructor
@Data
@AllArgsConstructor
public class SlipDetailDto implements Serializable {
    private static final long serialVersionUID = 5034432638205270763L;
    //tb_slip_dt
    String compCd;
    String slipNo;
    String slipSeq;
    String bungaeNo;
    String slipCd;
    String usedDt;
    BigDecimal slipHeaderId;
    BigDecimal slipLineId;
    String glAcctCd;
    String taxFlag;
    String usedCur;
    String fAmt;
    String tAmt;
    String surtax;
    String originAmt;
    String originalTaxFlag;
    String originalSupplyAmt;
    String originalVatAmt;
    String originalUsedAmt;
    String originalTaxAcctCd;
    String usedNo;
    String projectNo;
    String drcrType;
    String remark;

    //cbo_sp_slip_line
    //BigDecimal slipHeaderId;
    //Integer slipLineId;
    BigDecimal slipLineNumber;
    String lineTypeLookupCode;
    String orgId;
    BigDecimal ledgerId;
    String ttypeInterfaceModule;
    String source;
    String drCr;
    String drCrName;
    String compCode;
    String compName;
    String budgetDeptCode;
    String budgetDeptName;
    String projectCode;
    BigDecimal projectId;
    String projectName;
    String taskCode;
    BigDecimal taskId;
    String itemGroupCode;
    String itemGroupName;
    BigDecimal codeCombinationId;
    String coaFullCode;
    String coaFullDesc;
    String acctCode;
    String acctName;
    String interfaceSlipType;
    String actualDeptCode;
    String actualDeptName;
    String trAcctCode;
    String trAcctName;
    String slipTypeCode;
    String slipTypeName;
    String segment9Code;
    String segment9Name;
    String segment10Code;
    String segment10Name;
    BigDecimal supplyAmount;
    BigDecimal accountedSupplyAmount;
    BigDecimal vatAmount;
    BigDecimal accountedVatAmount;
    String taxCode;
    BigDecimal taxId;
    String taxAcctCode;
    String taxAcctName;
    BigDecimal taxCodeCombinationId;
    String taxCoaFullCode;
    String taxCoaFullDesc;
    String description;
    String cardUsedNo;
    BigDecimal quantityInvoiced;
    BigDecimal unitPrice;
    String assetsTrackingFlag;
    BigDecimal poHeaderId;
    BigDecimal poLineId;
    BigDecimal poLineLocationId;
    BigDecimal poDistributionId;
    BigDecimal rcvTransactionId;
    String poUnitOfMeasure;
    BigDecimal inventoryItemId;
    BigDecimal prepayInvoiceId;
    BigDecimal prepayLineNumber;
    String invoiceIncludesPrepayFlag;
    String validationFlag;
    String slipInterfaceFlag;
    String slipInterfaceErrorMsg;
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
    String globalAttribute16;
    String globalAttribute17;
    String globalAttribute18;
    String globalAttribute19;
    String globalAttribute20;
    String lineAttribute1;
    String lineAttribute2;
    String lineAttribute3;
    String lineAttribute4;
    String lineAttribute5;
    String lineAttribute6;
    String lineAttribute7;
    String lineAttribute8;
    String lineAttribute9;
    String lineAttribute10;
    BigDecimal createdPersonId;
    String creationDate;
    BigDecimal lastUpdatedPersonId;
    String lastUpdateDate;
    BigDecimal lastUpdateLogin;
    String receiptNumber;
    String receiptLineNumber;
    BigDecimal poReleaseId;
    Integer organizationId;
    String itemCode;
    BigDecimal lineGroupNumber;
    BigDecimal awtGroupId;

    String fromArea;

    String fromAreaText;

    String toArea;

    String toAreaText;

    String trafficType;

    String distance;

    String oilPrice;

    String mileage;

    String oilAmt;

    String oilBungae;

    String etcType1;

    String etcAmt1;

    String etcBungae1;

    String etcChitCd1;

    String etcUsedNo1;

    String etcType2;

    String etcAmt2;

    String etcBungae2;

    String etcChitCd2;

    String etcUsedNo2;

    String etcType3;

    String etcAmt3;

    String etcBungae3;

    String etcChitCd3;

    String etcUsedNo3;

    String sumAmt;

    String oilAmtType;

    BigDecimal confDist;

    LocalDateTime paymentDt;

    String bigo;

    String distanceGubun;

//    SlipTrafficDtDto slipTrafficDtDto;

    //tb_slip_dt
    public SlipDetailDto(String compCd, String slipNo, String slipSeq, String bungaeNo, String slipCd, String usedDt, BigDecimal slipHeaderId, BigDecimal slipLineId,
                         String glAcctCd, String taxFlag, String usedCur, String fAmt, String tAmt, String surtax, String originAmt, String originalTaxFlag,
                         String originalSupplyAmt, String originalVatAmt, String originalUsedAmt, String originalTaxAcctCd, String usedNo, String projectNo,
                         String drcrType, String remark){
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipSeq = slipSeq;
        this.bungaeNo = bungaeNo;
        this.slipCd = slipCd;
        this.usedDt = usedDt;
        this.slipHeaderId = slipHeaderId;
        this.slipLineId = slipLineId;
        this.glAcctCd = glAcctCd;
        this.taxFlag = taxFlag;
        this.usedCur = usedCur;
        this.fAmt = fAmt;
        this.tAmt = tAmt;
        this.surtax = surtax;
        this.originAmt = originAmt;
        this.originalTaxFlag = originalTaxFlag;
        this.originalSupplyAmt = originalSupplyAmt;
        this.originalVatAmt = originalVatAmt;
        this.originalUsedAmt = originalUsedAmt;
        this.originalTaxAcctCd = originalTaxAcctCd;
        this.usedNo = usedNo;
        this.projectNo = projectNo;
        this.drcrType = drcrType;
        this.remark = remark;
    }

    //cbo_sp_slip_line
    public SlipDetailDto(BigDecimal slipHeaderId, BigDecimal slipLineId, BigDecimal slipLineNumber, String lineTypeLookupCode, String orgId, BigDecimal ledgerId,
                         String ttypeInterfaceModule, String source, String drCr, String drCrName, String compCode, String compName, String budgetDeptCode,
                         String budgetDeptName, String projectCode, BigDecimal projectId, String projectName, String taskCode, BigDecimal taskId, String itemGroupCode,
                         String itemGroupName, BigDecimal codeCombinationId, String coaFullCode, String coaFullDesc, String acctCode, String acctName,
                         String interfaceSlipType, String actualDeptCode, String actualDeptName, String trAcctCode, String trAcctName, String slipTypeCode,
                         String slipTypeName, String segment9Code, String segment9Name, String segment10Code, String segment10Name, BigDecimal supplyAmount,
                         BigDecimal accountedSupplyAmount, BigDecimal vatAmount, BigDecimal accountedVatAmount, String taxCode, BigDecimal taxId, String taxAcctCode,
                         String taxAcctName, BigDecimal taxCodeCombinationId, String taxCoaFullCode, String taxCoaFullDesc, String description, String cardUsedNo,
                         BigDecimal quantityInvoiced, BigDecimal unitPrice, String assetsTrackingFlag, BigDecimal poHeaderId, BigDecimal poLineId, BigDecimal poLineLocationId,
                         BigDecimal poDistributionId, BigDecimal rcvTransactionId, String poUnitOfMeasure, BigDecimal inventoryItemId, BigDecimal prepayInvoiceId,
                         BigDecimal prepayLineNumber, String invoiceIncludesPrepayFlag, String validationFlag, String slipInterfaceFlag, String slipInterfaceErrorMsg,
                         String attributeCategory, String attribute1, String attribute2, String attribute3, String attribute4, String attribute5, String attribute6,
                         String attribute7, String attribute8, String attribute9, String attribute10, String attribute11, String attribute12, String attribute13,
                         String attribute14, String attribute15, String globalAttributeCategory, String globalAttribute1, String globalAttribute2, String globalAttribute3,
                         String globalAttribute4, String globalAttribute5, String globalAttribute6, String globalAttribute7, String globalAttribute8,
                         String globalAttribute9, String globalAttribute10, String globalAttribute11, String globalAttribute12, String globalAttribute13,
                         String globalAttribute14, String globalAttribute15, String globalAttribute16, String globalAttribute17, String globalAttribute18,
                         String globalAttribute19, String globalAttribute20, String lineAttribute1, String lineAttribute2, String lineAttribute3, String lineAttribute4,
                         String lineAttribute5, String lineAttribute6, String lineAttribute7, String lineAttribute8, String lineAttribute9, String lineAttribute10,
                         BigDecimal createdPersonId, String creationDate, BigDecimal lastUpdatedPersonId, String lastUpdateDate, BigDecimal lastUpdateLogin){
        this.slipHeaderId = slipHeaderId;
        this.slipLineId = slipLineId;
        this.slipLineNumber = slipLineNumber;
        this.lineTypeLookupCode = lineTypeLookupCode;
        this.orgId = orgId;
        this.ledgerId = ledgerId;
        this.ttypeInterfaceModule = ttypeInterfaceModule;
        this.source = source;
        this.drCr = drCr;
        this.drCrName = drCrName;
        this.compCode = compCode;
        this.compName = compName;
        this.budgetDeptCode = budgetDeptCode;
        this.budgetDeptName = budgetDeptName;
        this.projectCode = projectCode;
        this.projectId = projectId;
        this.projectName = projectName;
        this.taskCode = taskCode;
        this.taskId = taskId;
        this.itemGroupCode = itemGroupCode;
        this.itemGroupName = itemGroupName;
        this.codeCombinationId = codeCombinationId;
        this.coaFullCode = coaFullCode;
        this.coaFullDesc = coaFullDesc;
        this.acctCode = acctCode;
        this.acctName = acctName;
        this.interfaceSlipType = interfaceSlipType;
        this.actualDeptCode = actualDeptCode;
        this.actualDeptName = actualDeptName;
        this.trAcctCode = trAcctCode;
        this.trAcctName = trAcctName;
        this.slipTypeCode = slipTypeCode;
        this.slipTypeName = slipTypeName;
        this.segment9Code = segment9Code;
        this.segment9Name = segment9Name;
        this.segment10Code = segment10Code;
        this.segment10Name = segment10Name;
        this.supplyAmount = supplyAmount;
        this.accountedSupplyAmount = accountedSupplyAmount;
        this.vatAmount = vatAmount;
        this.accountedVatAmount = accountedVatAmount;
        this.taxCode = taxCode;
        this.taxId = taxId;
        this.taxAcctCode = taxAcctCode;
        this.taxAcctName = taxAcctName;
        this.taxCodeCombinationId = taxCodeCombinationId;
        this.taxCoaFullCode = taxCoaFullCode;
        this.taxCoaFullDesc = taxCoaFullDesc;
        this.description = description;
        this.cardUsedNo = cardUsedNo;
        this.quantityInvoiced = quantityInvoiced;
        this.unitPrice = unitPrice;
        this.assetsTrackingFlag = assetsTrackingFlag;
        this.poHeaderId = poHeaderId;
        this.poLineId = poLineId;
        this.poLineLocationId = poLineLocationId;
        this.poDistributionId = poDistributionId;
        this.rcvTransactionId = rcvTransactionId;
        this.poUnitOfMeasure = poUnitOfMeasure;
        this.inventoryItemId = inventoryItemId;
        this.prepayInvoiceId = prepayInvoiceId;
        this.prepayLineNumber = prepayLineNumber;
        this.invoiceIncludesPrepayFlag = invoiceIncludesPrepayFlag;
        this.validationFlag = validationFlag;
        this.slipInterfaceFlag = slipInterfaceFlag;
        this.slipInterfaceErrorMsg = slipInterfaceErrorMsg;
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
        this.globalAttribute16 = globalAttribute16;
        this.globalAttribute17 = globalAttribute17;
        this.globalAttribute18 = globalAttribute18;
        this.globalAttribute19 = globalAttribute19;
        this.globalAttribute20 = globalAttribute20;
        this.lineAttribute1 = lineAttribute1;
        this.lineAttribute2 = lineAttribute2;
        this.lineAttribute3 = lineAttribute3;
        this.lineAttribute4 = lineAttribute4;
        this.lineAttribute5 = lineAttribute5;
        this.lineAttribute6 = lineAttribute6;
        this.lineAttribute7 = lineAttribute7;
        this.lineAttribute8 = lineAttribute8;
        this.lineAttribute9 = lineAttribute9;
        this.lineAttribute10 = lineAttribute10;
        this.createdPersonId = createdPersonId;
        this.creationDate = creationDate;
        this.lastUpdatedPersonId = lastUpdatedPersonId;
        this.lastUpdateDate = lastUpdateDate;
        this.lastUpdateLogin = lastUpdateLogin;
    }


    String taskName;
    String taskItemGroup;
    String itemGroupType;
    String actualDeptType;
    BigDecimal taxPercentageRate;
    BigDecimal usedAmt;
    BigDecimal dffCnt;
    BigDecimal requiredFlagCnt;
    String attributeCategoryName;
    String c_type;
    String subTrxType;
    String subTrxTypeName;
    String oAmtType;
    String iAmtType;
    String createdEmpNo;
    String lastUpdatedEmpNo;
    String withholdingTaxCode;
    String attribute1Code;
    String attribute2Code;
    String attribute3Code;
    String attribute4Code;
    String attribute5Code;
    String attribute6Code;
    String poHeaderNum;
    BigDecimal poLineNum;

    //전표 디테일 조회
    public SlipDetailDto(BigDecimal slipHeaderId, BigDecimal slipLineId, BigDecimal slipLineNumber, String slipSeq, String lineTypeLookupCode, String orgId, BigDecimal ledgerId,
                         String ttypeInterfaceModule, String source, String compCode, String acctName, String actualDeptName, String compName, String budgetDeptCode,
                         String budgetDeptName, String projectCode, String projectName, BigDecimal projectId, String taskCode, String taskName, BigDecimal taskId,
                         String taskItemGroup, String itemGroupType, String itemGroupCode, String itemGroupName, BigDecimal codeCombinationId, String acctCode,
                         String interfaceSlipType, String actualDeptCode, String actualDeptType, String trAcctCode, String trAcctName, String slipTypeCode,
                         String segment9Code, String segment9Name, String segment10Code, String segment10Name, BigDecimal taxPercentageRate, BigDecimal supplyAmount,
                         BigDecimal accountedSupplyAmount, BigDecimal vatAmount, BigDecimal accountedVatAmount, BigDecimal usedAmt, String taxCode, BigDecimal taxId,
                         String taxAcctCode, String taxAcctName, BigDecimal taxCodeCombinationId, String description, String cardUsedNo, BigDecimal quantityInvoiced,
                         BigDecimal unitPrice, String assetsTrackingFlag, BigDecimal poHeaderId, String poHeaderNum, BigDecimal poLineId, BigDecimal poLineNum,
                         BigDecimal poLineLocationId, BigDecimal poDistributionId, BigDecimal rcvTransactionId, String poUnitOfMeasure, BigDecimal inventoryItemId, String itemCode,
                         BigDecimal prepayInvoiceId, BigDecimal prepayLineNumber, String invoiceIncludesPrepayFlag, String validationFlag, String slipInterfaceFlag,
                         String slipInterfaceErrorMsg, BigDecimal dffCnt, BigDecimal requiredFlagCnt, String attributeCategory, String attributeCategoryName, String attribute1,
                         String attribute2, String attribute3, String attribute4, String attribute5, String attribute6, String attribute7, String attribute8, String attribute9,
                         String attribute10, String attribute11, String attribute12, String attribute13, String attribute14, String attribute15, String c_type, String subTrxType,
                         String subTrxTypeName, String oAmtType, String iAmtType, String globalAttributeCategory, String globalAttribute1, String globalAttribute2,
                         String globalAttribute3, String globalAttribute4, String globalAttribute5, String globalAttribute6, String globalAttribute7, String globalAttribute8,
                         String globalAttribute9, String globalAttribute10, String globalAttribute11, String globalAttribute12, String globalAttribute13,
                         String globalAttribute14, String globalAttribute15, String globalAttribute16, String globalAttribute17, String globalAttribute18,
                         String globalAttribute19, String globalAttribute20, String lineAttribute1, String lineAttribute2, String lineAttribute3, String lineAttribute4,
                         String lineAttribute5, String lineAttribute6, String lineAttribute7, String lineAttribute8, String lineAttribute9, String lineAttribute10,
                         BigDecimal createdPersonId, String createdEmpNo, String creationDate, BigDecimal lastUpdatedPersonId, String lastUpdatedEmpNo,
                         String lastUpdateDate, BigDecimal lastUpdateLogin, String drCr, String originalSupplyAmt, String originalVatAmt, String originalUsedAmt,
                         String originalTaxFlag, String originalTaxAcctCd, String taxFlag, String drcrType, String attribute1Code, String attribute2Code,
                         String attribute3Code, String attribute4Code, String attribute5Code, String attribute6Code, String withholdingTaxCode){
        this.slipHeaderId = slipHeaderId;
        this.slipLineId = slipLineId;
        this.slipLineNumber = slipLineNumber;
        this.slipSeq = slipSeq;
        this.lineTypeLookupCode = lineTypeLookupCode;
        this.orgId = orgId;
        this.ledgerId = ledgerId;
        this.ttypeInterfaceModule = ttypeInterfaceModule;
        this.source = source;
        this.compCode = compCode;
        this.acctName = acctName;
        this.actualDeptName = actualDeptName;
        this.compName = compName;
        this.budgetDeptCode = budgetDeptCode;
        this.budgetDeptName = budgetDeptName;
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.projectId = projectId;
        this.taskCode = taskCode;
        this.taskName = taskName;
        this.taskId = taskId;
        this.taskItemGroup = taskItemGroup;
        this.itemGroupType = itemGroupType;
        this.itemGroupCode = itemGroupCode;
        this.itemGroupName = itemGroupName;
        this.codeCombinationId = codeCombinationId;
        this.acctCode = acctCode;
        this.interfaceSlipType = interfaceSlipType;
        this.actualDeptCode = actualDeptCode;
        this.actualDeptType = actualDeptType;
        this.trAcctCode = trAcctCode;
        this.trAcctName = trAcctName;
        this.slipTypeCode = slipTypeCode;
        this.segment9Code = segment9Code;
        this.segment9Name = segment9Name;
        this.segment10Code = segment10Code;
        this.segment10Name = segment10Name;
        this.taxPercentageRate = taxPercentageRate;
        this.supplyAmount = supplyAmount;
        this.accountedSupplyAmount = accountedSupplyAmount;
        this.vatAmount = vatAmount;
        this.accountedVatAmount = accountedVatAmount;
        this.usedAmt = usedAmt;
        this.taxCode = taxCode;
        this.taxId = taxId;
        this.taxAcctCode = taxAcctCode;
        this.taxAcctName = taxAcctName;
        this.taxCodeCombinationId = taxCodeCombinationId;
        this.description = description;
        this.cardUsedNo = cardUsedNo;
        this.quantityInvoiced = quantityInvoiced;
        this.unitPrice = unitPrice;
        this.assetsTrackingFlag = assetsTrackingFlag;
        this.poHeaderId = poHeaderId;
        this.poHeaderNum = poHeaderNum;
        this.poLineId = poLineId;
        this.poLineNum = poLineNum;
        this.poLineLocationId = poLineLocationId;
        this.poDistributionId = poDistributionId;
        this.rcvTransactionId = rcvTransactionId;
        this.poUnitOfMeasure = poUnitOfMeasure;
        this.inventoryItemId = inventoryItemId;
        this.itemCode = itemCode;
        this.prepayInvoiceId = prepayInvoiceId;
        this.prepayLineNumber = prepayLineNumber;
        this.invoiceIncludesPrepayFlag = invoiceIncludesPrepayFlag;
        this.validationFlag = validationFlag;
        this.slipInterfaceFlag = slipInterfaceFlag;
        this.slipInterfaceErrorMsg = slipInterfaceErrorMsg;
        this.dffCnt = dffCnt;
        this.requiredFlagCnt = requiredFlagCnt;
        this.attributeCategory = attributeCategory;
        this.attributeCategoryName = attributeCategoryName;
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
        this.c_type = c_type;
        this.subTrxType = subTrxType;
        this.subTrxTypeName = subTrxTypeName;
        this.oAmtType = oAmtType;
        this.iAmtType = iAmtType;
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
        this.globalAttribute16 = globalAttribute16;
        this.globalAttribute17 = globalAttribute17;
        this.globalAttribute18 = globalAttribute18;
        this.globalAttribute19 = globalAttribute19;
        this.globalAttribute20 = globalAttribute20;
        this.lineAttribute1 = lineAttribute1;
        this.lineAttribute2 = lineAttribute2;
        this.lineAttribute3 = lineAttribute3;
        this.lineAttribute4 = lineAttribute4;
        this.lineAttribute5 = lineAttribute5;
        this.lineAttribute6 = lineAttribute6;
        this.lineAttribute7 = lineAttribute7;
        this.lineAttribute8 = lineAttribute8;
        this.lineAttribute9 = lineAttribute9;
        this.lineAttribute10 = lineAttribute10;
        this.createdPersonId = createdPersonId;
        this.createdEmpNo = createdEmpNo;
        this.creationDate = creationDate;
        this.lastUpdatedPersonId = lastUpdatedPersonId;
        this.lastUpdatedEmpNo = lastUpdatedEmpNo;
        this.lastUpdateDate = lastUpdateDate;
        this.lastUpdateLogin = lastUpdateLogin;
        this.drCr = drCr;
        this.originalSupplyAmt = originalSupplyAmt;
        this.originalVatAmt = originalVatAmt;
        this.originalUsedAmt = originalUsedAmt;
        this.originalTaxFlag = originalTaxFlag;
        this.originalTaxAcctCd = originalTaxAcctCd;
        this.taxFlag = taxFlag;
        this.drcrType = drcrType;
        this.attribute1Code = attribute1Code;
        this.attribute2Code = attribute2Code;
        this.attribute3Code = attribute3Code;
        this.attribute4Code = attribute4Code;
        this.attribute5Code = attribute5Code;
        this.attribute6Code = attribute6Code;
        this.withholdingTaxCode = String.valueOf(withholdingTaxCode);
    }

    String regId;
    LocalDateTime regDtm;
    String chgId;
    LocalDateTime chgDtm;
    String temp1;
    String temp2;
    String temp3;
    String temp4;
    String temp5;


    //교통비 전표 디테일 조회
    public SlipDetailDto(BigDecimal slipHeaderId, String compCd, String slipSeq, BigDecimal ledgerId, String ttypeInterfaceModule, String compCode, String compName,
                         String budgetDeptCode, String budgetDeptName, String projectCode, String projectName, BigDecimal projectId, String taskCode, String taskName,
                         BigDecimal taskId, String itemGroupCode, String itemGroupName, BigDecimal codeCombinationId, String acctCode, String acctName,
                         String interfaceSlipType, String actualDeptCode, String actualDeptName, String actualDeptType, String trAcctCode, String trAcctName,
                         String slipTypeCode, String segment9Code, String segment9Name, String segment10Code, String segment10Name, BigDecimal supplyAmount,
                         BigDecimal accountedSupplyAmount, BigDecimal vatAmount, BigDecimal accountedVatAmount, BigDecimal usedAmt, String assetsTrackingFlag,
                         String validationFlag, String slipInterfaceFlag, BigDecimal dffCnt, BigDecimal requiredFlagCnt, String drCr, String lineTypeLookupCode,
                         String slipNo, String usedDt, String fromArea, String fromAreaText, String toArea, String toAreaText, String trafficType, String distance,
                         String oilPrice, String mileage, String oilAmt, String oilBungae, String etcType1, String etcAmt1, String etcBungae1, String etcChitCd1,
                         String etcUsedNo1, String etcType2, String etcAmt2, String etcBungae2, String etcChitCd2, String etcUsedNo2, String etcType3, String etcAmt3,
                         String etcBungae3, String etcChitCd3, String etcUsedNo3, String sumAmt, String remark, String regId, Timestamp regDtm, String chgId,
                         Timestamp chgDtm, String temp1, String temp2, String temp3, String temp4, String temp5, String oilAmtType, BigDecimal confDist,
                         Timestamp paymentDt
    ){

                /*BigDecimal slipLineId, BigDecimal slipLineNumber, String source, String taskItemGroup, String itemGroupType, BigDecimal taxPercentageRate,
                String taxCode, BigDecimal taxId, String taxAcctCode, String taxAcctName, BigDecimal taxCodeCombinationId, String description, String cardUsedNo,
                BigDecimal quantityInvoiced, BigDecimal unitPrice, BigDecimal poHeaderId, BigDecimal poLineId, BigDecimal poLineLocationId, BigDecimal poDistributionId,
                BigDecimal rcvTransactionId, String poUnitOfMeasure, BigDecimal inventoryItemId, BigDecimal prepayInvoiceId, BigDecimal prepayLineNumber,
                String invoiceIncludesPrepayFlag,   String slipInterfaceErrorMsg, String attributeCategory, String attributeCategoryName, String attribute1,
                String attribute2, String attribute3, String attribute4, String attribute5, String attribute6, String attribute7, String attribute8, String attribute9,
                String attribute10, String attribute11, String attribute12, String attribute13, String attribute14, String attribute15, String c_type, String subTrxType,
                String subTrxTypeName, String oAmtType, String iAmtType, String globalAttributeCategory, String globalAttribute1, String globalAttribute2,
                String globalAttribute3, String globalAttribute4, String globalAttribute5, String globalAttribute6, String globalAttribute7, String globalAttribute8,
                String globalAttribute9, String globalAttribute10, String globalAttribute11, String globalAttribute12, String globalAttribute13,
                String globalAttribute14, String globalAttribute15, String globalAttribute16, String globalAttribute17, String globalAttribute18,
                String globalAttribute19, String globalAttribute20, String lineAttribute1, String lineAttribute2, String lineAttribute3, String lineAttribute4,
                String lineAttribute5, String lineAttribute6, String lineAttribute7, String lineAttribute8, String lineAttribute9, String lineAttribute10,
                BigDecimal createdPersonId, String createdEmpNo, String creationDate, BigDecimal lastUpdatedPersonId, String lastUpdatedEmpNo,
                String lastUpdateDate, BigDecimal lastUpdateLogin,  String originalSupplyAmt, String originalVatAmt, String originalUsedAmt,
                String originalTaxFlag, String originalTaxAcctCd, String taxFlag, String drcrType, String attribute1Code, String attribute2Code,
                String attribute3Code, String attribute4Code, String attribute5Code, String attribute6Code, String withholdingTaxCode*/
        this.slipHeaderId = slipHeaderId;
        this.compCd = compCd;
        this.slipSeq = slipSeq;
        this.ledgerId = ledgerId;
        this.ttypeInterfaceModule = ttypeInterfaceModule;
        this.compCode = compCode;
        this.compName = compName;
        this.budgetDeptCode = budgetDeptCode;
        this.budgetDeptName = budgetDeptName;
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.projectId = projectId;
        this.taskCode = taskCode;
        this.taskName = taskName;
        this.taskId = taskId;
        this.itemGroupCode = itemGroupCode;
        this.itemGroupName = itemGroupName;
        this.codeCombinationId = codeCombinationId;
        this.acctCode = acctCode;
        this.acctName = acctName;
        this.interfaceSlipType = interfaceSlipType;
        this.actualDeptCode = actualDeptCode;
        this.actualDeptName = actualDeptName;
        this.actualDeptType = actualDeptType;
        this.trAcctCode = trAcctCode;
        this.trAcctName = trAcctName;
        this.slipTypeCode = slipTypeCode;
        this.segment9Code = segment9Code;
        this.segment9Name = segment9Name;
        this.segment10Code = segment10Code;
        this.segment10Name = segment10Name;
        this.supplyAmount = supplyAmount;
        this.accountedSupplyAmount = accountedSupplyAmount;
        this.vatAmount = vatAmount;
        this.accountedVatAmount = accountedVatAmount;
        this.usedAmt = usedAmt;
        this.assetsTrackingFlag = assetsTrackingFlag;
        this.validationFlag = validationFlag;
        this.slipInterfaceFlag = slipInterfaceFlag;
        this.dffCnt = dffCnt;
        this.requiredFlagCnt = requiredFlagCnt;
        this.drCr = drCr;
        this.lineTypeLookupCode = lineTypeLookupCode;
        this.slipNo = slipNo;
        this.usedDt = usedDt;
        this.fromArea = fromArea;
        this.fromAreaText = fromAreaText;
        this.toArea = toArea;
        this.toAreaText = toAreaText;
        this.trafficType = trafficType;
        this.distance = distance;
        this.oilPrice = oilPrice;
        this.mileage = mileage;
        this.oilAmt = oilAmt;
        this.oilBungae = oilBungae;
        this.etcType1 = etcType1;
        this.etcAmt1 = etcAmt1;
        this.etcBungae1 = etcBungae1;
        this.etcChitCd1 = etcChitCd1;
        this.etcUsedNo1 = etcUsedNo1;
        this.etcType2 = etcType2;
        this.etcAmt2 = etcAmt2;
        this.etcBungae2 = etcBungae2;
        this.etcChitCd2 = etcChitCd2;
        this.etcUsedNo2 = etcUsedNo2;
        this.etcType3 = etcType3;
        this.etcAmt3 = etcAmt3;
        this.etcBungae3 = etcBungae3;
        this.etcChitCd3 = etcChitCd3;
        this.etcUsedNo3 = etcUsedNo3;
        this.sumAmt = sumAmt;
        this.bigo = remark;
        this.regId = regId;
        this.regDtm = regDtm == null ? null : regDtm.toLocalDateTime();
        this.chgId = chgId;
        this.chgDtm = chgDtm == null ? null : chgDtm.toLocalDateTime();
        this.temp1 = temp1;
        this.distanceGubun = temp2;
        this.temp3 = temp3;
        this.temp4 = temp4;
        this.temp5 = temp5;
        this.oilAmtType = oilAmtType;
        this.confDist = confDist;
        this.paymentDt = paymentDt == null ? null : paymentDt.toLocalDateTime();
    }

    String budgetAcctCode;
    String budgetAcctDesc;
    BigDecimal budgetAmount;
    BigDecimal tactualAmount;

    public SlipDetailDto(String budgetAcctCode, String budgetAcctDesc, BigDecimal budgetAmount, String acctName, BigDecimal tactualAmount) {
        this.budgetAcctCode = budgetAcctCode;
        this.budgetAcctDesc = budgetAcctDesc;
        this.budgetAmount = budgetAmount;
        this.acctName = acctName;
        this.tactualAmount = tactualAmount;
    }
}
