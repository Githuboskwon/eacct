package com.iljin.apiServer.ijeas.slip.erpSubmit;

import com.iljin.apiServer.ijeas.es.ErpSlipSubmitDto;

import java.util.List;

public interface ErpSubmitRepositoryCustom {

    List<ErpSlipSubmitDto> getErpSlipSubmitList(ErpSlipSubmitDto erpSlipSubmitDto);
}
