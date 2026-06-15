package com.iljin.apiServer.ijeas.slipCommon.exchange;

import com.iljin.apiServer.ijeas.es.erpViews.ErpExchangeRtDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSpCurrencies;

import java.util.List;

public interface ExchangeQdslRepository {

    List<ErpExchangeRtDto> getErpExchangeRate(String curCd, String excDt);

    List<ErpSpCurrencies> getSpCurrentcieCode();
}
