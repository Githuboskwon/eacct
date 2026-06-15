package com.iljin.apiServer.ijeas.es.bulk;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;

import java.util.List;

public interface ApPaymentsHdQdslRepository {

    List<ErpBulkSlipDto> getErpBulkSlipList(ErpSlipRequestDto erpSlipRequestDto);

    List<ErpBulkSlipDto> getErpBulkSlipDetail(ErpSlipRequestDto erpSlipRequestDto);

    List<ErpBondSlipDto> getErpBondSlipList(ErpSlipRequestDto erpSlipRequestDto);
}
