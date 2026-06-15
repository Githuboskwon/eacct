package com.iljin.apiServer.ijeas.system.oil;


import static com.iljin.apiServer.ijeas.system.code.QCodeDetail.codeDetail;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static com.iljin.apiServer.ijeas.system.oil.QOilPrice.oilPrice;
import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.core.util.Util;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Repository
public class OilPriceQdslRepositoryImpl implements OilPriceQdslRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<OilPriceDto> getOilPriceList(OilPriceDto oilPriceDto) {

        return queryFactory
                .select(new QOilPriceDto(
                        oilPrice.compCd,
                        oilPrice.baseYm,
                        oilPrice.oilKindCd,
                        codeDetail.detailCd.as("oilKindNm"),
                        oilPrice.oilUpce,
                        oilPrice.oilEff,
                        oilPrice.regId,
                        oilPrice.regDtm,
                        oilPrice.chgId,
                        employee.empNm.as("chgNm"),
                        oilPrice.chgDtm,
                        oilPrice.remark
                ))
                .from(oilPrice)
                .leftJoin(codeDetail).on(codeDetail.compCd.eq(oilPrice.compCd))
                .leftJoin(employee).on(employee.empNo.eq(oilPrice.chgId),employee.compCd.eq(oilPrice.compCd))
                .where(oilPrice.compCd.eq(oilPriceDto.getCompCd())
                        ,codeDetail.useYn.eq("Y")
                        ,codeDetail.groupCd.eq("OIL_KIND_CD")
                        ,codeDetail.detailCd.eq(oilPrice.oilKindCd)
                        ,baseYmLk(oilPriceDto.getBaseYm())
                        ,oilKindCdEq(oilPriceDto.getOilKindCd())
                )
                .fetch();
    }

    @Override
    public List<OilPriceDto> getOilPriceSlip(OilPriceDto oilPriceDto) {
        return queryFactory
                .select(new QOilPriceDto(
                        oilPrice.compCd,
                        oilPrice.baseYm,
                        oilPrice.oilKindCd,
                        ExpressionUtils.as(JPAExpressions.select(codeDetail.detailNm)
                                .from(codeDetail)
                                .where(codeDetail.groupCd.eq("OIL_KIND_CD")
                                        .and(codeDetail.compCd.eq(oilPrice.compCd))
                                        .and(codeDetail.detailCd.eq(oilPrice.oilKindCd))), "oilKindNm"),
                        oilPrice.oilUpce,
                        oilPrice.oilEff,
                        oilPrice.remark
                ))
                .from(oilPrice)
                .where(oilPrice.compCd.eq(oilPriceDto.getCompCd()),
                        oilPrice.baseYm.eq(oilPriceDto.getBaseYm()),
                        oilPrice.oilKindCd.eq(oilPriceDto.getOilKindCd())
                )
                .orderBy(oilPrice.oilKindCd.asc())
                .fetch();
    }

    private BooleanExpression baseYmLk (String baseYm) {
        return hasText(baseYm) ? oilPrice.baseYm.likeIgnoreCase("%" + baseYm + "%") : null;
    }

    private BooleanExpression oilKindCdEq(String oilKindCd) {
        return hasText(oilKindCd) ? oilPrice.oilKindCd.eq(oilKindCd) : null;
    }



}
