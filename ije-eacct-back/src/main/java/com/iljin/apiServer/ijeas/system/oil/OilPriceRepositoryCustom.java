package com.iljin.apiServer.ijeas.system.oil;

import java.util.List;

public interface OilPriceRepositoryCustom {
    List<OilPriceDto> getOilPriceList(OilPriceDto oilPriceDto);
}
