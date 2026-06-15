package com.iljin.apiServer.ijeas.es.collection;

import com.iljin.apiServer.ijeas.es.erpViews.collection.ErpTrFundTrn;
import com.iljin.apiServer.ijeas.es.erpViews.collection.ErpTrFundTrnHeaders;

import java.util.List;

public interface SpTrCollectionDtQdslRepository {

    List<ErpTrFundTrn> pullErpSpTrCollectionDtList(ErpTrFundTrnHeaders erpTrFundTrnHeaders);

    List<SpTrCollectionDt> erpSpTrCollectionDtList(String compCd , String slipNo);
}
