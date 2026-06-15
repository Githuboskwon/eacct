package com.iljin.apiServer.ijeas.system.benCountry;

import java.util.List;

public interface BenCountryQdslRepository {

    List<BenCountryDto> getBenCountryList(String value);

}
