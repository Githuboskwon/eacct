package com.iljin.apiServer.ijeas.es.fund;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;

import java.util.List;

public interface SpTrFundHdQdslRepository {

    List<ErpFundSlipDto> getErpFundSlipDetail(ErpSlipRequestDto erpSlipRequestDto);

}
