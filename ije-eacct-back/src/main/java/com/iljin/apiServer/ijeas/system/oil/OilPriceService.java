package com.iljin.apiServer.ijeas.system.oil;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OilPriceService {
    List<OilPriceDto> getOilPriceList(OilPriceDto oilPriceDto);

    @Modifying
    @Transactional
    ResponseEntity<String> saveOilPrice(List<OilPriceDto> oilPrices);

    @Modifying
    @Transactional
    ResponseEntity<String> deleteOilPrice(OilPriceDto oilPriceDto);

    @Transactional(readOnly = true)
    OilPriceDto getOilPriceSlip(OilPriceDto oilPriceDto);
}
