package com.iljin.apiServer.ijeas.es.erpViews;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ErpGlTrxTypeEvidenceKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039749L;

    String trxTypeCode;

    String evidenceCode;

    String orgId;
}
