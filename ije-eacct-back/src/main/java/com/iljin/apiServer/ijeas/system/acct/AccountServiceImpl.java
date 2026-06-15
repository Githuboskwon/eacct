package com.iljin.apiServer.ijeas.system.acct;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService{
    private final AccountQdslRepository accountQdslRepository;

    @Override
    public List<AccountDto> getAccountList(AccountDto accountDto){

        return accountQdslRepository.getAccountList(accountDto);
    }
    @Override
    public List<AccountDto> getSlipAccountList(AccountDto accountDto){

        if(hasText(accountDto.attribute3) && accountDto.attribute3.equals("form")){
            //법인카드 사용내역에서 호출 (attribute3 - form)
            accountDto.attribute1 = "법인카드";
            List<AccountDto> list = getSlipAccountSubList(accountDto);
            if(list.size() == 1){
                accountDto.trxTypeCd = list.get(0).childTrxTypeCd;
                return accountQdslRepository.getSlipAccountList(accountDto);
            }
        }

        //일반 라인에서 호출 (attribute3 - grid)
        return accountQdslRepository.getSlipAccountList(accountDto);
    }

    @Override
    public List<AccountDto> getSlipAccountSubList(AccountDto accountDto){

        return accountQdslRepository.getSlipAccountSubList(accountDto);
    }

    @Override
    public List<AccountDto> getAccountPopList(AccountDto accountDto){

        return accountQdslRepository.getAccountPopList(accountDto);
    }
}
