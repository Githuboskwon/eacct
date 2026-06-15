package com.iljin.apiServer.ijeas.system.scheduler;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static com.iljin.apiServer.ijeas.system.scheduler.QScheduler.scheduler;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class SchedulerQdslRepositoryImpl implements SchedulerQdslRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SchedulerDto> getSchedulerList(SchedulerDto schedulerDto) {
        return queryFactory
                .select(new QSchedulerDto(
                        scheduler.compCd,
                        scheduler.schedulerCd,
                        scheduler.schedulerNm,
                        scheduler.useYn,
                        scheduler.procDtm,
                        scheduler.remark,
                        scheduler.chgId,
                        employee.empNm,
                        scheduler.chgDtm
                ))
                .from(scheduler)
                .leftJoin(employee).on(employee.empNo.eq(scheduler.chgId))
                .where(compCdEq(schedulerDto.getCompCd()),
                        schedulerNmContains(schedulerDto.getSchedulerNm()),
                        useYnEq(schedulerDto.getUseYn()))
                .fetch();
    }

    private BooleanExpression compCdEq(String compCd) {
        return hasText(compCd) ? scheduler.compCd.eq(compCd) : null;
    }

    private BooleanExpression schedulerNmContains(String schedulerNm) {
        return hasText(schedulerNm) ? scheduler.schedulerNm.contains(schedulerNm) : null;
    }

    private BooleanExpression useYnEq(String useYn) {
        return hasText(useYn) ? scheduler.useYn.eq(useYn) : null;
    }
}
