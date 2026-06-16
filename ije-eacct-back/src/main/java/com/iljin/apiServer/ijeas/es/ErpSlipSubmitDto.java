package com.iljin.apiServer.ijeas.es;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ErpSlipSubmitDto implements Serializable {

    private static final long serialVersionUID = 5281003613811924734L;

    String slipType;

    BigDecimal compCd;

    String slipNo;

    BigDecimal slipHeaderId;

    BigDecimal approvalGroupId;

    String slipTypeText;

    String createUser;

    String reportArraign;

    String approvalArraign;

    String sealArraign;

    String integrationVendorNum;

    String storeName;

    String erpValidation;

    BigDecimal enteredAmount;

    BigDecimal usedAmt;

    String approvalStatus;

    String slipCurrencyCode;

    BigDecimal usedFAmt;

    Timestamp glDate;

    String slipDataFixFlag;

    String slipIfFlag;

    String slipInterfaceErrorMsg;

    String transferType;

    String status;

    String postingDt;

    String erpAppUserId;

    String slipStatus;

    String slipStatusText;

    String erpState;

    String slipStatusName;

    String slipGroupYn;

    BigDecimal ledgerId;

    String slipGroupNumber;

    String ttypeInputModule;

    String slipForm;

    String erpInvoiceId;

    String searchMonth;

    // list

    @QueryProjection
    public ErpSlipSubmitDto(String slipType,BigDecimal compCd,String slipNo,BigDecimal slipHeaderId,BigDecimal approvalGroupId,
                            String slipTypeText,String createUser,String reportArraign,String approvalArraign,String sealArraign,
                            String integrationVendorNum,String storeName,String erpValidation,BigDecimal enteredAmount,BigDecimal usedAmt,
                            String approvalStatus,String slipCurrencyCode,BigDecimal usedFAmt,Timestamp glDate,String slipDataFixFlag,
                            String slipIfFlag,String slipInterfaceErrorMsg,String transferType,String status,String postingDt,
                            String erpAppUserId,String slipStatus,String slipStatusText,String erpState,String slipStatusName,
                            String slipGroupYn,BigDecimal ledgerId,String slipGroupNumber,String ttypeInputModule,String slipForm
                            ,String erpInvoiceId
    ) {
        this.slipType = slipType;
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipHeaderId = slipHeaderId;
        this.approvalGroupId = approvalGroupId;
        this.slipTypeText = slipTypeText;
        this.createUser = createUser;
        this.reportArraign = reportArraign;
        this.approvalArraign = approvalArraign;
        this.sealArraign = sealArraign;
        this.integrationVendorNum = integrationVendorNum;
        this.storeName = storeName;
        this.erpValidation = erpValidation;
        this.enteredAmount = enteredAmount;
        this.usedAmt = usedAmt;
        this.approvalStatus = approvalStatus;
        this.slipCurrencyCode = slipCurrencyCode;
        this.usedFAmt = usedFAmt;
        this.glDate = glDate;
        this.slipDataFixFlag = slipDataFixFlag;
        this.slipIfFlag = slipIfFlag;
        this.slipInterfaceErrorMsg = slipInterfaceErrorMsg;
        this.transferType = transferType;
        this.status = status;
        this.postingDt = postingDt;
        this.erpAppUserId = erpAppUserId;
        this.slipStatus = slipStatus;
        this.slipStatusText = slipStatusText;
        this.erpState = erpState;
        this.slipStatusName = slipStatusName;
        this.slipGroupYn = slipGroupYn;
        this.ledgerId = ledgerId;
        this.slipGroupNumber = slipGroupNumber;
        this.ttypeInputModule = ttypeInputModule;
        this.slipForm = slipForm;
        this.erpInvoiceId = erpInvoiceId;
    }

}
