package com.iljin.apiServer.ijeas.es.erpViews.gl;

import java.math.BigDecimal;
import java.util.List;

public interface ErpGlLinesQdslRepository {

    List<ErpGlLines> getErpGlLinesByJeHeaderId(BigDecimal jeHeaderId);

}
