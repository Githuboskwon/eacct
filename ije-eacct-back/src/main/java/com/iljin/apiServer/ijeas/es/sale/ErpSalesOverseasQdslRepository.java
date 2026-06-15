package com.iljin.apiServer.ijeas.es.sale;

import com.iljin.apiServer.ijeas.es.erpViews.sale.ErpArTrxHeaders;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.es.erpViews.frgn.ErpSpOsSlip;

import java.util.List;

public interface ErpSalesOverseasQdslRepository {

    List<ErpArTrxHeaders> pullErpArTrxHeaders(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);

    List<ErpSpOsSlip> pullErpSpOsSlip(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);

    List<ErpArTrxHeaders> pullErpArTrxHeaders_export(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);

    List<ErpSalesOverseasDto> getErpSalesSlipList(ErpSlipRequestDto erpSlipRequestDto);

}
