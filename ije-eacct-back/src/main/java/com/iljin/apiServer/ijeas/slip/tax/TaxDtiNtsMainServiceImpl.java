package com.iljin.apiServer.ijeas.slip.tax;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaxDtiNtsMainServiceImpl implements TaxDtiNtsMainService{

    private final TaxDtiNtsMainQdslRepository taxDtiNtsMainQdslRepository;

    @Override
    public List<TaxDtiNtsMainDto> getTaxDtiNtsMainList(SearchTaxDtiNtsDto searchDto) {
        return taxDtiNtsMainQdslRepository.getTaxDtiNtsMainList(searchDto);
    }

    @Override
    public List<TaxDtiNtsMainDto> getTaxRcvList(SearchTaxDtiNtsDto searchDto) {
        return taxDtiNtsMainQdslRepository.getTaxRcvList(searchDto);
    }
}
