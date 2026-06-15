package com.iljin.apiServer.ijeas.es.erpViews;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ErpGlTrxTypeHdDto implements Serializable {

    private static final long serialVersionUID = 1362934925090213230L;

    String trxTypeCode;

    String trxTypeName;

    String orgId;

    String orgName;

    String inputModule;

    String inputModuleName;

    String interfaceModule;

    String interfaceModuleName;

    String interfaceSlipType;

    String interfaceSlipTypeName;

    String trxTypeDescription;

    String trxSpTypeCode;

    String trxSpTypeName;

    String prepaymentFlag;

    String clearingAcctCode;

    String clearingAcctName;

    String addInfoType;

    String addInfoTypeName;

    String integrationVendorNum;

    String vendorCustomerName;

    BigDecimal vendorCustomerId;

    BigDecimal vendorCustomerSiteId;

    BigDecimal paymentReceiptTermId;

    String termName;

    String enabledFlag;

    BigDecimal createdBy;

    LocalDateTime creationDate;

    BigDecimal lastUpdatedBy;

    LocalDateTime lastUpdateDate;

    BigDecimal lastUpdateLogin;

    String jeSourceName;

    String jeSource;

    String jeCategoryName;

    String jeCategory;

    String awtGroupName;

    BigDecimal awtGroupId;

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

    String slipDisplayFlag;

    String slipCreationTargetFlag;

    BigDecimal orderBy;

    String slipTypeCd;

    String slipTypeName;

    String userVendorFlag;

    @QueryProjection
    public ErpGlTrxTypeHdDto(String trxTypeCode, String trxTypeName){
        this.trxTypeCode = trxTypeCode;
        this.trxTypeName = trxTypeName;
    }
}
