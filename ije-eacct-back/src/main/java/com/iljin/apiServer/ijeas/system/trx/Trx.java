package com.iljin.apiServer.ijeas.system.trx;


import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;


@Getter
@NoArgsConstructor
@IdClass(TrxKey.class)
@Table(name = "TB_MST_TRX")
@Entity
public class Trx extends BaseEntity {
    @Id
    @Column(name = "COMP_CD")
    String compCd;

    @Id
    @Column(name = "TRX_TYPE_CD")
    String trxTypeCd;

    @Column(name = "TRX_TYPE_NM")
    String trxTypeNm;

    @Column(name = "SLIP_TYPE_CD")
    String slipTypeCd;

    @Column(name = "SLIP_TYPE_NM")
    String slipTypeNm;

    @Column(name = "INPUT_MODULE")
    String inputMoule;

    @Column(name = "INPUT_MODULE_NM")
    String inputModuleNm;

    @Column(name = "INTERFACE_MODULE")
    String interfaceModule;

    @Column(name = "INTERFACE_MODULE_NM")
    String interfaceModuleNm;

    @Column(name = "INTERFACE_SLIP_TYPE")
    String interfaceSlipType;

    @Column(name = "INTERFACE_SLIP_TYPE_NM")
    String interfaceSlipTypeNm;

    @Column(name = "TRX_TYPE_DESCRIPTION")
    String trxTypeDescription;

    @Column(name = "TRX_SP_TYPE_CD")
    String trxSpTypeCd;

    @Column(name = "TRX_SP_TYPE_NM")
    String trxSpTypeNm;

    @Column(name = "PREPAYMENT_FLAG")
    String prepaymentFlag;

    @Column(name = "CLEARING_ACCT_CD")
    String clearingAcctCd;

    @Column(name = "CLEARING_ACCT_NM")
    String clearingAcctNm;

    @Column(name = "ADD_INFO_TYPE")
    String addInfoType;

    @Column(name = "ADD_INFO_TYPE_NM")
    String addInfoTypeNm;

    @Column(name = "INTEGRATION_VENDOR_NUM")
    String integrationVendorNum;

    @Column(name = "VENDOR_CUSTOMER_NM")
    String vendorCustomerNm;

    @Column(name = "VENDOR_CUSTOMER_ID")
    Integer vendorCustomerId;

    @Column(name = "VENDOR_CUSTOMER_SITE_ID")
    Integer vendorCustomerSiteId;

    @Column(name = "PAYMENT_RECEIPT_TERM_ID")
    BigDecimal paymentReceiptTermId;

    @Column(name = "TERM_NM")
    String termNm;

    @Column(name = "ENABLED_FLAG")
    String enabledFlag;

    @Column(name = "JE_SOURCE_NM")
    String jeSourceNm;

    @Column(name = "JE_SOURCE")
    String jeSource;

    @Column(name = "JE_CATEGORY_NM")
    String jeCategoryNm;

    @Column(name = "JE_CATEGORY")
    String jeCategory;

    @Column(name = "AWT_GROUP_NM")
    String awtGroupNm;

    @Column(name = "AWT_GROUP_ID")
    BigDecimal awtGroupId;

    @Column(name = "SLIP_DISPLAY_FLAG")
    String slipDisplayFlag;

    @Column(name = "SLIP_CREATION_TARGET_FLAG")
    String slipCreationTargetFlag;

    @Column(name = "ORDER_BY")
    Integer orderBy;

    @Column(name = "ATTRIBUTE1")
    String attribute1;

    @Column(name = "ATTRIBUTE2")
    String attribute2;

    @Column(name = "ATTRIBUTE3")
    String attribute3;

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


}
