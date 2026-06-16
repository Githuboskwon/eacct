package com.iljin.apiServer.ijeas.es.erpViews;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ErpGlTermsDto {

    String trxType;

    BigDecimal termId;

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

    String dueDate;

    String maturityDate;

    String typeInterfaceModule;

    String noteFlag;

    String postDt;

    String xDueDate;

    String xErrFlag;

    String xErrStep;

    String xErrMsg;

    String xMaturityDate;

    Integer termsStatus;

    Boolean termsFlag;

    String termsValue;

    String deptCd;

    String dtChangeFlag;

    String termName;

    @QueryProjection
    public ErpGlTermsDto(String trxType, Integer termId, String name, String description,
                         String orgId, String notesFlag, String maturityDays, String dueDateCalcFlag,
                         String currencyType, String paymentMethod, String vendorAcctCheck,
                         String spEnabledFlag, String defaultTermFlag) {

        this.trxType = trxType;
        this.termId = BigDecimal.valueOf(termId);
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
    }

    @QueryProjection
    public ErpGlTermsDto(String paymentMethod){
        this.paymentMethod = paymentMethod;
    }

    public ErpGlTermsDto(String name, String description, BigDecimal termId, String notesFlag, String paymentMethod, String vendorAcctCheck){
        this.name = name;
        this.description = description;
        this.termId = termId;
        this.notesFlag = notesFlag;
        this.paymentMethod = paymentMethod;
        this.vendorAcctCheck = vendorAcctCheck;
    }
}
