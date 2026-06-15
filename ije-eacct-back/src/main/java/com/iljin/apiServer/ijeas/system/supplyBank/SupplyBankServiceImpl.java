package com.iljin.apiServer.ijeas.system.supplyBank;


import com.iljin.apiServer.ijeas.es.erpViews.ErpGlTermsDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
public class SupplyBankServiceImpl implements SupplyBankService {
    private final SupplyQdslRepository supplyQdslRepository;


    @Override
    public List<SupplyBankDto> getSupplyBankList(String integrationVendorNum, String termId){
        ErpGlTermsDto erpGlTermsDto = supplyQdslRepository.getPaymentMethod(termId);
        String paymentMethod = erpGlTermsDto.getPaymentMethod();
        return supplyQdslRepository.getSupplyBankList(integrationVendorNum,paymentMethod);
    };


}
