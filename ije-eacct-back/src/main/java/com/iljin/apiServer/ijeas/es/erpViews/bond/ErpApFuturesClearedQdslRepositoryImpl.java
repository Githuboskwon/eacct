package com.iljin.apiServer.ijeas.es.erpViews.bond;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.es.erpViews.bond.QErpApFuturesCleared.erpApFuturesCleared;


@RequiredArgsConstructor
@Repository
public class ErpApFuturesClearedQdslRepositoryImpl implements ErpApFuturesClearedQdslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ErpApFuturesCleared> pullErpApFuturesClearedByeslipTransferBatchId(Integer orgId, String eslipTransferBatchId) {
        return queryFactory
                .selectFrom(erpApFuturesCleared)
                .where(erpApFuturesCleared.orgId.eq(orgId),
                        erpApFuturesCleared.eslipTransferBatchId.eq(eslipTransferBatchId))
                .fetch();
    }

}
