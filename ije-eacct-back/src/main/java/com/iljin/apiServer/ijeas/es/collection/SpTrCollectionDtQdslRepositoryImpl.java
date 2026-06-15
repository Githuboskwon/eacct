package com.iljin.apiServer.ijeas.es.collection;

import com.iljin.apiServer.ijeas.es.erpViews.collection.ErpTrFundTrn;
import com.iljin.apiServer.ijeas.es.erpViews.collection.ErpTrFundTrnHeaders;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.es.collection.QSpTrCollectionDt.spTrCollectionDt;
import static com.iljin.apiServer.ijeas.es.erpViews.collection.QErpTrFundTrn.erpTrFundTrn;

@RequiredArgsConstructor
@Repository
public class SpTrCollectionDtQdslRepositoryImpl implements SpTrCollectionDtQdslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ErpTrFundTrn> pullErpSpTrCollectionDtList(ErpTrFundTrnHeaders erpTrFundTrnHeaders) {
        return queryFactory
        .selectFrom(erpTrFundTrn)
        .where(erpTrFundTrn.orgId.eq(erpTrFundTrnHeaders.getOrgId()),
               erpTrFundTrn.xtrSlipType.eq(erpTrFundTrnHeaders.getXtrSlipType()),
               erpTrFundTrn.glDate.eq(erpTrFundTrnHeaders.getGlDate())
        )
        .fetch();
    }


    @Override
    public List<SpTrCollectionDt> erpSpTrCollectionDtList(String compCd , String slipNo) {
        return queryFactory
                .selectFrom(spTrCollectionDt)
                .where(spTrCollectionDt.compCd.eq(compCd), spTrCollectionDt.slipNo.eq(slipNo)
                )
                .fetch();
    }

}
