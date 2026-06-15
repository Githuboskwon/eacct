package com.iljin.apiServer.ijeas.sm.close;

import lombok.Data;

import java.io.Serializable;

@Data
public class AcctPeriodKey implements Serializable {
    private static final long serialVersionUID = -5862434088859750106L;

    String compCd;
    String closYm;
    String baCd;
}
