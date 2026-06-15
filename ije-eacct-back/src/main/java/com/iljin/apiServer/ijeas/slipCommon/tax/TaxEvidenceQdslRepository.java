package com.iljin.apiServer.ijeas.slipCommon.tax;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpTaxEvidenceCodeDto;

import java.util.List;

public interface TaxEvidenceQdslRepository {

    List<ErpTaxEvidenceCodeDto> getErpTaxEvidenceCode(ErpSlipRequestDto erpSlipRequestDto,String trxTypeCode);

}
