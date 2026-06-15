package com.iljin.apiServer.ijeas.es.erpViews.item;

import static com.iljin.apiServer.ijeas.es.item.QApPaymentsItem.apPaymentsItem;
import static com.iljin.apiServer.ijeas.es.erpViews.item.QErpApPaymentsHeader.erpApPaymentsHeader;
import static com.iljin.apiServer.ijeas.es.erpViews.item.QErpApPaymentsLines.erpApPaymentsLines;
import static java.util.Objects.nonNull;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ErpApPaymentsLinesQdslRepositoryImpl implements ErpApPaymentsLinesQdslRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ErpApPaymentsLines> pullErpApPaymentsLines(ErpSlipRequestDto erpSlipRequestDto) {
        return queryFactory
            .selectFrom(erpApPaymentsLines)
            .where(JPAExpressions.selectFrom(apPaymentsItem)
                .where(apPaymentsItem.checkId.eq(erpApPaymentsLines.checkId)).notExists(),
                JPAExpressions.selectFrom(apPaymentsItem).where(apPaymentsItem.checkId.eq(
                    erpApPaymentsLines.checkId)).notExists()
                )
            .leftJoin(erpApPaymentsHeader).on(erpApPaymentsHeader.checkId.eq(erpApPaymentsLines.checkId))
            .on(checkDtFromGoe(erpSlipRequestDto.getSearchFrom()),
                checkDtToLoe(erpSlipRequestDto.getSearchTo()))
            .fetch();
    }

    @Override
    public List<ErpApPaymentsLines> pullErpApPaymentsLinesByCheckId(Integer orgId, BigDecimal checkId) {
        return queryFactory
            .selectFrom(erpApPaymentsLines)
            .where(erpApPaymentsLines.orgId.eq(orgId),
                erpApPaymentsLines.checkId.eq(checkId))
            .fetch();
    }

    private BooleanExpression checkDtToLoe(LocalDateTime checkDtTo) {
        return nonNull(checkDtTo) ? erpApPaymentsHeader.checkDate.loe(checkDtTo) : null;
    }

    private BooleanExpression checkDtFromGoe(LocalDateTime checkDtFrom) {
        return nonNull(checkDtFrom) ? erpApPaymentsHeader.checkDate.goe(checkDtFrom) : null;
    }

}
