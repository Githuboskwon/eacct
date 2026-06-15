package com.iljin.apiServer.ijeas.slip.detail;

import lombok.Getter;

@Getter
public enum TrafficTypeCd {

    TOLL("A", "통행료"),
    PARKING("B", "주차비"),
    FOOD("C", "식비"),
    ETC("D", "기타"),
    PUBLIC("E", "대중교통비");

    final private String code;

    final private String name;

    TrafficTypeCd(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
