package com.iljin.apiServer.ijeas.es;

import lombok.Getter;

@Getter
public enum SlipType {

    ITEM("21", "건별지급"),
    BULK("22", "대량지급"),
    BOND("23", "전자채권"),
    FUND("24", "자금전표"),
    CLCT("25", "집금전표"),
    GL("27", "GL전표"),
    SALE("28", "매출전표"),
    FRGN("29", "해외전표"),
    EXPT("30", "수출전표"),
    BUDGET("90", "비용예산"),    // ERP전표 X
    PJT("91", "PRJ실행예산기안");      // ERP전표 X

    private final String code;

    private final String name;

    SlipType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
