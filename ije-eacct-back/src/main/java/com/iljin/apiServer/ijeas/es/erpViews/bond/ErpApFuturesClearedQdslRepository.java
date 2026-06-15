package com.iljin.apiServer.ijeas.es.erpViews.bond;

import java.util.List;

public interface ErpApFuturesClearedQdslRepository {

    List<ErpApFuturesCleared> pullErpApFuturesClearedByeslipTransferBatchId(Integer orgId, String eslipTransferBatchId);
}
