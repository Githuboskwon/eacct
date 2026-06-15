package com.iljin.apiServer.ijeas.ims.pjtLaborCost;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PjtLaborCostKey implements Serializable {
    private static final long serialVersionUID = -3590296339283933122L;

    String orgId;
    String projectMngNo;
    String positionCd;

}
