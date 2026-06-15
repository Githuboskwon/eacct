package com.iljin.apiServer.ijeas.slip.prepayment;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;

import java.util.List;

public interface PrePaymentApplyQdslRepository {

    List<ApplyPrepaysDto> getAdvancedList(ErpSlipRequestDto erpSlipRequestDto);

    List<PrepaymentApplyDto> getReimbursementList(ErpSlipRequestDto erpSlipRequestDto);

}
