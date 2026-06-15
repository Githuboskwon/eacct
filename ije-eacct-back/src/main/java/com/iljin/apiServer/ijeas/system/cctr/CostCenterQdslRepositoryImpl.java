package com.iljin.apiServer.ijeas.system.cctr;


import com.iljin.apiServer.core.util.Util;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.iljin.apiServer.ijeas.system.cctr.QCboApEmpSegment3.cboApEmpSegment3;
import static com.iljin.apiServer.ijeas.system.cctr.QCostCenter.costCenter;
import static com.iljin.apiServer.ijeas.system.dept.QDeptAuth.deptAuth;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class CostCenterQdslRepositoryImpl implements CostCenterQdslRepository {

    private final JPAQueryFactory queryFactory;

    private final Util util;

    private enum EntityType {
        MST_CCTR
    }

    @Override
    public List<CostCenterDto> getByCctrCdOrCctrNmContaining(String compCd, String value) {

        return queryFactory
                .select(new QCostCenterDto(
                        costCenter.compCd,
                        costCenter.deptCd,
                        costCenter.deptNm,
                        costCenter.startDateActive,
                        costCenter.endDateActive,
                        costCenter.enabledFlag,
                        costCenter.attribute1,
                        costCenter.attribute2,
                        costCenter.attribute3,
                        costCenter.attribute4,
                        costCenter.attribute5,
                        costCenter.attribute6,
                        costCenter.attribute7,
                        costCenter.attribute8,
                        costCenter.attribute9,
                        costCenter.attribute10,
                        costCenter.picode,
                        costCenter.pistat,
                        costCenter.pidate,
                        costCenter.pitime,
                        costCenter.piuser,
                        costCenter.pimsg,
                        costCenter.pimsgid
                        ))
                .from(costCenter)
                .where(costCenter.compCd.eq(compCd).and(deptCdLk(value))
                        .or(costCenter.compCd.eq(compCd).and(deptNmLk(value)))
                )
                .orderBy(costCenter.deptCd.asc())
                .fetch();
    }

    @Override
    public List<CostCenterDto> getSlipCctr(CostCenterDto costCenterDto){
        List<CostCenterDto> result = queryFactory
                .select(new QCostCenterDto(
                        costCenter.compCd,
                        costCenter.deptCd,
                        costCenter.deptNm,
                        costCenter.attribute2
                ))
                .from(costCenter)
                .where(
                        costCenter.deptCd.in(JPAExpressions.select(cboApEmpSegment3.segment3)
                                        .from(cboApEmpSegment3)
                                        .where(cboApEmpSegment3.personId.eq(costCenterDto.personId)
                                                .and(cboApEmpSegment3.enabledFlag.eq("Y"))
                                                .and(cboApEmpSegment3.orgId.eq(Integer.valueOf(costCenterDto.compCd))))
                                )
                                .and(costCenter.compCd.eq(costCenterDto.compCd))
                                //.and(costCenter.startDateActive.loe(costCenterDto.postingDate))
                                //.and(costCenter.endDateActive.goe(costCenterDto.postingDate))
                                .and(deptNmLk(costCenterDto.deptNm))
                                .and(deptCdLk(costCenterDto.deptCd))
                )
                .orderBy(costCenter.deptCd.asc())
                .fetch();

        return result;
    }


    @Override
    public List<CostCenterDto> getCctrsDeptRole(CostCenterDto costCenterDto) {

        return queryFactory
                .select(new QCostCenterDto(
                        costCenter.compCd,
                        costCenter.deptCd,
                        costCenter.deptNm,
                        costCenter.attribute2
                ))
                .from(costCenter)
                .where(costCenter.compCd.eq(util.getLoginCompCd())
                        .and(deptSearch(costCenterDto.search))
                        .and(costCenter.deptCd.eq(JPAExpressions.select(employee.deptCd).from(employee).where(employee.empNo.eq(costCenterDto.empNo)))
                            .or(costCenter.deptCd.in(JPAExpressions.select(deptAuth.deptCd).from(deptAuth).where(deptAuth.empNo.eq(costCenterDto.empNo))))
                        )

                )
                .orderBy(costCenter.deptCd.asc())
                .fetch();
    }

    @Override
    public List<CostCenterDto> getCctrsBetween(String compCd, String startDept, String endDept) {

        return queryFactory
                .select(new QCostCenterDto(
                        costCenter.compCd,
                        costCenter.deptCd,
                        costCenter.deptNm,
                        costCenter.attribute2
                ))
                .from(costCenter)
                .where(costCenter.compCd.eq(compCd)
                        .and(costCenter.deptCd.between(startDept, endDept))
                )
                .orderBy(costCenter.deptCd.asc())
                .fetch();
    }


    private BooleanExpression compCdEq(String compCd, EntityType entityType) {
        if (!StringUtils.hasText(compCd)) { return null; }
        switch (entityType) {
            case MST_CCTR:
                return costCenter.compCd.eq(compCd);
            default:
                return null;
        }
    }

    private BooleanExpression deptCdLk (String deptCd) {
        return hasText(deptCd) ? costCenter.deptCd.likeIgnoreCase(deptCd + "%") : null;
    }

    private BooleanExpression deptNmLk (String deptNm) {
        return hasText(deptNm) ? costCenter.deptNm.likeIgnoreCase("%" + deptNm + "%") : null;
    }

    private BooleanExpression deptSearch(String search){
        return hasText(search) ? costCenter.deptCd.containsIgnoreCase(search).or(costCenter.deptNm.containsIgnoreCase(search)) : null;
    }

}
