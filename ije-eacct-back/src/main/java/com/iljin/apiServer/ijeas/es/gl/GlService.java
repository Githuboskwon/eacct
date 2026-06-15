package com.iljin.apiServer.ijeas.es.gl;

import com.iljin.apiServer.ijeas.es.erpViews.ErpGlCodesDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpGlTermsDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;

import java.util.List;

public interface GlService {

    List<ErpGlTermsDto> getErpTermList(ErpSlipRequestDto erpSlipRequestDto);

    ErpGlTermsDto getErpTermGetDueDate(ErpGlTermsDto erpGlTermsDto) throws Exception;

    List<ErpGlCodesDto> getTaxIsuueTypeList(ErpSlipRequestDto erpSlipRequestDto);

    List<ErpGlCodesDto> getGlSlipTypeList(ErpGlSlipDto erpGlSlipDto);

}
