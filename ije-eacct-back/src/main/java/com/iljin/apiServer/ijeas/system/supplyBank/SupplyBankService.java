package com.iljin.apiServer.ijeas.system.supplyBank;

import java.util.List;

public interface SupplyBankService {
    List<SupplyBankDto> getSupplyBankList(String integrationVendorNum,String termId);

}
