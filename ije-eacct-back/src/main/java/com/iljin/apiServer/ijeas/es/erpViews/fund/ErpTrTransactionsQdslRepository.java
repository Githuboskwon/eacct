package com.iljin.apiServer.ijeas.es.erpViews.fund;

import java.util.List;

public interface ErpTrTransactionsQdslRepository {


    List<ErpTrTransactionsDto> getErpTrTransactionsList(ErpTrTrxHeadersDto erpTrTrxHeadersDto);
}
