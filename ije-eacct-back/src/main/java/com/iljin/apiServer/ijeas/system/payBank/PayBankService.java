package com.iljin.apiServer.ijeas.system.payBank;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface PayBankService {

    @Transactional(readOnly = true)
    List<PayBankDto> getPayBankList(PayBankDto payBankDto);

}
