package com.iljin.apiServer.ijeas.sm.bizTrip;

import static com.iljin.apiServer.ijeas.sm.bizTrip.QBusinessTripDisSub.businessTripDisSub;
import static com.iljin.apiServer.ijeas.system.code.QCodeDetail.codeDetail;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static org.springframework.util.StringUtils.hasText;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BusinessTripDisQdslRepositoryImpl implements BusinessTripDisQdslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BusinessTripDisDto> getBusinessTripDisList(BusinessTripDisDto businessTripDisDto) {
        return queryFactory
            .select(new QBusinessTripDisDto(
                businessTripDisSub.compCd,
                businessTripDisSub.standardYymm,
                businessTripDisSub.departureArrivalArea,
                businessTripDisSub.distance,
                businessTripDisSub.remark,
                businessTripDisSub.useYn,
                businessTripDisSub.chgId,
                employee.empNm,
                businessTripDisSub.chgDtm
            ))
            .from(businessTripDisSub)
            .leftJoin(employee).on(employee.compCd.eq(businessTripDisSub.compCd), employee.empNo.eq(businessTripDisSub.chgId))
            .where(businessTripDisSub.rowNum.eq(1L),
                compCdEq(businessTripDisDto.getCompCd()),
                standardYymmLoe(businessTripDisDto.getStandardYymm()),
                departureArrivalAreaEq(businessTripDisDto.getDepartureArrivalArea()),
                useYnEq("Y"))
            .orderBy(businessTripDisSub.departureArrivalArea.asc())
            .fetch();
    }

    @Override
    public List<BusinessTripDisDto> getBusinessTripDisSlip(BusinessTripDisDto businessTripDisDto) {
        return queryFactory
                .select(new QBusinessTripDisDto(
                        businessTripDisSub.compCd,
                        businessTripDisSub.standardYymm,
                        businessTripDisSub.departureArrivalArea,
                        businessTripDisSub.distance,
                        businessTripDisSub.remark,
                        businessTripDisSub.useYn,
                        ExpressionUtils.as(JPAExpressions.select(codeDetail.detailNm)
                                        .from(codeDetail)
                                        .where(codeDetail.groupCd.eq("BUSINESS_TRIP_AREA_CD")
                                                .and(codeDetail.compCd.eq(businessTripDisSub.compCd))
                                                .and(codeDetail.detailCd.eq(businessTripDisSub.departureArrivalArea)))
                         , "search")
                ))
                .from(businessTripDisSub)
                .where(businessTripDisSub.rowNum.eq(1L)
                      .and(compCdEq(businessTripDisDto.compCd))
                      .and(standardYymmLoe(businessTripDisDto.standardYymm))
                      .and(useYnEq("Y"))
                      .and(businessTripDisSub.departureArrivalArea.in(JPAExpressions.select(codeDetail.detailCd)
                                                                          .from(codeDetail)
                                                                          .where(codeDetail.groupCd.eq("BUSINESS_TRIP_AREA_CD")
                                                                                  .and(codeDetail.compCd.eq(businessTripDisDto.compCd))
                                                                                  .and(codeDetail.detailNm.containsIgnoreCase(businessTripDisDto.search)))
                                                                      )
                      )
                )
                .orderBy(businessTripDisSub.departureArrivalArea.asc())
                .fetch();
    }

    private BooleanExpression compCdEq(String compCd) {
        return hasText(compCd) ? businessTripDisSub.compCd.eq(compCd) : null;
    }

    private BooleanExpression standardYymmEq(String standardyymm) {
        return hasText(standardyymm) ? businessTripDisSub.standardYymm.eq(standardyymm) : null;
    }

    private BooleanExpression standardYymmLoe(String standardyymm) {
        return hasText(standardyymm) ? businessTripDisSub.standardYymm.loe(standardyymm) : null;
    }

    private BooleanExpression departureArrivalAreaEq(String departureArrivalArea) {
        return hasText(departureArrivalArea) ? businessTripDisSub.departureArrivalArea.eq(departureArrivalArea) : null;
    }

    private BooleanExpression useYnEq(String useYn) {
        return hasText(useYn) ? businessTripDisSub.useYn.eq(useYn) : null;
    }
}
