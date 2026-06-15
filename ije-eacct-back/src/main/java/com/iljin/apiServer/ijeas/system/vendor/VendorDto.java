package com.iljin.apiServer.ijeas.system.vendor;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class VendorDto implements Serializable {
    private static final long serialVersionUID = -6204607240619479298L;

    String compCd;
    String integrationVendorNum;
    String integrationVendorName;
    String vatRegistrationNum;
    String bizType;
    String bizTypeName;
    String territoryCode;
    String regionCode;
    String regionName;
    Integer customerId;
    Integer customerSiteId;
    String customerNum;
    String customerName;
    String custVatRegistrationNum;
    Integer customerPartyId;
    String customerPartyNumber;
    Integer custPartySiteId;
    Integer custLocationId;
    String customerType;
    String custRepresentativeName;
    String custBusinessType;
    String custBusinessCondition;
    String custAddress;
    String custCategoryCode;
    Integer custPaymentTermId;
    String custPaymentTermName;
    String custPartyStatus;
    String custPartySitesStatus;
    String custAccountStatus;
    String custSiteStatus;
    String custNumberOld;
    Integer custAcctSiteId;
    Integer billToSiteUseId;
    Integer custSiteUseId;
    String custDamboFlag;
    String custDamboFlagName;
    String custEmail;
    Integer vendorId;
    Integer vendorSiteId;
    String vendorNum;
    String vendorName;
    String vendorNameAlt;
    String venVatRegistrationNum;
    String vendorTypeLookupCode;
    String venRepresontativeName;
    String venBusinessCondition;
    String venBusinessType;
    String venZip;
    String venPhone;
    String venSiteValid;
    Integer venTermsId;
    String venTermsName;
    String venPayGroupLookupCd;
    String venInvoiceCurrencyCd;
    String venPaymentCurrencyCd;
    String venAddress;
    String venTaxAwtFlag;
    Integer venPartyId;
    Integer venPartySiteId;
    String venEnabledFlag;
    LocalDateTime venSiteInactiveDate;
    String venVatCode;
    String venAttribute5;
    String venNumberOld;
    String venEmail;
    String apFlag;
    String arFlag;
    String aparFlag;
    String regId;
    LocalDateTime regDtm;
    String chgId;
    LocalDateTime chgDtm;

    Integer page;
    Integer limit;


    Integer supplierSiteId;
    String vendorSiteCodeAlt;
    Integer bankAccountId;
    String bankName;
    String bankNumber;
    String bankBranchName;
    String bankAccountName;
    String bankAccountNumber;
    String bankAccountNumberMasking;
    String primaryFlag;
    String currencyCode;
    Integer orderOfPreference;
    Integer extBankAccountId;
    LocalDateTime endDate;
    String indentureCode;
    String indentureName;
    String noteAccountFlag;

    BigDecimal prepayCnt;
    String custPaymentDescription;
    String venPaymentDescription;

    @QueryProjection
    public VendorDto(String compCd, String integrationVendorNum, String integrationVendorName, String vatRegistrationNum, String bizType, String bizTypeName
                    , String territoryCode, String regionCode, String regionName, Integer customerId, Integer customerSiteId, String customerNum, String customerName
                    , String custVatRegistrationNum, Integer customerPartyId, String customerPartyNumber, Integer custPartySiteId, Integer custLocationId
                    , String customerType, String custRepresentativeName, String custBusinessType, String custBusinessCondition, String custAddress, String custCategoryCode
                    , Integer custPaymentTermId, String custPaymentTermName, String custPartyStatus, String custPartySitesStatus, String custAccountStatus
                    , String custSiteStatus, String custNumberOld, Integer custAcctSiteId, Integer billToSiteUseId, Integer custSiteUseId, String custDamboFlag
                    , String custDamboFlagName, String custEmail, Integer vendorId, Integer vendorSiteId, String vendorNum, String vendorName, String vendorNameAlt
                    , String venVatRegistrationNum, String vendorTypeLookupCode, String venRepresontativeName, String venBusinessCondition, String venBusinessType
                    , String venZip, String venPhone, String venSiteValid, Integer venTermsId, String venTermsName, String venPayGroupLookupCd, String venInvoiceCurrencyCd
                    , String venPaymentCurrencyCd, String venAddress, String venTaxAwtFlag, Integer venPartyId, Integer venPartySiteId, String venEnabledFlag
                    , LocalDateTime venSiteInactiveDate, String venVatCode, String venAttribute5, String venNumberOld, String venEmail
                    , String apFlag, String arFlag, String aparFlag, String regId, LocalDateTime regDtm, String chgId
                    , LocalDateTime chgDtm, BigDecimal prepayCnt, String custPaymentDescription, String venPaymentDescription) {
        this.compCd = compCd;
        this.integrationVendorNum = integrationVendorNum;
        this.integrationVendorName = integrationVendorName;
        this.vatRegistrationNum = vatRegistrationNum;
        this.bizType = bizType;
        this.bizTypeName = bizTypeName;
        this.territoryCode = territoryCode;
        this.regionCode = regionCode;
        this.regionName = regionName;
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
        this.custPaymentTermName = custPaymentTermName;
        this.custPartyStatus = custPartyStatus;
        this.custPartySitesStatus = custPartySitesStatus;
        this.custAccountStatus = custAccountStatus;
        this.custSiteStatus = custSiteStatus;
        this.custNumberOld = custNumberOld;
        this.custAcctSiteId = custAcctSiteId;
        this.billToSiteUseId = billToSiteUseId;
        this.custSiteUseId = custSiteUseId;
        this.custDamboFlag = custDamboFlag;
        this.custDamboFlagName = custDamboFlagName;
        this.custEmail = custEmail;
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
        this.venTermsName = venTermsName;
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
        this.venEmail = venEmail;
        this.apFlag = apFlag;
        this.arFlag = arFlag;
        this.aparFlag = aparFlag;
        this.regId = regId;
        this.regDtm = regDtm;
        this.chgId = chgId;
        this.chgDtm = chgDtm;
        this.prepayCnt = prepayCnt;
        this.custPaymentDescription = custPaymentDescription;
        this.venPaymentDescription = venPaymentDescription;
    }

    public VendorDto(BigDecimal prepayCnt){
        this.prepayCnt = prepayCnt;
    }


    @QueryProjection
    public VendorDto(String compCd, String integrationVendorNum, String integrationVendorName, String vatRegistrationNum, String bizType, String bizTypeName
            , String territoryCode, String regionCode, String regionName, Integer customerId, Integer customerSiteId, String customerNum, String customerName
            , String custVatRegistrationNum, Integer customerPartyId, String customerPartyNumber, Integer custPartySiteId, Integer custLocationId
            , String customerType, String custRepresentativeName, String custBusinessType, String custBusinessCondition, String custAddress, String custCategoryCode
            , Integer custPaymentTermId, String custPaymentTermName, String custPartyStatus, String custPartySitesStatus, String custAccountStatus
            , String custSiteStatus, String custNumberOld, Integer custAcctSiteId, Integer billToSiteUseId, Integer custSiteUseId, String custDamboFlag
            , String custDamboFlagName, String custEmail, Integer vendorId, Integer vendorSiteId, String vendorNum, String vendorName, String vendorNameAlt
            , String venVatRegistrationNum, String vendorTypeLookupCode, String venRepresontativeName, String venBusinessCondition, String venBusinessType
            , String venZip, String venPhone, String venSiteValid, Integer venTermsId, String venTermsName, String venPayGroupLookupCd, String venInvoiceCurrencyCd
            , String venPaymentCurrencyCd, String venAddress, String venTaxAwtFlag, Integer venPartyId, Integer venPartySiteId, String venEnabledFlag
            , LocalDateTime venSiteInactiveDate, String venVatCode, String venAttribute5, String venNumberOld, String venEmail, String apFlag, String arFlag
            , String aparFlag, String regId, LocalDateTime regDtm, String chgId, LocalDateTime chgDtm, Integer page, Integer limit) {
        this.compCd = compCd;
        this.integrationVendorNum = integrationVendorNum;
        this.integrationVendorName = integrationVendorName;
        this.vatRegistrationNum = vatRegistrationNum;
        this.bizType = bizType;
        this.bizTypeName = bizTypeName;
        this.territoryCode = territoryCode;
        this.regionCode = regionCode;
        this.regionName = regionName;
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
        this.custPaymentTermName = custPaymentTermName;
        this.custPartyStatus = custPartyStatus;
        this.custPartySitesStatus = custPartySitesStatus;
        this.custAccountStatus = custAccountStatus;
        this.custSiteStatus = custSiteStatus;
        this.custNumberOld = custNumberOld;
        this.custAcctSiteId = custAcctSiteId;
        this.billToSiteUseId = billToSiteUseId;
        this.custSiteUseId = custSiteUseId;
        this.custDamboFlag = custDamboFlag;
        this.custDamboFlagName = custDamboFlagName;
        this.custEmail = custEmail;
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
        this.venTermsName = venTermsName;
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
        this.venEmail = venEmail;
        this.apFlag = apFlag;
        this.arFlag = arFlag;
        this.aparFlag = aparFlag;
        this.regId = regId;
        this.regDtm = regDtm;
        this.chgId = chgId;
        this.chgDtm = chgDtm;
        this.page = page;
        this.limit = limit;
    }


    @QueryProjection
    public VendorDto(String noteAccountFlag, String bankName, String bankAccountNumber, String bankAccountName, String primaryFlag, Integer bankAccountId, String currencyCode){
        this.noteAccountFlag = noteAccountFlag;
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
        this.bankAccountName = bankAccountName;
        this.primaryFlag = primaryFlag;
        this.bankAccountId = bankAccountId;
        this.currencyCode = currencyCode;
    }


    @QueryProjection
    public VendorDto(String compCd, String integrationVendorNum, String integrationVendorName, String vatRegistrationNum, Integer customerId, Integer customerSiteId
            , Integer vendorId, Integer vendorSiteId, String venPayGroupLookupCd) {
        this.compCd = compCd;
        this.integrationVendorNum = integrationVendorNum;
        this.integrationVendorName = integrationVendorName;
        this.vatRegistrationNum = vatRegistrationNum;
        this.customerId = customerId;
        this.customerSiteId = customerSiteId;
        this.vendorId = vendorId;
        this.vendorSiteId = vendorSiteId;
        this.venPayGroupLookupCd = venPayGroupLookupCd;
    }

    @QueryProjection
    public VendorDto(String compCd, String integrationVendorNum, String integrationVendorName, String vatRegistrationNum, Integer customerId, Integer customerSiteId
            , Integer vendorId, Integer vendorSiteId, String venPayGroupLookupCd, String territoryCode, String regionName, String apFlag, String arFlag
            , Integer page, Integer limit) {
        this.compCd = compCd;
        this.integrationVendorNum = integrationVendorNum;
        this.integrationVendorName = integrationVendorName;
        this.vatRegistrationNum = vatRegistrationNum;
        this.customerId = customerId;
        this.customerSiteId = customerSiteId;
        this.vendorId = vendorId;
        this.vendorSiteId = vendorSiteId;
        this.venPayGroupLookupCd = venPayGroupLookupCd;
        this.territoryCode = territoryCode;
        this.regionName = regionName;
        this.apFlag = apFlag;
        this.arFlag = arFlag;
        this.page = page;
        this.limit = limit;
    }

    String trxType;
    Integer termId;
    String name;
    String description;
    String orgId;
    String notesFlag;
    String maturityDays;
    String dueDateCalcFlag;
    String currencyType;
    String paymentMethod;
    String vendorAcctCheck;
    String spEnabledFlag;
    String defaultTermFlag;
    String searchNm;
    String searchCd;

    @QueryProjection
    public VendorDto(String trxType, Integer termId, String name, String description, String orgId, String notesFlag, String maturityDays, String dueDateCalcFlag,
                     String currencyType, String paymentMethod, String vendorAcctCheck, String spEnabledFlag, String defaultTermFlag, String searchNm, String searchCd) {
        this.trxType = trxType;
        this.termId = termId;
        this.name = name;
        this.description = description;
        this.orgId = orgId;
        this.notesFlag = notesFlag;
        this.maturityDays = maturityDays;
        this.dueDateCalcFlag = dueDateCalcFlag;
        this.currencyType = currencyType;
        this.paymentMethod = paymentMethod;
        this.vendorAcctCheck = vendorAcctCheck;
        this.spEnabledFlag = spEnabledFlag;
        this.defaultTermFlag = defaultTermFlag;
        this.searchNm = searchNm;
        this.searchCd = searchCd;
    }
}
