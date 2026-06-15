package com.iljin.apiServer.ijeas.system.acct;

import java.util.List;

public interface AccountService {

    public List<AccountDto> getAccountList(AccountDto accountDto);

    public List<AccountDto> getSlipAccountList(AccountDto accountDto);

    List<AccountDto> getSlipAccountSubList(AccountDto accountDto);

    List<AccountDto> getAccountPopList(AccountDto accountDto);

}
