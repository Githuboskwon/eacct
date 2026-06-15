package com.iljin.apiServer.ijeas.system.trx;

import java.util.List;

public interface TrxService {

    List<TrxDto> getTrxList(TrxDto trxDto);

    List<TrxDto> getSlipTrxList(TrxDto trxDto);

    List<TrxDto> getAwtInfo(TrxDto trxDto);
}
