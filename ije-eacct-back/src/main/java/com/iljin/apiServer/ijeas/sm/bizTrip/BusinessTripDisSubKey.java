package com.iljin.apiServer.ijeas.sm.bizTrip;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessTripDisSubKey implements Serializable {

    private static final long serialVersionUID = 7816522236963109145L;
    String compCd;

    String standardYymm;

    String departureArrivalArea;

    @Builder
    public BusinessTripDisSubKey(String compCd, String standardYymm, String departureArrivalArea) {
        this.compCd = compCd;
        this.standardYymm = standardYymm;
        this.departureArrivalArea = departureArrivalArea;
    }

}
