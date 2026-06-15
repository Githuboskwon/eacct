package com.iljin.apiServer.ijeas.system.expend;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SlipExpendKey implements Serializable {
    private static final long serialVersionUID = 5596999969800268464L;

    String compCd;
    String slipNo;
    BigDecimal slipHeaderId;
}
