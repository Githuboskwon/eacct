package com.iljin.apiServer.ijeas.slip.surtax;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.slip.surtax.QSurTaxCode.surTaxCode;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class SurTaxCodeQRepositoryImpl implements SurTaxCodeQRepository {

    private final Util util;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<SurTaxCodeDto> getSurTaxCodeList(ErpSlipRequestDto erpSlipRequestDto,String taxEvidenceType,String returnType) {
        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();
        String taxCode = "";

        if(returnType.equals("grid")){
            taxCode = erpSlipRequestDto.getTaxRateCd();
        }

        return queryFactory
                .select(new QSurTaxCodeDto(
                        surTaxCode.taxRateCode,
                        surTaxCode.taxStatusCode,
                        surTaxCode.taxRateId,
                        surTaxCode.taxAcctCode,
                        surTaxCode.percentageRate
                ))
                .from(surTaxCode)
                .where(surTaxCode.taxEvidenceType.eq(taxEvidenceType),
                        surTaxCode.ledgerId.eq(Integer.valueOf(ledgerId)),
                        taxRateCodeLk(erpSlipRequestDto.getSearchCd()),
                        taxStatusCodeLk(erpSlipRequestDto.getSearchNm()),
                        taxRateCodeEq(taxCode)
                )
                .fetch();

    }


    private BooleanExpression taxRateCodeLk(String taxRateCode) {
        return hasText(taxRateCode) ? surTaxCode.taxRateCode.toUpperCase().likeIgnoreCase("%" + taxRateCode.toUpperCase() + "%") : null;
    }

    private BooleanExpression taxStatusCodeLk(String taxStatusCode) {
        return hasText(taxStatusCode) ? surTaxCode.taxStatusCode.toUpperCase().likeIgnoreCase("%" + taxStatusCode.toUpperCase() + "%") : null;
    }

    private BooleanExpression taxRateCodeEq(String taxRateCode) {
        return hasText(taxRateCode) ? surTaxCode.taxRateCode.eq(taxRateCode) : null;
    }

}
