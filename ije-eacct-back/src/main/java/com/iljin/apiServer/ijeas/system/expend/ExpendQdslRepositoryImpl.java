package com.iljin.apiServer.ijeas.system.expend;


import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.system.code.CodeDto;
import com.iljin.apiServer.ijeas.system.code.QCodeDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.es.erpViews.QErpSlipHeader.erpSlipHeader;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;
import static com.iljin.apiServer.ijeas.system.code.QCodeDetail.codeDetail;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static com.iljin.apiServer.ijeas.system.expend.QExpend.expend;
import static com.iljin.apiServer.ijeas.system.expend.QSlipExpend.slipExpend;
import static com.iljin.apiServer.ijeas.system.vendor.QVendor.vendor;
import static com.querydsl.core.types.ExpressionUtils.count;
import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.system.code.CodeDto;
import com.iljin.apiServer.ijeas.system.code.QCodeDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ExpendQdslRepositoryImpl implements ExpendQdslRepository {

    private final JPAQueryFactory queryFactory;

    private final Util util;


    @Override
    public List<ExpendDto> getExpendList(ExpendDto expendDto) {

        return queryFactory
                .select(new QExpendDto(
                        expend.compCd,
                        expend.expendCd,
                        expend.startDate,
                        expend.endDate,
                        expend.wreathYn,
                        expend.mutualYn,
                        expend.holiday,
                        expend.expendAmt,
                        expend.remark,
                        expend.regId,
                        expend.regDtm,
                        expend.chgId,
                        employee.empNm.as("chgNm"),
                        expend.chgDtm
                ))
                .from(expend)
                .leftJoin(employee).on(employee.empNo.eq(expend.chgId),employee.compCd.eq(expend.compCd))
                .where(expend.compCd.eq(expendDto.getCompCd())
                ,expend.startDate.loe(expendDto.getBasicDate())
                ,expend.endDate.goe(expendDto.getBasicDate())
                ,expendCdEq(expendDto.getExpendCd())
                )
                .orderBy(expend.expendCd.asc())
                .fetch();
    }


    @Override
    public List<CodeDto> getExpendCdList(String value) {
        String compCd = util.getLoginCompCd();

        return queryFactory
                .select(new QCodeDto(
                        codeDetail.detailCd,
                        codeDetail.detailNm,
                        codeDetail.remark1,
                        codeDetail.remark2,
                        codeDetail.remark3,
                        codeDetail.remark4,
                        codeDetail.remark5))
                .from(codeDetail)
                .where(codeDetail.compCd.eq(compCd),
                        codeDetail.groupCd.eq("EXPEND_MONEY_CD"),
                        codeDetail.useYn.eq("Y"),
                        detailNmLk(value))
                .orderBy(codeDetail.orderSeq.asc())
                .fetch();
    }


    @Override
    public List<SlipExpendDto> getHrExpendList(SlipExpendDto slipExpendDto) {
        String compCd = util.getLoginCompCd();
//        String erpSlipHeaderOrgId = String.valueOf(erpSlipHeader.orgId);

        return queryFactory
                .select(new QSlipExpendDto(
                        slipExpend.compCd,
                        slipExpend.slipNo,
                        slipExpend.slipHeaderId,
                        slipExpend.expendCd,
                        slipExpend.expendReceiveNm,
                        slipExpend.expendRelation,
                        slipExpend.expendDt,
                        slipExpend.expendAmt,
                        slipExpend.wreathYn,
                        slipExpend.mutualYn,
                        slipExpend.holiday,
                        vendor.integrationVendorNum,
                        vendor.integrationVendorName,
                        slipHeader.postingDt,
                        slipExpend.regId,
                        employee.empNm.as("chgNm"),
                        slipExpend.regDtm
                ))
                .from(slipExpend)
                .leftJoin(employee).on(employee.empNo.eq(slipExpend.regId),employee.compCd.eq(slipExpend.compCd))
                .leftJoin(slipHeader).on(slipHeader.slipHeaderId.eq(slipExpend.slipHeaderId),slipHeader.compCd.eq(slipExpend.compCd))
                .leftJoin(erpSlipHeader).on(erpSlipHeader.slipHeaderId.eq(slipHeader.slipHeaderId),slipHeader.compCd.eq(Expressions.stringTemplate("TO_CHAR({0})", erpSlipHeader.orgId)))
                .leftJoin(vendor).on(vendor.integrationVendorNum.eq(erpSlipHeader.integrationVendorNum),vendor.compCd.eq(Expressions.stringTemplate("TO_CHAR({0})", erpSlipHeader.orgId)))
                .where(slipExpend.compCd.eq(compCd)
                ,slipHeader.postingDt.goe(slipExpendDto.getStartDate())
                ,slipHeader.postingDt.loe(slipExpendDto.getEndDate())
                ,userIdEq(slipExpendDto.getEmpNo())
                ,integrationVendorNumEq(slipExpendDto.getIntegrationVendorNum())
                )
                .fetch();
    }

    @Override
    public List<ExpendDto> getSlipExpendList(ErpSlipRequestDto erpSlipRequestDto) {
        String compCd = util.getLoginCompCd();
        return queryFactory
                .select(new QExpendDto(
                        codeDetail.remark2,
                        codeDetail.remark3,
                        codeDetail.remark5,
                        expend.wreathYn,
                        expend.mutualYn,
                        expend.expendAmt,
                        expend.expendCd,
                        expend.holiday
                ))
                .from(expend)
                .leftJoin(codeDetail).on(codeDetail.compCd.eq(expend.compCd) , codeDetail.detailCd.eq(expend.expendCd))
                .where(expend.compCd.eq(compCd),
                        expend.startDate.loe(erpSlipRequestDto.getSearchDate()),
                        expend.endDate.goe(erpSlipRequestDto.getSearchDate()),
                        codeDetail.groupCd.eq("EXPEND_MONEY_CD")
                )
                .orderBy(expend.expendCd.asc())
                .fetch();
    }

    @Override
    public SlipExpendDto getSlipExpend(String slipNo){
        String compCd = util.getLoginCompCd();

        return queryFactory
                .select(new QSlipExpendDto(
                        slipExpend.compCd,
                        slipExpend.slipNo,
                        slipExpend.slipHeaderId,
                        slipExpend.expendCd,
                        ExpressionUtils.as(JPAExpressions.select(codeDetail.detailNm)
                                        .from(codeDetail)
                                        .where(codeDetail.groupCd.eq("EXPEND_MONEY_CD")
                                                .and(codeDetail.compCd.eq(compCd))
                                                .and(codeDetail.detailCd.eq(slipExpend.expendCd))),
                                "expendNm"),
                        slipExpend.expendReceiveNm,
                        slipExpend.expendRelation,
                        slipExpend.expendDt,
                        slipExpend.expendAmt,
                        slipExpend.wreathYn,
                        slipExpend.mutualYn,
                        slipExpend.holiday
                ))
                .from(slipExpend)
                .where(slipExpend.compCd.eq(compCd)
                        .and(slipExpend.slipNo.eq(slipNo))
                )
                .fetchFirst();
    }


    private BooleanExpression expendCdEq(String expendCd) {
        return hasText(expendCd) ? expend.expendCd.eq(expendCd) : null;
    }

    private BooleanExpression detailNmLk (String detailNm) {
        return hasText(detailNm) ? codeDetail.detailNm.likeIgnoreCase("%" + detailNm + "%") : null;
    }

    private BooleanExpression userIdEq(String chgId) {
        return hasText(chgId) ? slipExpend.chgId.eq(chgId) : null;
    }

    private BooleanExpression integrationVendorNumEq(String integrationVendorNum) {
        return hasText(integrationVendorNum) ? vendor.integrationVendorNum.eq(integrationVendorNum) : null;
    }
}
