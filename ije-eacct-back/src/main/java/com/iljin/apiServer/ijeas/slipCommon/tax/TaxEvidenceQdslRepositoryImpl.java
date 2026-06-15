package com.iljin.apiServer.ijeas.slipCommon.tax;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.erpViews.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.es.erpViews.QErpGlCodes.erpGlCodes;
import static com.iljin.apiServer.ijeas.es.erpViews.QErpGlTrxTypeEvidence.erpGlTrxTypeEvidence;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class TaxEvidenceQdslRepositoryImpl implements TaxEvidenceQdslRepository {

    private final JPAQueryFactory queryFactory;

    private final Util util;

    @Override
    public List<ErpTaxEvidenceCodeDto> getErpTaxEvidenceCode(ErpSlipRequestDto erpSlipRequestDto , String trxTypeCode) {
        String compCd = util.getLoginCompCd();

        return queryFactory
                .select(new QErpTaxEvidenceCodeDto(
                        erpGlTrxTypeEvidence.evidenceName,
                        erpGlTrxTypeEvidence.evidenceCode,
                        erpGlTrxTypeEvidence.trxTypeCode,
                        erpGlTrxTypeEvidence.lineAttribute1,
                        new CaseBuilder()
                                .when(erpGlTrxTypeEvidence.lineAttribute1.eq("V")).then("부가세")
                                .when(erpGlTrxTypeEvidence.lineAttribute1.eq("W")).then("원천세")
                        .otherwise("Y").as("lineAttribute1Name"),
                        erpGlCodes.lineAttribute2.coalesce("").as("lineAttribute2"),
                        erpGlCodes.lineAttribute3.coalesce("C").as("lineAttribute3"),
                        erpGlTrxTypeEvidence.lineAttribute6,
                        erpGlCodes.code
                ))
                .from(erpGlTrxTypeEvidence)
                .leftJoin(erpGlCodes).on(erpGlCodes.code.eq(erpGlTrxTypeEvidence.evidenceCode),
                                        erpGlCodes.codeType.eq("CD0100"),
                                        erpGlCodes.headerEnabledFlag.eq("Y"),
                                        erpGlCodes.lineEnabledFlag.eq("Y"))
                .where(erpGlTrxTypeEvidence.orgId.eq(compCd),
                       erpGlTrxTypeEvidence.trxTypeCode.eq(trxTypeCode),
                       erpGlTrxTypeEvidence.enabledFlag.eq("Y"),
                       evidenceCodeLk(erpSlipRequestDto.getSearchCd()),
                       evidenceNameLk(erpSlipRequestDto.getSearchNm())
                ).orderBy(erpGlTrxTypeEvidence.evidenceName.asc())
                .fetch();
    }

    private BooleanExpression evidenceCodeLk(String evidenceCode) {
        return hasText(evidenceCode) ? erpGlTrxTypeEvidence.evidenceCode.toUpperCase().likeIgnoreCase("%" + evidenceCode.toUpperCase() + "%") : null;
    }

    private BooleanExpression evidenceNameLk(String evidenceName) {
        return hasText(evidenceName) ? erpGlTrxTypeEvidence.evidenceName.toUpperCase().likeIgnoreCase("%" + evidenceName.toUpperCase() + "%") : null;
    }

}
