package com.iljin.apiServer.ijeas.es.erpViews;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ErpGlCodesDto {

    String taxIssueTypeCode;

    String taxIssueTypeName;

    BigDecimal codeTypeId;

    String glSlipTypeCd;
    String glSlipTypeNm;


    @QueryProjection
    public ErpGlCodesDto(String taxIssueTypeCode, String taxIssueTypeName) {
        this.taxIssueTypeCode = taxIssueTypeCode;
        this.taxIssueTypeName = taxIssueTypeName;
    }

    public ErpGlCodesDto(BigDecimal codeTypeId, String glSlipTypeCd, String glSlipTypeNm) {
        this.codeTypeId = codeTypeId;
        this.glSlipTypeCd = glSlipTypeCd;
        this.glSlipTypeNm = glSlipTypeNm;
    }
}
