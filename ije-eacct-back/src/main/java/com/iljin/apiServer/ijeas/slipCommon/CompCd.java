package com.iljin.apiServer.ijeas.slipCommon;

import lombok.Getter;

@Getter
public enum CompCd {

    ELECTRIC("81", "전기"),
    HOLDINGS("101", "홀딩스"),
    DNCO("102", "디앤코"),
    PARTNERS("103", "파트너스");

    final private String code;

    final private String name;

    CompCd(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
