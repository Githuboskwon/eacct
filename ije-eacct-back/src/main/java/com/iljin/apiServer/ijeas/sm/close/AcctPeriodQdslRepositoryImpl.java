package com.iljin.apiServer.ijeas.sm.close;

import static com.iljin.apiServer.ijeas.sm.close.QAcctPeriod.acctPeriod;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AcctPeriodQdslRepositoryImpl implements AcctPeriodQdslRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AcctPeriodDto> getCheckAcctPeriodList(String compCd, String postingDt) {
        return queryFactory
            .select(new QAcctPeriodDto(
                acctPeriod.closYm,
                acctPeriod.closStatCd
            ))
            .from(acctPeriod)
            .where(acctPeriod.compCd.eq(compCd),
                acctPeriod.closYm.eq(postingDt.substring(0,6)))
            .fetch();

    }

}
