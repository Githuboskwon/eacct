package com.iljin.apiServer.ijeas.es.erpViews.collection;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ErpTrFundTrnHeadersKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039740L;

    Integer orgId;

    String xtrSlipType;

    LocalDateTime glDate;
}
