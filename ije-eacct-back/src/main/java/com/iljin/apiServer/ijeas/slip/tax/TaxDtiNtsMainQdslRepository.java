package com.iljin.apiServer.ijeas.slip.tax;

import java.util.List;

public interface TaxDtiNtsMainQdslRepository {

    List<TaxDtiNtsMainDto> getTaxDtiNtsMainList(SearchTaxDtiNtsDto searchDto);

    List<TaxDtiNtsMainDto> getTaxRcvList(SearchTaxDtiNtsDto searchDto);

    List<TaxDtiNtsMainDto> getCountUntrEtaxList(String compCd, String previousDate, String nextDate);
}
