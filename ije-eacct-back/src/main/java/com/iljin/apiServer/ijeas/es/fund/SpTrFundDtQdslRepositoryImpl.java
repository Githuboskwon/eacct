package com.iljin.apiServer.ijeas.es.fund;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.es.fund.QSpTrFundDt.spTrFundDt;

@RequiredArgsConstructor
@Repository
public class SpTrFundDtQdslRepositoryImpl implements SpTrFundDtQdslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SpTrFundDt> pullErpSpTrFundDtList(String compCd , String slipNo) {
        return queryFactory
        .selectFrom(spTrFundDt)
        .where(spTrFundDt.compCd.eq(compCd), spTrFundDt.slipNo.eq(slipNo)
        )
        .fetch();
    }




//    private BooleanExpression checkDtFromGoe(LocalDateTime checkDtFrom) {
//        return nonNull(checkDtFrom) ? apPaymentsHd.checkDt.goe(checkDtFrom) : null;
//    }
//
//    private BooleanExpression checkDtToLoe(LocalDateTime checkDtTo) {
//        return nonNull(checkDtTo) ? apPaymentsHd.checkDt.loe(checkDtTo) : null;
//    }
//
//    private BooleanExpression futurePayDueDtFromGoe(LocalDateTime checkDtFrom) {
//        return nonNull(checkDtFrom) ? apPaymentsHd.futurePayDueDt.goe(checkDtFrom) : null;
//    }
//
//    private BooleanExpression futurePayDueDtToLoe(LocalDateTime checkDtTo) {
//        return nonNull(checkDtTo) ? apPaymentsHd.futurePayDueDt.loe(checkDtTo) : null;
//    }
//
//    private BooleanExpression transferredByEq(String transferredBy) {
//        return hasText(transferredBy) ? apPaymentsHd.transferredBy.eq(transferredBy) : null;
//    }
//
//    private BooleanExpression paymentMethodCdEq(String paymentMethodCd) {
//        return hasText(paymentMethodCd) ? apPaymentsHd.paymentMethodLookupCode.eq(paymentMethodCd) : null;
//    }
//
//    private BooleanExpression bankAcctNumLk(String bankAcctNum) {
//        return hasText(bankAcctNum) ? apPaymentsHd.bankAcctNum.likeIgnoreCase("%" + bankAcctNum.replaceAll("-","") + "%") : null;
//    }

}
