package com.iljin.apiServer.ijeas.es.erpViews;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ErpTaxEvidenceCodeDto {

    String evidenceName;

    String evidenceCode;

    String trxTypeCode;

    String lineAttribute1;

    String lineAttribute1Name;

    String lineAttribute2;

    String lineAttribute3;

    String lineAttribute6;

    String code;

    @QueryProjection
    public ErpTaxEvidenceCodeDto(String evidenceName, String evidenceCode, String trxTypeCode,
                                 String lineAttribute1, String lineAttribute1Name, String lineAttribute2,
                                 String lineAttribute3, String lineAttribute6, String code) {
        this.evidenceName = evidenceName;
        this.evidenceCode = evidenceCode;
        this.trxTypeCode = trxTypeCode;
        this.lineAttribute1 = lineAttribute1;
        this.lineAttribute1Name = lineAttribute1Name;
        this.lineAttribute2 = lineAttribute2;
        this.lineAttribute3 = lineAttribute3;
        this.lineAttribute6 = lineAttribute6;
        this.code = code;
    }
}
