package com.iljin.apiServer.ijeas.system.oil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OilPriceKey implements Serializable {
    private static final long serialVersionUID = 5596999969800268464L;

    String compCd;
    String baseYm;
    String oilKindCd;
}
