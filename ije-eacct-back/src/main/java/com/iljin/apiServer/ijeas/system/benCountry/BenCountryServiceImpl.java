package com.iljin.apiServer.ijeas.system.benCountry;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BenCountryServiceImpl implements BenCountryService {

    private final BenCountryRepository benCountryRepository;

    private final BenCountryQdslRepository benCountryQdslRepository;

    @Override
    public List<BenCountryDto> getBenCountryList(String value) {
        return benCountryQdslRepository.getBenCountryList(value);
    }

}
