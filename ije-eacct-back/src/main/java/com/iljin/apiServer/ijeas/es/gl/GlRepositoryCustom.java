package com.iljin.apiServer.ijeas.es.gl;

import com.iljin.apiServer.ijeas.es.erpViews.ErpGlCodesDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpGlTermsDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import java.util.List;

public interface GlRepositoryCustom {

    List<ErpGlTermsDto> getErpTermList(ErpSlipRequestDto erpSlipRequestDto);

    List<ErpGlCodesDto> getGlSlipTypeList(ErpGlSlipDto erpGlSlipDto);
}
