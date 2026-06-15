package com.iljin.apiServer.ijeas.slip.tax;

import java.util.List;

public interface TaxDtiNtsMainService {

    List<TaxDtiNtsMainDto> getTaxDtiNtsMainList(SearchTaxDtiNtsDto searchDto);

    List<TaxDtiNtsMainDto> getTaxRcvList(SearchTaxDtiNtsDto searchDto);
}
