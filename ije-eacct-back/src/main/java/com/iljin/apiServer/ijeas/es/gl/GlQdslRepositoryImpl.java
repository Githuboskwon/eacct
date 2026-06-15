package com.iljin.apiServer.ijeas.es.gl;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.erpViews.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.es.erpViews.QErpGlTerms.erpGlTerms;
import static com.iljin.apiServer.ijeas.es.erpViews.QErpGlCodes.erpGlCodes;
import static com.iljin.apiServer.ijeas.system.termsPayment.QTermsPayment.termsPayment;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class GlQdslRepositoryImpl implements GlQdslRepository {

    private final Util util;
    private final JPAQueryFactory queryFactory;


    @Override
    public List<ErpGlTermsDto> getErpTermList(ErpSlipRequestDto erpSlipRequestDto) {
        String compCd = util.getLoginCompCd();

        return queryFactory
                .select(new QErpGlTermsDto(
                        erpGlTerms.trxType,
                        erpGlTerms.termId,
                        erpGlTerms.name,
                        erpGlTerms.description,
                        erpGlTerms.orgId,
                        erpGlTerms.notesFlag,
                        erpGlTerms.maturityDays,
                        erpGlTerms.dueDateCalcFlag,
                        erpGlTerms.currencyType,
                        erpGlTerms.paymentMethod,
                        erpGlTerms.vendorAcctCheck,
                        erpGlTerms.spEnabledFlag,
                        erpGlTerms.defaultTermFlag
                ))
                .from(erpGlTerms)
                .where(erpGlTerms.orgId.eq(compCd),
                        erpGlTerms.spEnabledFlag.eq("Y"),
                        trxTypeEq(erpSlipRequestDto.getTrxTypeCd()),
                        termIdLk(erpSlipRequestDto.getSearchCd()),
                        nameDescriptionLk(erpSlipRequestDto.getSearchNm())
                ).orderBy(erpGlTerms.termId.asc())
                .fetch();
    }

    @Override
    public List<ErpGlCodesDto> getTaxIsuueTypeList(ErpSlipRequestDto erpSlipRequestDto){
        String compCd = util.getLoginCompCd();

        return queryFactory
                .select(new QErpGlCodesDto(
                        erpGlCodes.code.as("taxIssueTypeCode"),
                        erpGlCodes.lineMeaning.as("taxIssueTypeName")
                ))
                .from(erpGlCodes)
                .where(erpGlCodes.codeType.eq("CD0090"),
                        erpGlCodes.headerEnabledFlag.eq("Y"),
                        erpGlCodes.lineEnabledFlag.eq("Y"),
                        taxIssueTypeCodeEq(erpSlipRequestDto.getSearchCd()),
                        taxIssueTypeNameLk(erpSlipRequestDto.getSearchNm())
                )
                .fetch();
    }

    @Override
    public Long getTermsDateCnt(ErpGlTermsDto erpSlipRequestDto) {

        String compCd = util.getLoginCompCd();

        return queryFactory
                .select(termsPayment.termId.count())
                .from(termsPayment)
                .where(termsPayment.compCd.eq(compCd),
                        termsPayment.termId.eq(erpSlipRequestDto.getTermId().intValue()),
                        termsPayment.eaEnabledFlag.eq(erpSlipRequestDto.getSpEnabledFlag()),
                        termsPayment.deptCd.eq(erpSlipRequestDto.getDeptCd()),
                        dtChangeFlagEq(erpSlipRequestDto.getDtChangeFlag())
                )
                .fetchFirst();
    }

    @Override
    public String getTermsChangeFlag(ErpGlTermsDto erpSlipRequestDto) {

        String compCd = util.getLoginCompCd();

        return queryFactory
                .select(termsPayment.dtChangeFlag.coalesce("N").as("dtChangeFlag"))
                .from(termsPayment)
                .where(termsPayment.compCd.eq(compCd),
                        termsPayment.termId.eq(erpSlipRequestDto.getTermId().intValue()),
                        termsPayment.eaEnabledFlag.eq(erpSlipRequestDto.getSpEnabledFlag()),
                        termsPayment.deptCd.eq(erpSlipRequestDto.getDeptCd())
                )
                .fetchFirst();
    }


    private BooleanExpression trxTypeEq(String trxType) {
        return hasText(trxType) ? erpGlTerms.trxType.eq(trxType) : null;
    }

    private BooleanExpression termIdLk(String termId) {
        return hasText(termId) ? erpGlTerms.termId.like("%" + termId + "%") : null;
    }

    private BooleanExpression nameDescriptionLk(String nameDescription) {
        return hasText(nameDescription) ?  Expressions.stringTemplate("CONCAT({0}, {1})", erpGlTerms.name , erpGlTerms.description).toUpperCase().likeIgnoreCase("%" + nameDescription.toUpperCase() + "%") : null;
    }


    private BooleanExpression taxIssueTypeCodeEq(String taxIssueTypeCode) {
        return hasText(taxIssueTypeCode) ? erpGlCodes.lineMeaning.eq(taxIssueTypeCode) : null;
    }

    private BooleanExpression taxIssueTypeNameLk(String taxIssueTypeName) {
        return hasText(taxIssueTypeName) ? erpGlCodes.code.toUpperCase().likeIgnoreCase("%" + taxIssueTypeName.toUpperCase() + "%") : null;
    }

    private BooleanExpression dtChangeFlagEq(String dtChangeFlag) {
        return hasText(dtChangeFlag) ? termsPayment.dtChangeFlag.eq(dtChangeFlag) : null;
    }


}
