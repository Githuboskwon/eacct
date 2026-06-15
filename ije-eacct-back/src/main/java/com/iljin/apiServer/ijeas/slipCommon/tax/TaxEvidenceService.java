package com.iljin.apiServer.ijeas.slipCommon.tax;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpTaxEvidenceCodeDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaxEvidenceService {

    @Transactional(readOnly = true)
    List<ErpTaxEvidenceCodeDto> getErpTaxEvidenceCode(ErpSlipRequestDto erpSlipRequestDto, String trxTypeCode);

}
