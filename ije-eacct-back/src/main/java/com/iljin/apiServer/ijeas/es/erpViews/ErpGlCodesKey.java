package com.iljin.apiServer.ijeas.es.erpViews;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ErpGlCodesKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039749L;

    Integer codeTypeId;

    String codeType;

    String headerMeaning;

    String code;

    String lineMeaning;
}
