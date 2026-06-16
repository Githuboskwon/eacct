package com.iljin.apiServer.ijeas.system.trx;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TrxDto implements Serializable {
    private static final long serialVersionUID = 615618657267846169L;
    String compCd;
    String trxTypeCd;
    String trxTypeNm;
    String slipTypeCd;
    String slipTypeNm;
    String inputModule;
    String inputModuleNm;
    String interfaceModule;
    String interfaceModuleNm;
    String interfaceSlipType;
    String interfaceSlipTypeNm;
    String trxTypeDescription;
    String trxSpTypeCd;
    String trxSpTypeNm;
    String prepaymentFlag;
    String clearingAcctCd;
    String clearingAcctNm;
    String addInfoType;
    String addInfoTypeNm;
    String integrationVendorNum;
    String vendorCustomerNm;
    Integer vendorCustomerId;
    Integer vendorCustomerSiteId;
    BigDecimal paymentReceiptTermId;
    String termNm;
    String enabledFlag;
    String jeSourceNm;
    String jeSource;
    String jeCategoryNm;
    String jeCategory;
    String awtGroupNm;
    BigDecimal awtGroupId;
    String slipDisplayFlag;
    String slipCreationTargetFlag;
    Integer orderBy;
    String userVendorFlag;
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

    Integer personId;
    String registrationMethod;
    String enableFlag;
    Integer ledgerId;

    @QueryProjection
    public TrxDto(String compCd, String trxTypeCd, String trxTypeNm, String slipTypeCd, String slipTypeNm, String inputModule, String inputModuleNm, String interfaceModule, String interfaceModuleNm,
                  String interfaceSlipType, String interfaceSlipTypeNm, String trxTypeDescription, String trxSpTypeCd, String trxSpTypeNm, String prepaymentFlag,
                  String clearingAcctCd, String clearingAcctNm, String addInfoType, String addInfoTypeNm, String integrationVendorNum, String vendorCustomerNm,
                  Integer vendorCustomerId, Integer vendorCustomerSiteId, BigDecimal paymentReceiptTermId, String termNm, String enabledFlag, String jeSourceNm,
                  String jeSource, String jeCategoryNm, String jeCategory, String awtGroupNm, BigDecimal awtGroupId, String slipDisplayFlag, String slipCreationTargetFlag,
                  Integer orderBy, String attribute1, String attribute2, String attribute3, String attribute4, String attribute5, String attribute6, String attribute7,
                  String attribute8, String attribute9, String attribute10, String attribute11, String attribute12, String attribute13, String attribute14, String attribute15) {
        this.compCd = compCd;
        this.trxTypeCd = trxTypeCd;
        this.trxTypeNm = trxTypeNm;
        this.slipTypeCd = slipTypeCd;
        this.slipTypeNm = slipTypeNm;
        this.inputModule = inputModule;
        this.inputModuleNm = inputModuleNm;
        this.interfaceModule = interfaceModule;
        this.interfaceModuleNm = interfaceModuleNm;
        this.interfaceSlipType = interfaceSlipType;
        this.interfaceSlipTypeNm = interfaceSlipTypeNm;
        this.trxTypeDescription = trxTypeDescription;
        this.trxSpTypeCd = trxSpTypeCd;
        this.trxSpTypeNm = trxSpTypeNm;
        this.prepaymentFlag = prepaymentFlag;
        this.clearingAcctCd = clearingAcctCd;
        this.clearingAcctNm = clearingAcctNm;
        this.addInfoType = addInfoType;
        this.addInfoTypeNm = addInfoTypeNm;
        this.integrationVendorNum = integrationVendorNum;
        this.vendorCustomerNm = vendorCustomerNm;
        this.vendorCustomerId = vendorCustomerId;
        this.vendorCustomerSiteId = vendorCustomerSiteId;
        this.paymentReceiptTermId = paymentReceiptTermId;
        this.termNm = termNm;
        this.enabledFlag = enabledFlag;
        this.jeSourceNm = jeSourceNm;
        this.jeSource = jeSource;
        this.jeCategoryNm = jeCategoryNm;
        this.jeCategory = jeCategory;
        this.awtGroupNm = awtGroupNm;
        this.awtGroupId = awtGroupId;
        this.slipDisplayFlag = slipDisplayFlag;
        this.slipCreationTargetFlag = slipCreationTargetFlag;
        this.orderBy = orderBy;
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
    }

    @QueryProjection
    public TrxDto(String compCd, String trxTypeCd, Integer personId, String registrationMethod, String enableFlag, Integer ledgerId, String attribute1, String attribute2,
                     String attribute3, String attribute4, String attribute5, String attribute6, String attribute7, String attribute8, String attribute9, String attribute10,
                     String attribute11, String attribute12, String attribute13, String attribute14, String attribute15) {
        this.compCd = compCd;
        this.trxTypeCd = trxTypeCd;
        this.personId = personId;
        this.registrationMethod = registrationMethod;
        this.enableFlag = enableFlag;
        this.ledgerId = ledgerId;
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
    }

    //TB_MST_ACCT
    String acctCd;
    String drCr;

    //CBO_SP_AWT_GROUP_V
    String locationCode;

    //CBO_SP_LOCATION_V
    String locationName;

    @QueryProjection
    public TrxDto(String compCd, String trxTypeCd, String trxTypeNm, String slipTypeCd) {
        this.compCd = compCd;
        this.trxTypeCd = trxTypeCd;
        this.trxTypeNm = trxTypeNm;
        this.slipTypeCd = slipTypeCd;
    }

    @QueryProjection
    public TrxDto(String compCd, String trxTypeCd, String trxTypeNm, String trxSpTypeCd, String inputModule, String interfaceSlipType, String prepaymentFlag,
                  String clearingAcctCd, String addInfoType, String integrationVendorNum, BigDecimal paymentReceiptTermId, String acctCd, String drCr,
                  String trxTypeDescription, String slipDisplayFlag, String slipCreationTargetFlag, BigDecimal awtGroupId, String awtGroupNm, Integer orderBy,
                  String locationCode, String locationName, Integer personId) {
        this.compCd = compCd;
        this.trxTypeCd = trxTypeCd;
        this.trxTypeNm = trxTypeNm;
        this.trxSpTypeCd = trxSpTypeCd;
        this.inputModule = inputModule;
        this.interfaceSlipType = interfaceSlipType;
        this.prepaymentFlag = prepaymentFlag;
        this.clearingAcctCd = clearingAcctCd;
        this.addInfoType = addInfoType;
        this.integrationVendorNum = integrationVendorNum;
        this.paymentReceiptTermId = paymentReceiptTermId;
        this.acctCd = acctCd;
        this.drCr = drCr;
        this.trxTypeDescription = trxTypeDescription;
        this.slipDisplayFlag = slipDisplayFlag;
        this.slipCreationTargetFlag = slipCreationTargetFlag;
        this.awtGroupId = awtGroupId;
        this.awtGroupNm = awtGroupNm;
        this.orderBy = orderBy;
        this.locationCode = locationCode;
        this.locationName = locationName;
        this.personId = personId;
    }

    BigDecimal acctCnt;
    String acctNm;
    String evidenceName;
    String evidenceCode;
    String lineAttribute1;
    String lineAttribute1Name;
    String lineAttribute2;
    String lineAttribute3;



    public TrxDto(String trxTypeNm, String trxTypeCd, String trxSpTypeCd, String slipTypeCd, String slipTypeNm, String inputModule, String interfaceModule,
                  String interfaceSlipType, String prepaymentFlag, String clearingAcctCd, String addInfoType, String integrationVendorNum,
                  BigDecimal paymentReceiptTermId, BigDecimal acctCnt, String acctCd, String trxTypeDescription, String acctNm, String drCr,
                  String slipDisplayFlag, String slipCreationTargetFlag, BigDecimal awtGroupId, String awtGroupNm, String userVendorFlag,
                  String locationCode, String locationName, String evidenceName, String evidenceCode, String lineAttribute1, String lineAttribute1Name,
                  String lineAttribute2, String lineAttribute3) {
        this.trxTypeCd = trxTypeCd;
        this.trxTypeNm = trxTypeNm;
        this.trxSpTypeCd = trxSpTypeCd;
        this.slipTypeCd = slipTypeCd;
        this.slipTypeNm = slipTypeNm;
        this.inputModule = inputModule;
        this.interfaceModule = interfaceModule;
        this.interfaceSlipType = interfaceSlipType;
        this.prepaymentFlag = String.valueOf(prepaymentFlag);
        this.clearingAcctCd = clearingAcctCd;
        this.addInfoType = addInfoType;
        this.integrationVendorNum = integrationVendorNum;
        this.paymentReceiptTermId = paymentReceiptTermId;
        this.acctCnt = acctCnt;
        this.acctCd = acctCd;
        this.trxTypeDescription = trxTypeDescription;
        this.acctNm = acctNm;
        this.drCr = drCr;
        this.slipDisplayFlag = String.valueOf(slipDisplayFlag);
        this.slipCreationTargetFlag = slipCreationTargetFlag;
        this.awtGroupId = awtGroupId;
        this.awtGroupNm = awtGroupNm;
        this.userVendorFlag = userVendorFlag;
        this.locationCode = locationCode;
        this.locationName = locationName;
        this.evidenceName = evidenceName;
        this.evidenceCode = evidenceCode;
        this.lineAttribute1 = lineAttribute1;
        this.lineAttribute1Name = lineAttribute1Name;
        this.lineAttribute2 = lineAttribute2;
        this.lineAttribute3 = lineAttribute3;
    }

    BigDecimal orgId;
    BigDecimal groupId;
    BigDecimal taxRateId;
    String taxName;
    String description;
    String awtAccount;
    String awtAccountName;
    BigDecimal taxRate;
    String vendorName;
    String vendorSiteName;
    String taxCodeCoa;

    public TrxDto(BigDecimal groupId, String taxName, String description, BigDecimal taxRateId, String awtAccount, String awtAccountName, BigDecimal taxRate,
                  BigDecimal orgId, String locationCode, String vendorName, String vendorSiteName, String taxCodeCoa){
        this.orgId = orgId;
        this.groupId = groupId;
        this.taxRateId = taxRateId;
        this.taxName = taxName;
        this.description = description;
        this.awtAccount = awtAccount;
        this.awtAccountName = awtAccountName;
        this.taxRate = taxRate;
        this.locationCode = locationCode;
        this.vendorName = vendorName;
        this.vendorSiteName = vendorSiteName;
        this.taxCodeCoa = taxCodeCoa;
    }

}
