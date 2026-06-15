package com.iljin.apiServer.ijeas.approval;

import com.iljin.apiServer.core.util.Pair;
import lombok.Getter;

@Getter
public enum ApprovalType {
    TYPE_APPROVAL("01", "결재"),
    TYPE_SEAL("02", "검인");

    private final String code;

    private final String name;

    ApprovalType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    Pair<String, String> getCodeAndValue() {
        return new Pair<>(code, name);
    }
}
