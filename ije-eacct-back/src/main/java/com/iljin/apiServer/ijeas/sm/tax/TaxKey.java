package com.iljin.apiServer.ijeas.sm.tax;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaxKey implements Serializable {
    private static final long serialVersionUID = -2428964185141436619L;

    String compCd;
    String taxCd;
}
