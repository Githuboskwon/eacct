package com.iljin.apiServer.ijeas.system.confirm;


import com.iljin.apiServer.ijeas.system.emp.QEmployee;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.system.cctr.QCostCenter.costCenter;
import static com.iljin.apiServer.ijeas.system.confirm.QConfirm.confirm;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class ConfirmQdslRepositoryImpl implements ConfirmQdslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ConfirmDto> getConfirmList(ConfirmDto confirmDto) {

        QEmployee employee1 = new QEmployee("employee1");

        return queryFactory
                .select(new QConfirmDto(
                        confirm.compCd,
                        confirm.deptCd,
                        costCenter.deptNm.as("deptNm"),
                        confirm.confirmUserId,
                        employee1.empNm.as("confirmUserNm"),
                        confirm.confirmSeq,
                        confirm.confirmStartAmt,
                        confirm.confirmEndAmt,
                        confirm.remark,
                        confirm.regId,
                        confirm.regDtm,
                        confirm.chgId,
                        employee.empNm.as("chgNm"),
                        confirm.chgDtm
                        ))
                .from(confirm)
                .leftJoin(employee).on(employee.empNo.eq(confirm.chgId),employee.compCd.eq(confirm.compCd))
                .leftJoin(employee1).on(employee1.empNo.eq(confirm.confirmUserId),employee1.compCd.eq(confirm.compCd))
                .leftJoin(costCenter).on(costCenter.deptCd.eq(confirm.deptCd),costCenter.compCd.eq(confirm.compCd))
                .where(confirm.compCd.eq(confirmDto.getCompCd())
                .and(confirmSeqEq(confirmDto.getConfirmSeq()))
                .and(confirmUserIdEq(confirmDto.getConfirmUserId()))
                .and(deptCdEq(confirmDto.getDeptCd()))
                )
                .orderBy((confirm.deptCd.desc()),(confirm.confirmSeq.asc()))
                .fetch();
    }

    private BooleanExpression deptCdEq(String deptCd) {
        return hasText(deptCd) ? confirm.deptCd.eq(deptCd) : null;
    }

    private BooleanExpression confirmUserIdEq(String confirmUserId) {
        return hasText(confirmUserId) ? confirm.confirmUserId.eq(confirmUserId) : null;
    }

    private BooleanExpression confirmSeqEq(String confirmSeq) {
        return hasText(confirmSeq) ? confirm.confirmSeq.eq(confirmSeq) : null;
    }

}
