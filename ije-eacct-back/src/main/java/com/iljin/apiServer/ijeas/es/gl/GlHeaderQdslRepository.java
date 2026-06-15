package com.iljin.apiServer.ijeas.es.gl;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpGlCodesDto;

import java.util.List;

public interface GlHeaderQdslRepository{

    List<ErpGlSlipDto> getErpGlSlipList(ErpSlipRequestDto erpSlipRequestDto);

    List<ErpGlSlipDto> getErpGlSlipDetail(ErpSlipRequestDto erpSlipRequestDto);

}
