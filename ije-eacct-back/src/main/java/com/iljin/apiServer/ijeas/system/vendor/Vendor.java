package com.iljin.apiServer.ijeas.system.vendor;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/*@NamedStoredProcedureQuery(
        name = "SP_MST_CUSTOMER_VENDOR",
        procedureName = "SP_MST_CUSTOMER_VENDOR",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "Msg")
        }
)*/

@Getter
@Entity
@NoArgsConstructor
@Table(name = "tb_mst_customer_vendor")
@IdClass(VendorKey.class)
public class Vendor extends BaseEntity {

    @Id
    @Column(name = "COMP_CD", nullable = false)
    private String compCd;

    @Id
    @Column(name = "INTEGRATION_VENDOR_NUM", nullable = false)
    private String integrationVendorNum;

    @Column(name = "INTEGRATION_VENDOR_NAME")
    private String integrationVendorName;

    @Column(name = "VAT_REGISTRATION_NUM")
    private String vatRegistrationNum;

    @Column(name = "BIZ_TYPE")
    private String bizType;

    @Column(name = "BIZ_TYPE_NAME")
    private String bizTypeName;

    @Column(name = "TERRITORY_CODE")
    private String territoryCode;

    @Column(name = "REGION_CODE")
    private String regionCode;

    @Column(name = "REGION_NAME")
    private String regionName;

    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "CUSTOMER_SITE_ID")
    private Integer customerSiteId;

    @Column(name = "CUSTOMER_NUM")
    private String customerNum;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CUST_VAT_REGISTRATION_NUM")
    private String custVatRegistrationNum;

    @Column(name = "CUSTOMER_PARTY_ID")
    private Integer customerPartyId;

    @Column(name = "CUSTOMER_PARTY_NUMBER")
    private String customerPartyNumber;

    @Column(name = "CUST_PARTY_SITE_ID")
    private Integer custPartySiteId;

    @Column(name = "CUST_LOCATION_ID")
    private Integer custLocationId;

    @Column(name = "CUSTOMER_TYPE")
    private String customerType;

    @Column(name = "CUST_REPRESENTATIVE_NAME")
    private String custRepresentativeName;

    @Column(name = "CUST_BUSINESS_TYPE")
    private String custBusinessType;

    @Column(name = "CUST_BUSINESS_CONDITION")
    private String custBusinessCondition;

    @Column(name = "CUST_ADDRESS")
    private String custAddress;

    @Column(name = "CUST_CATEGORY_CODE")
    private String custCategoryCode;

    @Column(name = "CUST_PAYMENT_TERM_ID")
    private Integer custPaymentTermId;

    @Column(name = "CUST_PAYMENT_TERM_NAME")
    private String custPaymentTermName;

    @Column(name = "CUST_PARTY_STATUS")
    private String custPartyStatus;

    @Column(name = "CUST_PARTY_SITES_STATUS")
    private String custPartySitesStatus;

    @Column(name = "CUST_ACCOUNT_STATUS")
    private String custAccountStatus;

    @Column(name = "CUST_SITE_STATUS")
    private String custSiteStatus;

    @Column(name = "CUST_NUMBER_OLD")
    private String custNumberOld;

    @Column(name = "CUST_ACCT_SITE_ID")
    private Integer custAcctSiteId;

    @Column(name = "BILL_TO_SITE_USE_ID")
    private Integer billToSiteUseId;

    @Column(name = "CUST_SITE_USE_ID")
    private Integer custSiteUseId;

    @Column(name = "CUST_DAMBO_FLAG")
    private String custDamboFlag;

    @Column(name = "CUST_DAMBO_FLAG_NAME")
    private String custDamboFlagName;

    @Column(name = "CUST_EMAIL")
    private String custEmail;

    @Column(name = "VENDOR_ID")
    private Integer vendorId;

    @Column(name = "VENDOR_SITE_ID")
    private Integer vendorSiteId;

    @Column(name = "VENDOR_NUM")
    private String vendorNum;

    @Column(name = "VENDOR_NAME")
    private String vendorName;

    @Column(name = "VENDOR_NAME_ALT")
    private String vendorNameAlt;

    @Column(name = "VEN_VAT_REGISTRATION_NUM")
    private String venVatRegistrationNum;

    @Column(name = "VENDOR_TYPE_LOOKUP_CODE")
    private String vendorTypeLookupCode;

    @Column(name = "VEN_REPRESENTATIVE_NAME")
    private String venRepresontativeName;

    @Column(name = "VEN_BUSINESS_CONDITION")
    private String venBusinessCondition;

    @Column(name = "VEN_BUSINESS_TYPE")
    private String venBusinessType;

    @Column(name = "VEN_ZIP")
    private String venZip;

    @Column(name = "VEN_PHONE")
    private String venPhone;

    @Column(name = "VEN_SITE_VALID")
    private String venSiteValid;

    @Column(name = "VEN_TERMS_ID")
    private Integer venTermsId;

    @Column(name = "VEN_TERMS_NAME")
    private String venTermsName;

    @Column(name = "VEN_PAY_GROUP_LOOKUP_CD")
    private String venPayGroupLookupCd;

    @Column(name = "VEN_INVOICE_CURRENCY_CD")
    private String venInvoiceCurrencyCd;

    @Column(name = "VEN_PAYMENT_CURRENCY_CD")
    private String venPaymentCurrencyCd;

    @Column(name = "VEN_ADDRESS")
    private String venAddress;

    @Column(name = "VEN_TAX_AWT_FLAG")
    private String venTaxAwtFlag;

    @Column(name = "VEN_PARTY_ID")
    private Integer venPartyId;

    @Column(name = "VEN_PARTY_SITE_ID")
    private Integer venPartySiteId;

    @Column(name = "VEN_ENABLED_FLAG")
    private String venEnabledFlag;

    @Column(name = "VEN_SITE_INACTIVE_DATE")
    private LocalDateTime venSiteInactiveDate;

    @Column(name = "VEN_VAT_CODE")
    private String venVatCode;

    @Column(name = "VEN_ATTRIBUTE5")
    private String venAttribute5;

    @Column(name = "VEN_NUMBER_OLD")
    private String venNumberOld;

    @Column(name = "VEN_EMAIL")
    private String venEmail;

    @Column(name = "AP_FLAG")
    private String apFlag;

    @Column(name = "AR_FLAG")
    private String arFlag;

    @Column(name = "APAR_FLAG")
    private String aparFlag;

    @Builder
    public Vendor(String compCd, String integrationVendorNum, String integrationVendorName, String vatRegistrationNum, Integer customerId, Integer customerSiteId
                , String customerNum, String customerName, String custVatRegistrationNum, Integer customerPartyId, String customerPartyNumber, Integer custPartySiteId
                , Integer custLocationId, String customerType, String custRepresentativeName, String custBusinessType, String custBusinessCondition, String custAddress
                , String custCategoryCode, Integer custPaymentTermId, String custPartyStatus, String custPartySitesStatus, String custAccountStatus
                , String custSiteStatus, String custNumberOld, Integer custAcctSiteId, Integer billToSiteUseId, Integer custSiteUseId, String custDamboFlag
                , Integer vendorId, Integer vendorSiteId, String vendorNum, String vendorName, String vendorNameAlt, String venVatRegistrationNum
                , String vendorTypeLookupCode, String venRepresontativeName, String venBusinessCondition, String venBusinessType, String venZip, String venPhone
                , String venSiteValid, Integer venTermsId, String venPayGroupLookupCd, String venInvoiceCurrencyCd, String venPaymentCurrencyCd, String venAddress
                , String venTaxAwtFlag, Integer venPartyId, Integer venPartySiteId, String venEnabledFlag, LocalDateTime venSiteInactiveDate, String venVatCode
                , String venAttribute5, String venNumberOld, String apFlag, String arFlag, String aparFlag) {
        this.compCd = compCd;
        this.integrationVendorNum = integrationVendorNum;
        this.integrationVendorName = integrationVendorName;
        this.vatRegistrationNum = vatRegistrationNum;

        this.customerId = customerId;
        this.customerSiteId = customerSiteId;
        this.customerNum = customerNum;
        this.customerName = customerName;
        this.custVatRegistrationNum = custVatRegistrationNum;
        this.customerPartyId = customerPartyId;
        this.customerPartyNumber = customerPartyNumber;
        this.custPartySiteId = custPartySiteId;
        this.custLocationId = custLocationId;
        this.customerType = customerType;
        this.custRepresentativeName = custRepresentativeName;
        this.custBusinessType = custBusinessType;
        this.custBusinessCondition = custBusinessCondition;
        this.custAddress = custAddress;
        this.custCategoryCode = custCategoryCode;
        this.custPaymentTermId = custPaymentTermId;
        this.custPartyStatus = custPartyStatus;
        this.custPartySitesStatus = custPartySitesStatus;
        this.custAccountStatus = custAccountStatus;
        this.custSiteStatus = custSiteStatus;
        this.custNumberOld = custNumberOld;
        this.custAcctSiteId = custAcctSiteId;
        this.billToSiteUseId = billToSiteUseId;
        this.custSiteUseId = custSiteUseId;
        this.custDamboFlag = custDamboFlag;
        this.vendorId = vendorId;
        this.vendorSiteId = vendorSiteId;
        this.vendorNum = vendorNum;
        this.vendorName = vendorName;
        this.vendorNameAlt = vendorNameAlt;
        this.venVatRegistrationNum = venVatRegistrationNum;
        this.vendorTypeLookupCode = vendorTypeLookupCode;
        this.venRepresontativeName = venRepresontativeName;
        this.venBusinessCondition = venBusinessCondition;
        this.venBusinessType = venBusinessType;
        this.venZip = venZip;
        this.venPhone = venPhone;
        this.venSiteValid = venSiteValid;
        this.venTermsId = venTermsId;
        this.venPayGroupLookupCd = venPayGroupLookupCd;
        this.venInvoiceCurrencyCd = venInvoiceCurrencyCd;
        this.venPaymentCurrencyCd = venPaymentCurrencyCd;
        this.venAddress = venAddress;
        this.venTaxAwtFlag = venTaxAwtFlag;
        this.venPartyId = venPartyId;
        this.venPartySiteId = venPartySiteId;
        this.venEnabledFlag = venEnabledFlag;
        this.venSiteInactiveDate = venSiteInactiveDate;
        this.venVatCode = venVatCode;
        this.venAttribute5 = venAttribute5;
        this.venNumberOld = venNumberOld;
        this.apFlag = apFlag;
        this.arFlag = arFlag;
        this.aparFlag = aparFlag;
    }


}
