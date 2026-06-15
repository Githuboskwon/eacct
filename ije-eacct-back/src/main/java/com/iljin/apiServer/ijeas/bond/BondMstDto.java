package com.iljin.apiServer.ijeas.bond;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BondMstDto implements Serializable {

    private static final long serialVersionUID = -1985303773285041103L;

    String compCd;

    String refNo;

    String bondCd;

    String benCountry;

    String projectNm;

    String intBankNm;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime openingDt;

    BigDecimal budget;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime releaseDt;

    String benCountryCd;

    String customerNm;

    String customerId;

    String projectId;

    String localBankNm;

    String localBankId;

    BigDecimal currencyAmt;

    String currencyCd;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime openingDtFrom;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime openingDtTo;

    @QueryProjection
    public BondMstDto(String compCd, String refNo, String bondCd, String benCountry,
        String projectNm,
        String intBankNm, LocalDateTime openingDt, BigDecimal budget, LocalDateTime releaseDt,
        String benCountryCd, String customerNm, String customerId, String projectId,
        String localBankNm,
        String localBankId, BigDecimal currencyAmt, String currencyCd) {
        this.compCd = compCd;
        this.refNo = refNo;
        this.bondCd = bondCd;
        this.benCountry = benCountry;
        this.projectNm = projectNm;
        this.intBankNm = intBankNm;
        this.openingDt = openingDt;
        this.budget = budget;
        this.releaseDt = releaseDt;
        this.benCountryCd = benCountryCd;
        this.customerNm = customerNm;
        this.customerId = customerId;
        this.projectId = projectId;
        this.localBankNm = localBankNm;
        this.localBankId = localBankId;
        this.currencyAmt = currencyAmt;
        this.currencyCd = currencyCd;
    }
}
