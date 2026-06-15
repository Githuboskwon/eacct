package com.iljin.apiServer.ijeas.system.benCountry;

import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BenCountryDto implements Serializable {

    private static final long serialVersionUID = 8487399672006567463L;

    String benCountryCd;
    String benCountryNm;
    String alternateBenCountryCd;
    String description;

    @QueryProjection
    public BenCountryDto(String benCountryCd, String benCountryNm, String alternateBenCountryCd,
        String description) {
        this.benCountryCd = benCountryCd;
        this.benCountryNm = benCountryNm;
        this.alternateBenCountryCd = alternateBenCountryCd;
        this.description = description;
    }
}
