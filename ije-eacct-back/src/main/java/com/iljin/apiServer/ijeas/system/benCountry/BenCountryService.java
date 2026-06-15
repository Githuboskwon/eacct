package com.iljin.apiServer.ijeas.system.benCountry;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface BenCountryService {
    @Transactional(readOnly = true)
    List<BenCountryDto> getBenCountryList(String value);


}
