package com.iljin.apiServer.ijeas.es.gl;

import com.iljin.apiServer.ijeas.es.erpViews.ErpGlTermsDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpGlCodesDto;

import java.util.List;

public interface GlQdslRepository {
    List<ErpGlTermsDto> getErpTermList(ErpSlipRequestDto erpSlipRequestDto);

    List<ErpGlCodesDto> getTaxIsuueTypeList(ErpSlipRequestDto erpSlipRequestDto);

    Long getTermsDateCnt(ErpGlTermsDto erpSlipRequestDto);

    String getTermsChangeFlag(ErpGlTermsDto erpSlipRequestDto);
}
