package com.iljin.apiServer.ijeas.es.erpViews;

import java.util.List;

public interface ErpViewPopService {

    List<ErpViewPopFundSlipDto> getPopDealType(String searchDate);

    List<ErpViewPopFundSlipDto> getPopProductType(String searchDate);

}
