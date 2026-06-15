package com.iljin.apiServer.ijeas.es.item;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import java.util.List;

public interface ApPaymentsDtQdslRepository {

    List<ErpItemSlipDto> getErpItemSlipList(ErpSlipRequestDto erpSlipRequestDto);

    List<ErpItemSlipDto> getErpItemSlipDetail(ErpSlipRequestDto erpSlipRequestDto);

    List<ApPaymentsDtDto> pullErpApPaymentsDtList(String compCd, String eSlipTransferBatchId);

//    List<ApPaymentsDtDto> pullErpApFuturesDtList(String compCd, String eSlipTransferBatchId);
}
