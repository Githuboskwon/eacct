package com.iljin.apiServer.ijeas.system.termsPayment;


import com.iljin.apiServer.core.util.Util;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.iljin.apiServer.ijeas.system.termsPayment.QTermsPayment.termsPayment;
import static com.iljin.apiServer.ijeas.es.erpViews.QErpGlTerms.erpGlTerms;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class TermsPaymentQdslRepositoryImpl implements TermsPaymentQdslRepository {

    private final JPAQueryFactory queryFactory;

    private final Util util;


    @Override
    public List<TermsPaymentDto> getTermsPaymentList(TermsPaymentDto termsPaymentDto) {

        String compCd = util.getLoginCompCd();

        return queryFactory
                .select(new QTermsPaymentDto(
                        erpGlTerms.trxType,
                        termsPayment.termId,
                        termsPayment.name,
                        termsPayment.description,
                        termsPayment.compCd,
                        termsPayment.notesFlag,
                        termsPayment.maturityDays,
                        termsPayment.dueDateCalcFlag,
                        termsPayment.currencyType,
                        termsPayment.paymentMethod,
                        termsPayment.vendorAcctCheck,
                        termsPayment.trxTypeCd,
                        termsPayment.trxTypeNm,
                        termsPayment.deptCd,
                        termsPayment.deptNm,
                        erpGlTerms.spEnabledFlag,
                        termsPayment.eaEnabledFlag,
                        termsPayment.ptEnabledFlag,
                        termsPayment.dtChangeFlag,
                        termsPayment.deptCd.as("oriDeptCd"),
                        termsPayment.deptNm.as("oriDeptNm"),
                        termsPayment.chgId,
                        employee.empNm.as("chgNm"),
                        termsPayment.chgDtm
                ))
                .from(termsPayment)
                .leftJoin(erpGlTerms).on(
                        termsPayment.compCd.eq(Expressions.stringTemplate("TO_CHAR({0})", erpGlTerms.orgId)),
                        termsPayment.termId.eq(erpGlTerms.termId)
                )
                .leftJoin(employee).on(
                        employee.compCd.eq(termsPayment.compCd),
                        employee.empNo.eq(termsPayment.chgId)
                )
                .where(termsPayment.compCd.eq(compCd),
                        termsIdEq(termsPaymentDto.getTermId()),
                        deptCdEq(termsPaymentDto.getDeptCd())
                )
                .orderBy((termsPayment.trxType.asc()),(termsPayment.name.asc()))
                .fetch();
    }


    private BooleanExpression deptCdEq(String deptCd) {
        return hasText(deptCd) ? termsPayment.deptCd.eq(deptCd) : null;
    }

    private BooleanExpression termsIdEq(Integer termId) {
        return Objects.nonNull(termId) ? termsPayment.termId.eq(termId) : null;
    }

}
