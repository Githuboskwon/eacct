package com.iljin.apiServer.ijeas.system.supplyBank;

import com.iljin.apiServer.ijeas.system.payBank.PayBankKey;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@IdClass(SupplyBankKey.class)
@NoArgsConstructor
@Table(name = "CBO_SP_SUPPLIER_BANK_V")
public class SupplyBank {

    @Id
    @Column(name = "VENDOR_ID")
    BigDecimal vendorId;

    @Id
    @Column(name = "INTEGRATION_VENDOR_NUM")
    String integrationVendorNum;

    @Column(name = "VENDOR_NAME")
    String vendorName;

    @Column(name = "VAT_REGISTRATION_NUM")
    String vatRegistrationNum;

    @Column(name = "VENDOR_SITE_ID")
    BigDecimal vendorSiteId;

    @Column(name = "SUPPLIER_SITE_ID")
    BigDecimal supplierSiteId;

    @Column(name = "VENDOR_SITE_CODE_ALT")
    String vendorSiteCodeAlt;

    @Id
    @Column(name = "BANK_ACCOUNT_ID")
    BigDecimal bankAccountId;

    @Column(name = "BANK_NAME")
    String bankName;

    @Id
    @Column(name = "BANK_NUMBER")
    String bankNumber;

    @Column(name = "BANK_BRANCH_NAME")
    String bankBranchName;

    @Column(name = "BANK_ACCOUNT_NAME")
    String bankAccountName;

    @Column(name = "BANK_ACCOUNT_NUMBER")
    String bankAccountNumber;

    @Column(name = "BANK_ACCOUNT_NUMBER_MASKING")
    String bankAccountNumberMasking;

    @Column(name = "PRIMARY_FLAG")
    String primaryFlag;

    @Column(name = "CURRENCY_CODE")
    String currencyCode;

    @Column(name = "ORDER_OF_PREFERENCE")
    Integer orderOfPreference;

    @Column(name = "EXT_BANK_ACCOUNT_ID")
    BigDecimal extBankAccountId;

    @Column(name = "END_DATE")
    LocalDateTime endDate;

    @Column(name = "ORG_ID")
    String orgId;

    @Column(name = "INDENTURE_CODE")
    String indentureCode;

    @Column(name = "INDENTURE_NAME")
    String indentureName;

    @Column(name = "NOTE_ACCOUNT_FLAG")
    String noteAccountFlag;

}
