package com.iljin.apiServer.ijeas.sm.close;

import com.iljin.apiServer.core.util.Pair;
import lombok.Getter;

public enum ClosStatCd {
    OPEN("Open", "오픈"),
    CLOSE("Close", "마감");

    @Getter
    final private String code;
    @Getter
    final private String name;

    ClosStatCd(String code, String name) {
        this.code = code;
        this.name = name;
    }

    Pair<String, String> getCodeAndValue() { return new Pair<>(code, name); }
}
