package com.iljin.apiServer.ijeas.system.currency;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface CurrencyService {

    @Transactional(readOnly = true)
    List<CurrencyDto> getCurrencyList(CurrencyDto currencyDto);

}
