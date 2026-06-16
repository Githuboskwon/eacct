package com.iljin.apiServer.ijeas.system.vendor;


import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "tb_mst_supplier_bank")
@IdClass(SupplierBankKey.class)
public class SupplierBank extends BaseEntity{
    @Id
    @Column(name ="COMP_CD")
    private String compCd;

    @Column(name ="VENDOR_ID")
    private Integer vendorId;

    @Id
    @Column(name ="INTEGRATION_VENDOR_NUM")
    private String integrationVendorNum;

    @Column(name ="VENDOR_NAME")
    private String vendorName;

    @Column(name ="VAT_REGISTRATION_NUM")
    private String vatRegistrationNum;

    @Column(name ="VENDOR_SITE_ID")
    private Integer vendorSiteId;

    @Column(name ="SUPPLIER_SITE_ID")
    private Integer supplierSiteId;

    @Column(name ="VENDOR_SITE_CODE_ALT")
    private String vendorSiteCodeAlt;

    @Column(name ="BANK_ACCOUNT_ID")
    private Integer bankAccountId;

    @Column(name ="BANK_NAME")
    private String bankName;

    @Column(name ="BANK_NUMBER")
    private String bankNumber;

    @Column(name ="BANK_BRANCH_NAME")
    private String bankBranchName;

    @Column(name ="BANK_ACCOUNT_NAME")
    private String bankAccountName;

    @Column(name ="BANK_ACCOUNT_NUMBER")
    private String bankAccountNumber;

    @Column(name ="BANK_ACCOUNT_NUMBER_MASKING")
    private String bankAccountNumberMasking;

    @Column(name ="PRIMARY_FLAG")
    private String primaryFlag;

    @Column(name ="CURRENCY_CODE")
    private String currencyCode;

    @Column(name ="ORDER_OF_PREFERENCE")
    private Integer orderOfPreference;

    @Column(name ="EXT_BANK_ACCOUNT_ID")
    private Integer extBankAccountId;

    @Column(name ="END_DATE")
    private LocalDateTime endDate;

    @Column(name ="INDENTURE_CODE")
    private String indentureCode;

    @Column(name ="INDENTURE_NAME")
    private String indentureName;

    @Column(name ="NOTE_ACCOUNT_FLAG")
    private String noteAccountFlag;

}
