package com.iljin.apiServer.ijeas.slip.surtax;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class SurTaxCodeDto implements Serializable {
    private static final long serialVersionUID = 5729842227739508470L;

    String taxRateCode;

    String taxStatusCode;

    Integer taxRateId;

    String taxAcctCode;

    BigDecimal taxAccountCcid;

    Integer percentageRate;

    Integer ledgerId;

    String taxEvidenceType;

    String dtiType;

    @QueryProjection
    public SurTaxCodeDto(String taxRateCode, String taxStatusCode, Integer taxRateId, String taxAcctCode, Integer percentageRate) {

        this.taxRateCode = taxRateCode;
        this.taxStatusCode = taxStatusCode;
        this.taxRateId = taxRateId;
        this.taxAcctCode = taxAcctCode;
        this.percentageRate = percentageRate;
    }

}
