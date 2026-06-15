package com.iljin.apiServer.ijeas.slip.surtax;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;


import java.util.List;

public interface SurTaxCodeQRepository {
    List<SurTaxCodeDto> getSurTaxCodeList(ErpSlipRequestDto erpSlipRequestDto,String taxEvidenceType,String returnType);

}