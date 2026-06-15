package com.iljin.apiServer.ijeas.es.erpViews.item;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import java.util.List;

public interface ErpApPaymentsHeaderQdslRepository {

    List<ErpApPaymentsHeader> pullErpApPaymentsHeader(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);

    List<ErpApPaymentsHeader> pullErpApPaymentsHeadersByeslipTransferBatchId(String orgId, Integer eslipTransferBatchId);
}
