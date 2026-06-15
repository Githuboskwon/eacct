package com.iljin.apiServer.ijeas.sm.bizTrip;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class BusinessTripDisKey implements Serializable {

    private static final long serialVersionUID = 3973090930745991200L;
    String compCd;

    String standardYymm;

    String departureArrivalArea;

    @Builder
    public BusinessTripDisKey(String compCd, String standardYymm, String departureArrivalArea) {
        this.compCd = compCd;
        this.standardYymm = standardYymm;
        this.departureArrivalArea = departureArrivalArea;
    }
}
