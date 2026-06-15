package com.iljin.apiServer.ijeas.es.erpViews.collection;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;

import java.util.List;

public interface ErpTrFundTrnHeadersQdslRepository {

    List<ErpTrFundTrnHeaders> pullErpTrFundTrnHeaders(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);
//
//    List<ErpFundSlipDto> getErpFundSlipList(ErpSlipRequestDto erpSlipRequestDto);
}
