package com.iljin.apiServer.ijeas.es.erpViews.item;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import java.math.BigDecimal;
import java.util.List;

public interface ErpApPaymentsLinesQdslRepository {

    List<ErpApPaymentsLines> pullErpApPaymentsLines(ErpSlipRequestDto erpSlipRequestDto);

    List<ErpApPaymentsLines> pullErpApPaymentsLinesByCheckId(Integer orgId, BigDecimal checkId);
}
