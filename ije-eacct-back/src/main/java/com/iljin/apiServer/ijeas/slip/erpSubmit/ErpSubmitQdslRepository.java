package com.iljin.apiServer.ijeas.slip.erpSubmit;

import com.iljin.apiServer.ijeas.es.ErpSlipSubmitDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpGlTrxTypeHdDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipHeaderDto;

import java.util.List;

public interface ErpSubmitQdslRepository {

    List<ErpGlTrxTypeHdDto> getErpSlipSubmitDealTypeList(String search);

    List<ErpSlipHeaderDto> getErpSlipStatus(ErpSlipSubmitDto erpSlipSubmitDto);

    List<ErpSlipHeaderDto> getErpSlipFlag(ErpSlipSubmitDto erpSlipSubmitDto);

}
