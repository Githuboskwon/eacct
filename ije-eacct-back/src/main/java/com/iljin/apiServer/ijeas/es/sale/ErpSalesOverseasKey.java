package com.iljin.apiServer.ijeas.es.sale;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ErpSalesOverseasKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039740L;

    String compCd;

    String slipNo;

    String slipType;

    public ErpSalesOverseasKey(String compCd, String slipNo, String slipType){
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
    }
}
