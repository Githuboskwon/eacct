package com.iljin.apiServer.ijeas.system.payBank;

import java.util.List;

public interface PayBankQdslRepository {

    List<PayBankDto> getPayBankList(PayBankDto payBankDto);

}
