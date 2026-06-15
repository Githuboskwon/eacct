package com.iljin.apiServer.ijeas.es.erpViews.bulk;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ErpApPaymentsBatchKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039749L;

    Integer orgId;

    Integer eslipTransferBatchId;
}
