package com.iljin.apiServer.ijeas.es.erpViews.item;

import static com.iljin.apiServer.ijeas.es.item.QApPaymentsDt.apPaymentsDt;
import static com.iljin.apiServer.ijeas.es.erpViews.item.QErpApPaymentsHeader.erpApPaymentsHeader;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ErpApPaymentsHeaderQdslRepositoryImpl implements ErpApPaymentsHeaderQdslRepository{

    private final JPAQueryFactory queryFactory;
    @Override
    public List<ErpApPaymentsHeader> pullErpApPaymentsHeader(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto) {
        return queryFactory
            .selectFrom(erpApPaymentsHeader)
            .where(erpApPaymentsHeader.orgId.eq(Integer.parseInt(erpSlipRequestDto.getCompCd())),
                JPAExpressions.selectFrom(apPaymentsDt).where(apPaymentsDt.checkId.eq(
                    erpApPaymentsHeader.checkId)).notExists(),
                erpApPaymentsHeader.transferType.eq("P"),
                erpApPaymentsHeader.checkDate.loe(erpSlipRequestDto.getSearchTo()),
                erpApPaymentsHeader.checkDate.goe(erpSlipRequestDto.getSearchFrom())
                )
            .fetch();
    }

    @Override
    public List<ErpApPaymentsHeader> pullErpApPaymentsHeadersByeslipTransferBatchId(String orgId, Integer eslipTransferBatchId) {
        return queryFactory
                .selectFrom(erpApPaymentsHeader)
                .where(erpApPaymentsHeader.orgId.eq(Integer.parseInt(orgId)),
                        erpApPaymentsHeader.eslipTransferBatchId.eq(Long.valueOf(eslipTransferBatchId)))
                .fetch();
    }

}
