package com.iljin.apiServer.ijeas.es.erpViews.bulk;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.es.erpViews.bulk.QErpApPaymentsBatch.erpApPaymentsBatch;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;

@RequiredArgsConstructor
@Repository
public class ErpApPaymentsBatchQdslRepositoryImpl implements ErpApPaymentsBatchQdslRepository{

    private final JPAQueryFactory queryFactory;
    @Override
    public List<ErpApPaymentsBatch> pullErpApPaymentsBatch(String slipTypeCd, @NotNull ErpSlipRequestDto erpSlipRequestDto) {
        return queryFactory
            .selectFrom(erpApPaymentsBatch)
            .where(
                    erpApPaymentsBatch.orgId.eq(Integer.parseInt(erpSlipRequestDto.getCompCd())) ,
                    JPAExpressions.selectFrom(slipHeader).where(slipHeader.slipType.eq(slipTypeCd),
                            slipHeader.erpInvoiceId.eq(
                            Expressions.stringTemplate("TO_CHAR({0})", erpApPaymentsBatch.eslipTransferBatchId)
                            )).notExists(),
                erpApPaymentsBatch.checkDate.loe(erpSlipRequestDto.getSearchTo()),
                erpApPaymentsBatch.checkDate.goe(erpSlipRequestDto.getSearchFrom())
                )
            .fetch();
    }

}
