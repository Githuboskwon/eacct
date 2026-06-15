package com.iljin.apiServer.ijeas.slipCommon.tax;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpTaxEvidenceCodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaxEvidenceServiceImpl implements TaxEvidenceService {

    private final TaxEvidenceQdslRepository taxEvidenceQdslRepository;

    @Override
    public List<ErpTaxEvidenceCodeDto> getErpTaxEvidenceCode(ErpSlipRequestDto erpSlipRequestDto, String trxTypeCode) {
        return taxEvidenceQdslRepository.getErpTaxEvidenceCode(erpSlipRequestDto,trxTypeCode);
    }

}
