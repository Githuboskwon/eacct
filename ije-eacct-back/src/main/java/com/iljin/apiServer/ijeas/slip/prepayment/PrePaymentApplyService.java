package com.iljin.apiServer.ijeas.slip.prepayment;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface PrePaymentApplyService {

    List<ApplyPrepaysDto> getAdvancedList(ErpSlipRequestDto erpSlipRequestDto);

    List<PrepaymentApplyDto> getReimbursementList(ErpSlipRequestDto erpSlipRequestDto);

    @Modifying
    @Transactional
    ResponseEntity<String> saveAdvanced(@RequestBody List<PrepaymentApplyDto> prepaymentApplyDto);

    @Modifying
    @Transactional
    ResponseEntity<String> deleteAdvanced(@RequestBody List<PrepaymentApplyDto> prepaymentApplyDto);
}
