package com.iljin.apiServer.ijeas.slip.erpSubmit;

import com.iljin.apiServer.ijeas.es.ErpSlipSubmitDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpGlTrxTypeHdDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ErpSubmitService {

    List<ErpSlipSubmitDto> getErpSlipSubmitList(ErpSlipSubmitDto erpSlipSubmitDto);

    List<ErpGlTrxTypeHdDto> getErpSlipSubmitDealTypeList(String search);

    @Modifying
    @Transactional
    ResponseEntity<String> slipTransferErp(List<ErpSlipSubmitDto> erpSlipSubmitDtos);

}
