package com.iljin.apiServer.ijeas.es.erpViews.bond;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ErpApFuturesClearedKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039741L;

    Integer orgId;

    Integer checkId;
}
