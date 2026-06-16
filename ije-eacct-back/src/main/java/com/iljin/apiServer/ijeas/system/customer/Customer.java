package com.iljin.apiServer.ijeas.system.customer;

import com.iljin.apiServer.core.audit.BaseEntity;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@IdClass(CustomerKey.class)
@Table(name = "TB_MST_CUSTOMER_VENDOR")
@Entity
public class Customer extends BaseEntity {
    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    @Id
    @Column(name = "INTEGRATION_VENDOR_NUM", nullable = false)
    String integrationVendorNum;

    @Column(name = "INTEGRATION_VENDOR_NAME")
    String integrationVendorName;

    @Column(name = "VAT_REGISTRATION_NUM")
    String vatRegistrationNum;

    @Column(name = "CUSTOMER_ID")
    Long customerId;

    @Column(name = "CUSTOMER_SITE_ID")
    Long customerSiteId;

    @Column(name = "CUSTOMER_NUM")
    String customerNum;

    @Column(name = "CUSTOMER_NAME")
    String customerName;

    @Column(name = "CUST_VAT_REGISTRATION_NUM")
    String custVatRegistrationNum;

    @Column(name = "CUSTOMER_PARTY_ID")
    Long customerPartyId;

    @Column(name = "CUSTOMER_PARTY_NUMBER")
    String customerPartyNumber;

    @Column(name = "CUST_PARTY_SITE_ID")
    Long custPartySiteId;

    @Column(name = "CUST_LOCATION_ID")
    Long custLocationId;

    @Column(name = "CUSTOMER_TYPE")
    String customerType;

    @Column(name = "CUST_REPRESENTATIVE_NAME")
    String custRepresentativeName;

    @Column(name = "CUST_BUSINESS_TYPE")
    String custBusinessType;

    @Column(name = "CUST_BUSINESS_CONDITION")
    String custBusinessCondition;

    @Column(name = "CUST_ADDRESS")
    String custAddress;

    @Column(name = "CUST_CATEGORY_CODE")
    String custCategoryCode;

    @Column(name = "CUST_PAYMENT_TERM_ID")
    Long custPaymentTermId;

    @Column(name = "CUST_PARTY_STATUS")
    String custPartyStatus;

    @Column(name = "CUST_PARTY_SITES_STATUS")
    String custPartySitesStatus;

    @Column(name = "CUST_ACCOUNT_STATUS")
    String custAccountStatus;

    @Column(name = "CUST_SITE_STATUS")
    String custSiteStatus;

    @Column(name = "CUST_NUMBER_OLD")
    String custNumberOld;

    @Column(name = "CUST_ACCT_SITE_ID")
    Long custAcctSideId;

    @Column(name = "BILL_TO_SITE_USE_ID")
    Long billToSiteUseId;

    @Column(name = "CUST_SITE_USE_ID")
    Long custSiteUseId;

    @Column(name = "CUST_DAMBO_FLAG")
    String CustDamboFlag;

    @Column(name = "VENDOR_ID")
    Long vendorId;

    @Column(name = "VENDOR_SITE_ID")
    Long vendorSiteId;

    @Column(name = "VENDOR_NUM")
    String vendorNum;

    @Column(name = "VENDOR_NAME")
    String vendorName;

    @Column(name = "VENDOR_NAME_ALT")
    String vendorNameAlt;

    @Column(name = "VEN_VAT_REGISTRATION_NUM")
    String venVatRegistrationNum;

    @Column(name = "VENDOR_TYPE_LOOKUP_CODE")
    String vendorTypeLookupCode;

    @Column(name = "VEN_REPRESENTATIVE_NAME")
    String venRepresentativeName;

    @Column(name = "VEN_BUSINESS_CONDITION")
    String venBusinessCondition;

    @Column(name = "VEN_BUSINESS_TYPE")
    String venBusinessType;

    @Column(name = "VEN_ZIP")
    String venZip;

    @Column(name = "VEN_PHONE")
    String venPhone;

    @Column(name = "VEN_SITE_VALID")
    String venSiteValid;

    @Column(name = "VEN_TERMS_ID")
    Long venTermsId;

    @Column(name = "VEN_PAY_GROUP_LOOKUP_CD")
    String venPayGroupLookupCd;

    @Column(name = "VEN_INVOICE_CURRENCY_CD")
    String venInvoiceCurrencyCd;

    @Column(name = "VEN_PAYMENT_CURRENCY_CD")
    String venPaymentCurrencyCd;

    @Column(name = "VEN_ADDRESS")
    String venAddress;

    @Column(name = "VEN_TAX_AWT_FLAG")
    String venTaxAwtFlag;

    @Column(name = "VEN_PARTY_ID")
    Long venPartyId;

    @Column(name = "VEN_PARTY_SITE_ID")
    Long venPartySideId;

    @Column(name = "VEN_ENABLED_FLAG")
    String venEnabledFlag;

    @Column(name = "VEN_SITE_INACTIVE_DATE")
    LocalDateTime venSiteInactiveDate;

    @Column(name = "VEN_VAT_CODE")
    String venVatCode;

    @Column(name = "VEN_ATTRIBUTE5")
    String venAttribute5;

    @Column(name = "VEN_NUMBER_OLD")
    String venNumverOld;

    @Column(name = "AP_FLAG")
    String apFlag;

    @Column(name = "AR_FLAG")
    String arFlag;

    @Column(name = "APAR_FLAG")
    String aparFlag;






}
