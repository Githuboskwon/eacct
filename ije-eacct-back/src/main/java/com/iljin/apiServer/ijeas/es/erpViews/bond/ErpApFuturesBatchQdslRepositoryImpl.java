package com.iljin.apiServer.ijeas.es.erpViews.bond;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.es.erpViews.bond.QErpApFuturesBatch.erpApFuturesBatch;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;

@RequiredArgsConstructor
@Repository
public class ErpApFuturesBatchQdslRepositoryImpl implements ErpApFuturesBatchQdslRepository {

    private final JPAQueryFactory queryFactory;
    @Override
    public List<ErpApFuturesBatch> pullErpApFuturesBatch(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto) {
        return queryFactory
            .selectFrom(erpApFuturesBatch)
            .where(erpApFuturesBatch.orgId.eq(Integer.parseInt(erpSlipRequestDto.getCompCd())),
                    JPAExpressions.selectFrom(slipHeader).where(slipHeader.slipType.eq(slipTypeCd),
                            slipHeader.erpInvoiceId.eq(
                            Expressions.stringTemplate("TO_CHAR({0})", erpApFuturesBatch.eslipTransferBatchId)
                            )).notExists(),
                    erpApFuturesBatch.futurePayDueDate.loe(erpSlipRequestDto.getSearchTo()),
                    erpApFuturesBatch.futurePayDueDate.goe(erpSlipRequestDto.getSearchFrom())
//                erpApPaymentsBatch.transferredBy.eq(erpSlipRequestDto.getUserId()), // 추후 주석 해제
                )
            .fetch();
    }

}
