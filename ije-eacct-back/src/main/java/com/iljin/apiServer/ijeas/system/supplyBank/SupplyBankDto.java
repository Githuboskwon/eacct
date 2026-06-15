package com.iljin.apiServer.ijeas.system.supplyBank;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SupplyBankDto implements Serializable {

    private static final long serialVersionUID = 1059255305123286586L;

    BigDecimal vendorId;

    String integrationVendorNum;

    String vendorName;

    String vatRegistrationNum;

    BigDecimal vendorSiteId;

    BigDecimal supplierSiteId;

    String vendorSiteCodeAlt;

    BigDecimal bankAccountId;

    String bankName;

    String bankNumber;

    String bankBranchName;

    String bankAccountName;

    String bankAccountName2;

    String bankAccountNumber;

    String bankAccountNumberMasking;

    String primaryFlag;

    String currencyCode;

    Integer orderOfPreference;

    BigDecimal extBankAccountId;

    LocalDateTime endDate;

    String orgId;

    String indentureCode;

    String indentureName;

    String noteAccountFlag;

    String termId;

    @QueryProjection
    public SupplyBankDto(String integrationVendorNum,String vendorName,String vatRegistrationNum,
                         BigDecimal vendorSiteId,BigDecimal supplierSiteId,String vendorSiteCodeAlt,BigDecimal bankAccountId,
                         String bankName,String bankNumber,String bankBranchName, String bankAccountName2 ,String bankAccountName,String bankAccountNumber,
                         String currencyCode,String primaryFlag, BigDecimal extBankAccountId, String indentureName,
                         String orgId,String noteAccountFlag) {

        this.integrationVendorNum = integrationVendorNum;
        this.vendorName = vendorName;
        this.vatRegistrationNum = vatRegistrationNum;
        this.vendorSiteId = vendorSiteId;
        this.supplierSiteId = supplierSiteId;
        this.vendorSiteCodeAlt = vendorSiteCodeAlt;
        this.bankAccountId = bankAccountId;
        this.bankName = bankName;
        this.bankNumber = bankNumber;
        this.bankBranchName = bankBranchName;
        this.bankAccountName2 = bankAccountName2;
        this.bankAccountName = bankAccountName;
        this.bankAccountNumber = bankAccountNumber;
        this.currencyCode = currencyCode;
        this.primaryFlag = primaryFlag;
        this.extBankAccountId = extBankAccountId;
        this.indentureName = indentureName;
        this.orgId = orgId;
        this.noteAccountFlag = noteAccountFlag;
    }
}
