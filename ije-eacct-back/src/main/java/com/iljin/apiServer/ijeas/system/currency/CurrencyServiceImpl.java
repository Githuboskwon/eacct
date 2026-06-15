package com.iljin.apiServer.ijeas.system.currency;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyQdslRepository currencyQdslRepository;

    @Transactional(readOnly = true)
    public List<CurrencyDto> getCurrencyList(CurrencyDto currencyDto) {
        return currencyQdslRepository.getCurrencyList(currencyDto);
    }

}
