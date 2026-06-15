package com.iljin.apiServer.ijeas.slip.etax;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CboArTrxMergeKey  implements Serializable {
    private static final long serialVersionUID = 250573003503536122L;

    BigDecimal etaxIssueId;
    BigDecimal customerTrxId;
}
