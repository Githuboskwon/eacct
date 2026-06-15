package com.iljin.apiServer.ijeas.system.delegate;


import com.iljin.apiServer.ijeas.system.emp.QEmployee;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

import static com.iljin.apiServer.ijeas.system.delegate.QDelegate.delegate;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class DelegateQdslRepositoryImpl implements DelegateQdslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<DelegateDto> getDelegateList(DelegateDto delegateDto) {

        QEmployee employee1 = new QEmployee("employee1");
        QEmployee employee2 = new QEmployee("employee2");
        QEmployee employee3 = new QEmployee("employee3");

        return queryFactory
                .select(new QDelegateDto(
                        delegate.compCd,
                        delegate.giveUserId,
                        employee1.empNm.as("giveUserNm"),
                        delegate.receiveUserId,
                        employee2.empNm.as("receiveUserNm"),
                        delegate.delegateSeq,
                        delegate.delegateStatCd,
                        delegate.startDate,
                        delegate.endDate,
                        delegate.remark,
                        delegate.regId,
                        delegate.regDtm,
                        delegate.chgId,
                        employee3.empNm.as("chgNm"),
                        delegate.chgDtm
                ))
                .from(delegate)
                .leftJoin(employee1).on(employee1.empNo.eq(delegate.giveUserId),employee1.compCd.eq(delegate.compCd))
                .leftJoin(employee2).on(employee2.empNo.eq(delegate.receiveUserId),employee2.compCd.eq(delegate.compCd))
                .leftJoin(employee3).on(employee3.empNo.eq(delegate.chgId),employee3.compCd.eq(delegate.compCd))
                .where(delegate.compCd.eq(delegateDto.getCompCd())
                ,receiveUserIdEq(delegateDto.getReceiveUserId())
                ,giveUserIdEq(delegateDto.getGiveUserId())
                ,delegateStatCdEq(delegateDto.delegateStatCd)
                ,delegate.startDate.goe(delegateDto.startDate)
                ,delegate.startDate.loe(delegateDto.endDate)
                )
                .orderBy(delegate.startDate.asc())
                .fetch();
    }

    @Override
    public List<DelegateDto> getPersonalList(DelegateDto delegateDto) {

        QEmployee employee1 = new QEmployee("employee1");
        QEmployee employee2 = new QEmployee("employee2");

        return queryFactory
                .select(new QDelegateDto(
                        delegate.giveUserId,
                        employee1.empNm.as("giveUserNm"),
                        employee1.deptCd,
                        employee1.deptNm,
                        delegate.receiveUserId,
                        employee2.empNm.as("receiveUserNm"),
                        employee2.cctrCd.as("giveCctrCd"),
                        employee2.cctrNm.as("giveCctrNm")
                ))
                .from(delegate)
                .leftJoin(employee1).on(employee1.empNo.eq(delegate.giveUserId),employee1.compCd.eq(delegate.compCd))
                .leftJoin(employee2).on(employee2.empNo.eq(delegate.receiveUserId),employee2.compCd.eq(delegate.compCd))
                .where(delegate.compCd.eq(delegateDto.getCompCd())
                        ,(receiveUserIdEq(delegateDto.getReceiveUserId()))
                        ,(delegateStatCdEq(delegateDto.delegateStatCd))
                        ,(delegate.startDate.loe(delegateDto.startDate))
                        ,(delegate.endDate.goe(delegateDto.endDate))
                )
                .orderBy(employee1.empNm.asc())
                .fetch();
    }

    private BooleanExpression giveUserIdEq(String giveUserId) {
        return hasText(giveUserId) ? delegate.giveUserId.eq(giveUserId) : null;
    }

    private BooleanExpression receiveUserIdEq(String receiveUserId) {
        return hasText(receiveUserId) ? delegate.receiveUserId.eq(receiveUserId) : null;
    }

    private BooleanExpression delegateStatCdEq(String delegateStatCd) {
        return hasText(delegateStatCd) ? delegate.delegateStatCd.eq(delegateStatCd) : null;
    }



}
