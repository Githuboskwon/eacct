package com.iljin.apiServer.ijeas.system.supplyBank;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.erpViews.QErpGlTermsDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpGlTermsDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.es.erpViews.QErpGlTerms.erpGlTerms;
import static com.iljin.apiServer.ijeas.system.supplyBank.QSupplyBank.supplyBank;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class SupplyQdslRepositoryImpl implements SupplyQdslRepository {

    private final Util util;
    private final JPAQueryFactory queryFactory;

    @Override
    public ErpGlTermsDto getPaymentMethod(String termId){
        String compCd = util.getLoginCompCd();
        return queryFactory
                .select(new QErpGlTermsDto(
                        erpGlTerms.paymentMethod
                ))
                .from(erpGlTerms)
                .where(erpGlTerms.orgId.eq(compCd),
                        erpGlTerms.termId.eq(Integer.valueOf(termId))
                ).fetchFirst();
    }

    @Override
    public List<SupplyBankDto> getSupplyBankList(String integrationVendorNum, String paymentMethod){
        String compCd = util.getLoginCompCd();

        String paymentMethodFlag = "";

        if(!paymentMethod.equals("ALL")){
            paymentMethodFlag = paymentMethod;
        }

        return queryFactory
                .select(new QSupplyBankDto(
                        supplyBank.integrationVendorNum,
                        supplyBank.vendorName,
                        supplyBank.vatRegistrationNum,
                        supplyBank.vendorSiteId,
                        supplyBank.supplierSiteId,
                        supplyBank.vendorSiteCodeAlt,
                        supplyBank.bankAccountId,
                        supplyBank.bankName,
                        supplyBank.bankNumber,
                        supplyBank.bankBranchName,
                        supplyBank.bankAccountName.as("bankAccountName2"),
                        new CaseBuilder().when(supplyBank.integrationVendorNum.eq("1007008")).then("대표이사_계좌")
                                .when(supplyBank.integrationVendorNum.eq("1005788")).then("대표이사_계좌")
                                .otherwise(supplyBank.bankName.concat("_")
                                        .concat(Expressions.stringTemplate("replace({0},'-','')" ,supplyBank.bankAccountNumber))
                                        .concat("_")
                                        .concat(Expressions.stringTemplate("replace({0},'&','＆')" ,supplyBank.bankAccountName)))
                                .as("bankAccountName"),
                        new CaseBuilder().when(supplyBank.integrationVendorNum.eq("1007008")).then("대표이사_계좌")
                                .when(supplyBank.integrationVendorNum.eq("1005788")).then("대표이사_계좌")
                                .otherwise(supplyBank.bankAccountNumber)
                                .as("bankAccountNumber"),
                        supplyBank.currencyCode,
                        supplyBank.primaryFlag,
                        supplyBank.extBankAccountId,
                        supplyBank.indentureName,
                        supplyBank.orgId,
                        supplyBank.noteAccountFlag
                ))
                .from(supplyBank)
                .where(supplyBank.orgId.eq(compCd),
                        supplyBank.integrationVendorNum.eq(integrationVendorNum),
                        noteAccountFlagEq(paymentMethodFlag)
                        )
                .orderBy(supplyBank.integrationVendorNum.asc() , supplyBank.bankAccountId.asc())
                .fetch();

    }

    private BooleanExpression noteAccountFlagEq(String noteAccountFlag) {
        return hasText(noteAccountFlag) ? supplyBank.noteAccountFlag.eq(noteAccountFlag) : null;
    }

}
