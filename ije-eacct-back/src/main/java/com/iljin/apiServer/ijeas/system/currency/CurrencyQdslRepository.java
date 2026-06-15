package com.iljin.apiServer.ijeas.system.currency;

import java.util.List;

public interface CurrencyQdslRepository {

    List<CurrencyDto> getCurrencyList(CurrencyDto currencyDto);

}
