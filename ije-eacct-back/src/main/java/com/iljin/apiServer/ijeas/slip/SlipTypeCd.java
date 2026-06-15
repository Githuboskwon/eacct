package com.iljin.apiServer.ijeas.slip;

import lombok.Getter;

@Getter
public enum SlipTypeCd {

    EXPEND("EXPEND", "경조금"),
    TRIP("TRIP", "출장비"),
    TRAFFIC("TRAFFIC", "여비교통비"),
    BOND("BOND", "BOND"),
    AWT("AWT", "원천세"),
    ETCAWT("ETCAWT", "잡급비"),
    CARD("CARD", "법인카드"),
    ACARD("ACARD", "법인카드(항공권)"),
    PO("PO", "구매물대"),
    IM("IM", "수입물대"),
    HR("HR", "인사전표");


    private final String code;

    private final String name;

    SlipTypeCd(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
