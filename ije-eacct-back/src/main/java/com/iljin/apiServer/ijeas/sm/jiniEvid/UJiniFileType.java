package com.iljin.apiServer.ijeas.sm.jiniEvid;

import lombok.Getter;

public enum UJiniFileType {

    MOBILE("10", "모바일"),
    ESLIP("20", "전자전표"),
    BUDGET("30", "예산"),
    TEMP("40", "Temp"),
    CERT("50", "인증서");

    @Getter
    final private String code;

    @Getter
    final private String name;

    UJiniFileType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
