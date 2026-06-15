package com.iljin.apiServer.ijeas.es.erpViews.fund;

import com.iljin.apiServer.ijeas.es.fund.ErpFundSlipDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;

import java.util.List;

public interface ErpTrTrxHeadersQdslRepository {

    List<ErpTrTrxHeadersDto> pullErpTrTransactions(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);

    List<ErpFundSlipDto> getErpFundSlipList(ErpSlipRequestDto erpSlipRequestDto);
}
