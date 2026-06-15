package com.iljin.apiServer.ijeas.slip;

import lombok.Getter;

@Getter
public enum SlipStatusType {

    SV("SV", "저장됨"),
    VE("VE", "검증오류"),
    VC("VC", "검증됨"),
    AP("AP", "결재중"),
    AR("AR", "결재반려"),
    CP("CP", "검인중"),
    CR("CR", "검인반려"),
    CC("CC", "검인됨"),
    IE("IE", "이관오류"),
    IC("IC", "이관완료"),
    SD("SD", "전표삭제"),
    FP("FP", "검인중(ERP)"),
    FH("FH", "검인보류(ERP)"),
    FC("FC", "검인됨(ERP)"),
    FR("FR", "검인반려(ERP)"),
    SC("SC", "전표취소");

    private final String code;

    private final String name;

    SlipStatusType(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
