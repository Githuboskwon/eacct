package com.iljin.apiServer.ijeas.es.collection;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;

import java.util.List;

public interface SpTrCollectionHdQdslRepository {

    List<ErpCollectionSlipDto> getErpCollectionSlipList(ErpSlipRequestDto erpSlipRequestDto);

    List<ErpCollectionSlipDto> getErpCollectionSlip(ErpSlipRequestDto erpSlipRequestDto);
}
