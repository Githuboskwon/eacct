package com.iljin.apiServer.ijeas.slip.hr;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class PayrollUploadKey implements Serializable {
    private static final long serialVersionUID = -9157969609221037264L;

    Integer orgId;
    Integer ledgerId;
    Integer payrollBatchId;
    Integer payrollLineId;

    @Builder
    public PayrollUploadKey(Integer orgId, Integer ledgerId, Integer payrollBatchId, Integer payrollLineId){
        this.orgId = orgId;
        this.ledgerId = ledgerId;
        this.payrollBatchId = payrollBatchId;
        this.payrollLineId = payrollLineId;
    }
}
