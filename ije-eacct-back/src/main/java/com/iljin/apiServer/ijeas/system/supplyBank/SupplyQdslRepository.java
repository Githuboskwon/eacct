package com.iljin.apiServer.ijeas.system.supplyBank;

import com.iljin.apiServer.ijeas.es.erpViews.ErpGlTermsDto;

import java.util.List;

public interface SupplyQdslRepository {
    List<SupplyBankDto> getSupplyBankList(String integrationVendorNum, String paymentMethod);

    ErpGlTermsDto getPaymentMethod(String termId);

}
