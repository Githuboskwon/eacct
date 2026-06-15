package com.iljin.apiServer.ijeas.sm.tax;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaxService {

	List<Tax> getByEvdTypeCd(String evdTypeCd);

    List<TaxDto> getTaxCodeList(TaxDto taxDto);

    @Modifying
    @Transactional
    ResponseEntity<String> saveTaxCodes(List<TaxDto> taxDtos);

    @Modifying
    @Transactional
    ResponseEntity<String> deleteTaxCode(TaxDto taxDto);
}
