package com.iljin.apiServer.ijeas.sm.tax;

import java.util.List;

public interface TaxRepositoryCustom {
    List<TaxDto> getTaxCodeList(TaxDto taxDto);
}
