package com.iljin.apiServer.ijeas.es.erpViews.fund;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ErpTrTrxHeadersKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039740L;

    String xtrSlipType;

    Integer orgId;

    Integer batchId;

    String dealNumber;

    String transactionNumber;

    String description;

//    LocalDateTime journalDate;
}
