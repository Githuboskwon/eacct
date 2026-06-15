package com.iljin.apiServer.ijeas.system.oil;


import java.util.List;

public interface OilPriceQdslRepository {

    List<OilPriceDto> getOilPriceList(OilPriceDto oilPriceDto);

    List<OilPriceDto> getOilPriceSlip(OilPriceDto oilPriceDto);

}
