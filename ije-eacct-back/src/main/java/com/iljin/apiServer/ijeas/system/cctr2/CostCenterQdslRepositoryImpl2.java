package com.iljin.apiServer.ijeas.system.cctr2;


import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.system.delegate.DelegateDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.system.cctr2.QCboApEmpSegment2.cboApEmpSegment2;
import static com.iljin.apiServer.ijeas.system.cctr2.QCostCenter2.costCenter2;
import static com.iljin.apiServer.ijeas.system.delegate.QDelegate.delegate;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class CostCenterQdslRepositoryImpl2 implements CostCenterQdslRepository2 {

    private final JPAQueryFactory queryFactory;

    private final Util util;

    private enum EntityType {
        MST_CCTR2
    }

    @Override
    public List<CostCenterDto2> getByCctrCdOrCctrNmContaining(String compCd, String value) {

        return queryFactory
                .select(new QCostCenterDto2(
                        costCenter2.compCd,
                        costCenter2.deptCd,
                        costCenter2.deptNm,
                        costCenter2.startDateActive,
                        costCenter2.endDateActive,
                        costCenter2.enabledFlag,
                        costCenter2.attribute1,
                        costCenter2.attribute2,
                        costCenter2.attribute3,
                        costCenter2.attribute4,
                        costCenter2.attribute5,
                        costCenter2.attribute6,
                        costCenter2.attribute7,
                        costCenter2.attribute8,
                        costCenter2.attribute9,
                        costCenter2.attribute10,
                        costCenter2.picode,
                        costCenter2.pistat,
                        costCenter2.pidate,
                        costCenter2.pitime,
                        costCenter2.piuser,
                        costCenter2.pimsg,
                        costCenter2.pimsgid
                ))
                .from(costCenter2)
                .where(costCenter2.compCd.eq(compCd)
                        .and(deptCdLk(value))
                        .or(deptNmLk(value))
                )
                .orderBy(costCenter2.deptCd.asc())
                .fetch();
    }

    @Override
    public List<CostCenterDto2> getSlipCctr(CostCenterDto2 costCenterDto){
        List<CostCenterDto2> result = queryFactory
                .select(new QCostCenterDto2(
                        costCenter2.compCd,
                        costCenter2.deptCd,
                        costCenter2.deptNm,
                        costCenter2.attribute2
                ))
                .from(costCenter2)
                .where(
                        costCenter2.deptCd.in(JPAExpressions.select(cboApEmpSegment2.segment2)
                                        .from(cboApEmpSegment2)
                                        .where(cboApEmpSegment2.personId.eq(costCenterDto.personId)
                                                .and(cboApEmpSegment2.enabledFlag.eq("Y"))
                                                .and(cboApEmpSegment2.orgId.eq(Integer.valueOf(costCenterDto.compCd))))
                                )
                                .and(costCenter2.compCd.eq(costCenterDto.compCd))
                                //.and(costCenter2.startDateActive.loe(costCenterDto.postingDate))
                                //.and(costCenter2.startDateActive.loe(LocalDate.parse(costCenterDto.postingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay())
                                //.and(costCenter2.endDateActive.goe(costCenterDto.postingDate))
                                        .and(deptNmLk(costCenterDto.deptNm))
                                        .and(deptCdLk(costCenterDto.deptCd))
                )
                .orderBy(costCenter2.deptCd.asc())
                .fetch();

        return result;
    }

    @Override
    public List<CostCenterDto2> getSlipCctrAdmin(CostCenterDto2 costCenterDto){
        List<CostCenterDto2> result = queryFactory
                .select(new QCostCenterDto2(
                        costCenter2.compCd,
                        costCenter2.deptCd,
                        costCenter2.deptNm,
                        costCenter2.attribute2
                ))
                .from(costCenter2)
                .where(
                        costCenter2.deptCd.in(JPAExpressions.select(cboApEmpSegment2.segment2)
                                        .from(cboApEmpSegment2)
                                        .where(cboApEmpSegment2.enabledFlag.eq("Y")
                                                .and(cboApEmpSegment2.orgId.eq(Integer.valueOf(costCenterDto.compCd))))
                                )
                                .and(costCenter2.compCd.eq(costCenterDto.compCd))
                                .and(deptNmLk(costCenterDto.deptNm))
                                .and(deptCdLk(costCenterDto.deptCd))
                )
                .orderBy(costCenter2.deptCd.asc())
                .fetch();

        return result;
    }

    @Override
    public List<CostCenterDto2> getCctrsDelegate(DelegateDto delegateDto) {

        return queryFactory
                .select(new QCostCenterDto2(
                        costCenter2.compCd,
                        costCenter2.deptCd,
                        costCenter2.deptNm,
                        costCenter2.attribute2
                )).distinct()
                .from(costCenter2)
                .leftJoin(employee).on(costCenter2.deptCd.eq(employee.cctrCd),costCenter2.compCd.eq(employee.compCd))
                .leftJoin(delegate).on(delegate.giveUserId.eq(employee.empNo),delegate.compCd.eq(employee.compCd))
                .where(costCenter2.compCd.eq(delegateDto.getCompCd())
                        .and(receiveUserIdEq(delegateDto.getReceiveUserId()))
                        .and(delegateStatCdEq(delegateDto.getDelegateStatCd()))
                        .and(delegate.startDate.loe(delegateDto.getStartDate()))
                        .and(delegate.endDate.goe(delegateDto.getEndDate()))
                        .and(cctrSearch(delegateDto.getSearch()))
                        .or(employee.empNo.eq(delegateDto.getReceiveUserId()))
                )
                .orderBy(costCenter2.deptCd.asc())
                .fetch();
    }


    private BooleanExpression deptCdLk (String deptCd) {
        return hasText(deptCd) ? costCenter2.deptCd.likeIgnoreCase(deptCd + "%") : null;
    }

    private BooleanExpression deptNmLk (String deptNm) {
        return hasText(deptNm) ? costCenter2.deptNm.likeIgnoreCase("%" + deptNm + "%") : null;
    }

    private BooleanExpression cctrSearch(String search){
        return hasText(search) ? costCenter2.deptCd.containsIgnoreCase(search).or(costCenter2.deptNm.containsIgnoreCase(search)) : null;
    }

    private BooleanExpression receiveUserIdEq(String receiveUserId) {
        return hasText(receiveUserId) ? delegate.receiveUserId.eq(receiveUserId) : null;
    }

    private BooleanExpression delegateStatCdEq(String delegateStatCd) {
        return hasText(delegateStatCd) ? delegate.delegateStatCd.eq(delegateStatCd) : null;
    }
}
