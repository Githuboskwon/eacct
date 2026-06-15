package com.iljin.apiServer.ijeas.system.emp;


import static com.iljin.apiServer.core.security.role.QRole.role;
import static com.iljin.apiServer.core.security.role.QUserRole.userRole;
import static com.iljin.apiServer.core.security.user.QUser.user;
import static com.iljin.apiServer.ijeas.system.cctr.QCostCenter.costCenter;
import static com.iljin.apiServer.ijeas.system.delegate.QDelegate.delegate;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.core.util.Util;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class EmployeeQdslRepositoryImpl implements EmployeeQdslRepository {

    private final JPAQueryFactory queryFactory;

    private final Util util;

    @Override
    public List<EmployeeDto> searchAccrualSlipEmployees(String value) {
        String compCd = util.getLoginCompCd();
        return queryFactory
                .select(new QEmployeeDto(
                        employee.cctrCd,
                        employee.cctrNm,
                        employee.deptCd,
                        employee.deptNm,
                        employee.pjtId,
                        employee.pjtCd,
                        employee.pjtNm,
                        employee.productCd,
                        employee.productNm,
                        employee.taskNo,
                        employee.taskNm,
                        employee.taskId,
                        employee.trAcctCd,
                        employee.trAcctNm,
                        employee.slipTypeCd,
                        employee.slipTypeNm,
                        employee.segment1Cd.as("compCode"),
                        employee.segment9Cd,
                        employee.segment10Cd,
                        employee.ledgerId,
                        costCenter.attribute2
                ))
                .from(employee)
                .leftJoin(costCenter).on(employee.compCd.eq(costCenter.compCd) , employee.deptCd.eq(costCenter.deptCd))
                .where(employee.compCd.eq(compCd),
                        employee.empNo.eq(value),
                        costCenter.enabledFlag.eq("Y")
                )
                .fetch();
    }

    @Override
    public List<EmployeeDto> getDelegatingEmployeesByEmpNoOrEmpNm(String value) {
        String loginCompCd = util.getLoginCompCd();
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return queryFactory
            .select(new QEmployeeDto(
                employee.compCd,
                employee.compNm,
                employee.empNo,
                employee.empNm,
                employee.personId.stringValue(),
                employee.deptCd,
                employee.deptNm,
                employee.upperDeptCd,
                employee.upperDeptNm,
                employee.jobDutCd,
                employee.jobDutNm,
                employee.jobGradeCd,
                employee.jobGradeNm,
                userRole.role,
                role.roleNm,
                employee.serveCd,
                employee.serveNm,
                user.enableFlag,
                employee.email,
                employee.mobTelNo,
                user.creationDate,
                user.modifiedDate,
                employee.ledgerId,
                employee.productCd,
                employee.productNm,
                employee.pjtId,
                employee.pjtCd,
                employee.pjtNm,
                employee.trAcctCd,
                employee.trAcctNm,
                employee.slipTypeCd,
                employee.slipTypeNm,
                employee.segment1Cd,
                employee.segment9Cd,
                employee.segment10Cd,
                employee.cctrCd,
                employee.cctrNm,
                employee.taskNo,
                employee.taskNm,
                employee.taskId
            ))
            .from(delegate)
            .innerJoin(employee).on(employee.empNo.eq(delegate.giveUserId), employee.compCd.eq(loginCompCd))
            .innerJoin(user).on(user.loginId.eq(employee.empNo), user.compCd.eq(loginCompCd))
            .innerJoin(userRole).on(userRole.userId.eq(user.id), userRole.compCd.eq(loginCompCd))
            .innerJoin(role).on(role.roleCd.eq(userRole.role), role.compCd.eq(loginCompCd))
            .where(employee.compCd.eq(loginCompCd), delegate.receiveUserId.eq(util.getLoginId()),
                delegate.startDate.loe(now),
                delegate.endDate.goe(now),
                empCdLk(value),
                empNmLk(value))
            .orderBy(employee.deptCd.asc(), employee.empNm.asc())
            .distinct()
            .fetch();
    }


    private BooleanExpression deptCdLk (String deptNm) {
        return hasText(deptNm) ? employee.deptCd.likeIgnoreCase("%" +deptNm + "%") : null;
    }

    private BooleanExpression deptNmLk (String deptNm) {
        return hasText(deptNm) ? employee.deptNm.likeIgnoreCase("%" + deptNm + "%") : null;
    }

    private BooleanExpression empCdLk (String empNm) {
        return hasText(empNm) ? employee.empNo.likeIgnoreCase("%" +empNm + "%") : null;
    }

    private BooleanExpression empNmLk (String empNm) {
        return hasText(empNm) ? employee.empNm.likeIgnoreCase("%" + empNm + "%") : null;
    }

    private BooleanExpression serveCdEq(String serveCd) {
        return hasText(serveCd) ? employee.serveCd.eq(serveCd) : null;
    }

    private BooleanExpression deptRoleYnEq(String deptRoleYn) {
        return hasText(deptRoleYn) ? employee.deptRoleYn.eq(deptRoleYn) : null;
    }
}
