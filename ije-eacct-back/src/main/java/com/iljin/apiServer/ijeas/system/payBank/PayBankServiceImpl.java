package com.iljin.apiServer.ijeas.system.payBank;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayBankServiceImpl implements PayBankService{

    private final PayBankQdslRepository payBankQdslRepository;

    @Override
    public List<PayBankDto> getPayBankList(PayBankDto payBankDto) {
        return payBankQdslRepository.getPayBankList(payBankDto);
    }

}
