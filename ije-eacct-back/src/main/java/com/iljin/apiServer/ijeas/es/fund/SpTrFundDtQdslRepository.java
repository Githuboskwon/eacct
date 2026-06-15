package com.iljin.apiServer.ijeas.es.fund;

import java.util.List;

public interface SpTrFundDtQdslRepository {

    List<SpTrFundDt> pullErpSpTrFundDtList(String compCd , String slipNo);

}
