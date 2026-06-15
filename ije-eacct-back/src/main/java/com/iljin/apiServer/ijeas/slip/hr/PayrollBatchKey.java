package com.iljin.apiServer.ijeas.slip.hr;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class PayrollBatchKey implements Serializable {

    private static final long serialVersionUID = -3637525703174353994L;

    Integer orgId;
    Integer ledgerId;
    Integer payrollBatchId;

    @Builder
    public PayrollBatchKey(Integer orgId, Integer ledgerId, Integer payrollBatchId){
        this.orgId = orgId;
        this.ledgerId = ledgerId;
        this.payrollBatchId = payrollBatchId;
    }
}
