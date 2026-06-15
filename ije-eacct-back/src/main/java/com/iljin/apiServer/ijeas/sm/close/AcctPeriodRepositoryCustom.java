package com.iljin.apiServer.ijeas.sm.close;

import java.util.List;

public interface AcctPeriodRepositoryCustom {
    List<AcctPeriodDto> getAcctPeriodList(AcctPeriodDto acctPeriodDto);

    List<AcctPeriodDto> getAcctPeriodOpenList(AcctPeriodDto acctPeriodDto);

    AcctPeriodDto getAcctPeriodOpenCloseDate();
}
