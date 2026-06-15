package com.iljin.apiServer.ijeas.slipCommon.exchange;


import com.iljin.apiServer.ijeas.es.erpViews.ErpExchangeRtDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSpCurrencies;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ExchangeServiceImpl implements ExchangeService {

    private final ExchangeQdslRepository exchangeQdslRepository;


    @Override
    public List<ErpExchangeRtDto> getErpExchangeRate(String curCd, String excDt) {
        return exchangeQdslRepository.getErpExchangeRate(curCd, excDt);
    }


    @Override
    public List<ErpSpCurrencies> getSpCurrentcieCode() {
        return exchangeQdslRepository.getSpCurrentcieCode();
    }

}
