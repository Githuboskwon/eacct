package com.iljin.apiServer.ijeas.es.erpViews.bulk;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;

import java.util.List;

public interface ErpApPaymentsBatchQdslRepository {

    List<ErpApPaymentsBatch> pullErpApPaymentsBatch(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);
}
