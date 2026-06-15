package com.iljin.apiServer.ijeas.slipCommon.exchange;

import com.iljin.apiServer.ijeas.es.erpViews.ErpExchangeRtDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSpCurrencies;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExchangeService {

    @Transactional(readOnly = true)
    List<ErpExchangeRtDto> getErpExchangeRate(String curCd,String excDt);

    @Transactional(readOnly = true)
    List<ErpSpCurrencies> getSpCurrentcieCode();

}
