package com.iljin.apiServer.ijeas.es.erpViews;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Immutable
@Table(name = "CBO_GL_TRX_TYPE_HEADER_V")
@Entity
public class ErpGlTrxTypeHd {

    @Id
    @Column(name = "TRX_TYPE_CODE")
    String trxTypeCode;

    @Column(name = "TRX_TYPE_NAME")
    String trxTypeName;

    @Column(name = "ORG_ID")
    String orgId;

    @Column(name = "ORG_NAME")
    String orgName;

    @Column(name = "INPUT_MODULE")
    String inputModule;

    @Column(name = "INPUT_MODULE_NAME")
    String inputModuleName;

    @Column(name = "INTERFACE_MODULE")
    String interfaceModule;

    @Column(name = "INTERFACE_MODULE_NAME")
    String interfaceModuleName;

    @Column(name = "INTERFACE_SLIP_TYPE")
    String interfaceSlipType;

    @Column(name = "INTERFACE_SLIP_TYPE_NAME")
    String interfaceSlipTypeName;

    @Column(name = "TRX_TYPE_DESCRIPTION")
    String trxTypeDescription;

    @Column(name = "TRX_SP_TYPE_CODE")
    String trxSpTypeCode;

    @Column(name = "TRX_SP_TYPE_NAME")
    String trxSpTypeName;

    @Column(name = "PREPAYMENT_FLAG")
    String prepaymentFlag;

    @Column(name = "CLEARING_ACCT_CODE")
    String clearingAcctCode;

    @Column(name = "CLEARING_ACCT_NAME")
    String clearingAcctName;

    @Column(name = "ADD_INFO_TYPE")
    String addInfoType;

    @Column(name = "ADD_INFO_TYPE_NAME")
    String addInfoTypeName;

    @Column(name = "INTEGRATION_VENDOR_NUM")
    String integrationVendorNum;

    @Column(name = "VENDOR_CUSTOMER_NAME")
    String vendorCustomerName;

    @Column(name = "VENDOR_CUSTOMER_ID")
    BigDecimal vendorCustomerId;

    @Column(name = "VENDOR_CUSTOMER_SITE_ID")
    BigDecimal vendorCustomerSiteId;

    @Column(name = "PAYMENT_RECEIPT_TERM_ID")
    BigDecimal paymentReceiptTermId;

    @Column(name = "TERM_NAME")
    String termName;

    @Column(name = "ENABLED_FLAG")
    String enabledFlag;

    @Column(name = "CREATED_BY")
    BigDecimal createdBy;

    @Column(name = "CREATION_DATE")
    LocalDateTime creationDate;

    @Column(name = "LAST_UPDATED_BY")
    BigDecimal lastUpdatedBy;

    @Column(name = "LAST_UPDATE_DATE")
    LocalDateTime lastUpdateDate;

    @Column(name = "LAST_UPDATE_LOGIN")
    BigDecimal lastUpdateLogin;

    @Column(name = "JE_SOURCE_NAME")
    String jeSourceName;

    @Column(name = "JE_SOURCE")
    String jeSource;

    @Column(name = "JE_CATEGORY_NAME")
    String jeCategoryName;

    @Column(name = "JE_CATEGORY")
    String jeCategory;

    @Column(name = "AWT_GROUP_NAME")
    String awtGroupName;

    @Column(name = "AWT_GROUP_ID")
    BigDecimal awtGroupId;

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

    @Column(name = "SLIP_DISPLAY_FLAG")
    String slipDisplayFlag;

    @Column(name = "SLIP_CREATION_TARGET_FLAG")
    String slipCreationTargetFlag;

    @Column(name = "ORDER_BY")
    BigDecimal orderBy;

    @Column(name = "SLIP_TYPE_CD")
    String slipTypeCd;

    @Column(name = "SLIP_TYPE_NAME")
    String slipTypeName;

    @Column(name = "USER_VENDOR_FLAG")
    String userVendorFlag;
}
