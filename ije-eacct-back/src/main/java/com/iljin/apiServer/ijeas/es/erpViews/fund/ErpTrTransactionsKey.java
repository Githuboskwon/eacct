package com.iljin.apiServer.ijeas.es.erpViews.fund;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ErpTrTransactionsKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039740L;

    Integer orgId;

    String xtrSlipType;

    Integer batchId;

    String dealNumber;

    String transactionNumber;

    String description;
}
