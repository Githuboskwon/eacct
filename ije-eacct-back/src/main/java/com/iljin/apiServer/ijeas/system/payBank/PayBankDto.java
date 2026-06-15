package com.iljin.apiServer.ijeas.system.payBank;

import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PayBankDto implements Serializable {

    private static final long serialVersionUID = 1059255305123286586L;

    Long operatingUnitId;

    Long bankAccountId;

    String bankAccountName;

    String bankAccountNum;

    String bankName;

    String bankBranchName;

    String currencyCode;

    @QueryProjection
    public PayBankDto(Long operatingUnitId, Long bankAccountId, String bankAccountName,
        String bankAccountNum, String bankName, String bankBranchName, String currencyCode) {
        this.operatingUnitId = operatingUnitId;
        this.bankAccountId = bankAccountId;
        this.bankAccountName = bankAccountName;
        this.bankAccountNum = bankAccountNum;
        this.bankName = bankName;
        this.bankBranchName = bankBranchName;
        this.currencyCode = currencyCode;
    }
}
