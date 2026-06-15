package com.iljin.apiServer.ijeas.es.erpViews.bond;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;

import java.util.List;

public interface ErpApFuturesBatchQdslRepository {

    List<ErpApFuturesBatch> pullErpApFuturesBatch(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);
}
