package com.iljin.apiServer.ijeas.es.erpViews.gl;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import java.util.List;

public interface ErpGlHeadersQdslRepository {

    List<ErpGlHeaders> pullErpGlHeaders(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);

}
