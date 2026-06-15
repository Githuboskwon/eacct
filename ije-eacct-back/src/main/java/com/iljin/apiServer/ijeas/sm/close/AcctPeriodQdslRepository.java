package com.iljin.apiServer.ijeas.sm.close;

import java.util.List;

public interface AcctPeriodQdslRepository {

    List<AcctPeriodDto> getCheckAcctPeriodList(String compCd, String postingDt);

}
