package com.iljin.apiServer.ijeas.slip.tax;

import lombok.Getter;

@Getter
public enum TaxCompCd {

    ELECTRIC_TAXCODE("1248667922", "전기"),
    HOLDINGS_TAXCODE("1248134732", "홀딩스"),
    DNCO_TAXCODE("1058721897", "디앤코"),
    PARTNERS_TAXCODE("1058183741", "파트너스");

    private final String code;

    private final String name;

    TaxCompCd(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
