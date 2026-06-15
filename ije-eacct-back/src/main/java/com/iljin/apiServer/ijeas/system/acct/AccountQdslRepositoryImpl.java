package com.iljin.apiServer.ijeas.system.acct;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.system.acct.QAccount.account;
import static com.iljin.apiServer.ijeas.system.acct.QAccountSub.accountSub;
import static com.iljin.apiServer.ijeas.system.cctr2.QCostCenter2.costCenter2;
import static com.iljin.apiServer.ijeas.system.acct.QCboSpDffColumn.cboSpDffColumn;
import static com.querydsl.core.types.ExpressionUtils.count;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class AccountQdslRepositoryImpl implements AccountQdslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<AccountDto> getAccountList(AccountDto accountDto){
        List<AccountDto> result = queryFactory
                .select(new QAccountDto(
                        account.compCd, account.trxTypeCd, account.acctCd, account.acctNm, account.acctType, account.enabledFlag, account.drCr, account.drcrType,
                        account.interfaceSlipType, account.interfaceSlipTypeNm, account.acctModule, account.interfaceModule, account.checkFlag, account.acctAttribute1,
                        account.acctAttribute2, account.assetsTrackingFlag, account.attribute1, account.attribute2, account.attribute3, account.attribute4, account.attribute5, account.attribute6,
                        account.attribute7, account.attribute8, account.attribute9, account.attribute10, account.attribute11, account.attribute12, account.attribute13,
                        account.attribute14, account.attribute15
                ))
                .from(account)
                .where(acctSearch(accountDto.acctCd, accountDto.acctNm)
                        .and(account.enabledFlag.eq("Y"))
                        .and(trxTypeCdEq(accountDto.trxTypeCd))
                        .and(interfaceSlipTypeEq(accountDto.interfaceSlipType))
                )
                .fetch();


        return result;
    }

    @Override
    public List<AccountDto> getSlipAccountList(AccountDto accountDto){
        List<AccountDto> result = queryFactory
                .select(new QAccountDto(
                    account.compCd, account.trxTypeCd, account.acctCd, account.acctNm, account.acctType, account.enabledFlag, account.drCr, account.drcrType,
                        account.acctModule, account.acctAttribute1, account.assetsTrackingFlag,
                        ExpressionUtils.as(JPAExpressions.select(count(cboSpDffColumn.applicationShortName))
                                        .from(cboSpDffColumn)
                                        .where(cboSpDffColumn.applicationShortName.eq(account.acctModule)
                                                .and(cboSpDffColumn.descriptiveFlexfieldName.eq(new CaseBuilder()
                                                         .when(account.acctModule.eq("AP")).then("AP_INVOICE_LINES")
                                                         .when(account.acctModule.eq("AR")).then("RA_CUSTOMER_TRX_LINES")
                                                         .otherwise("#")))
                                                .and(cboSpDffColumn.contextCode.eq(account.acctCd))
                                        ),
                                "dffCnt"),
                        ExpressionUtils.as(JPAExpressions.select(count(cboSpDffColumn.applicationShortName))
                                        .from(cboSpDffColumn)
                                        .where(cboSpDffColumn.applicationShortName.eq(account.acctModule)
                                                .and(cboSpDffColumn.descriptiveFlexfieldName.eq(new CaseBuilder()
                                                        .when(account.acctModule.eq("AP")).then("AP_INVOICE_LINES")
                                                        .when(account.acctModule.eq("AR")).then("RA_CUSTOMER_TRX_LINES")
                                                        .otherwise("#")))
                                                .and(cboSpDffColumn.contextCode.eq(account.acctCd))
                                                .and(cboSpDffColumn.requiredFlag.eq("Y"))
                                        ),
                                "requiredFlagCnt"),
                        Expressions.asString("").as("deptType"),
                        account.interfaceSlipType
                ))
                .from(account)
                .where(acctSearch(accountDto.acctCd, accountDto.acctNm)
                        .and(account.enabledFlag.eq("Y"))
                        .and(account.compCd.eq(accountDto.compCd))
                        .and(account.acctModule.eq(accountDto.acctModule))
                        .and(account.acctAttribute2.ne(accountDto.deptType.equals("S") ? "M" : (accountDto.deptType.equals("M") ? "S" : "X"))
                                .or(account.acctAttribute2.isNull()))  //조건 CASE WHEN deptType = 'S' THEN 'M' WHEN deptType = 'M' THEN 'S' ELSE 'X'
                        .and(subAcctTrxTypeCdCheck(accountDto.trxTypeCd, accountDto.attribute1))
                )
                .orderBy(account.acctCd.asc())
                .fetch();

        return result;
    }

    @Override
    public List<AccountDto> getSlipAccountSubList(AccountDto accountDto){
        List<AccountDto> result = queryFactory
                .select(new QAccountDto(
                        accountSub.compCd, accountSub.trxTypeCd, accountSub.childTrxTypeCd, accountSub.childTrxTypeNm, accountSub.attribute1, accountSub.attribute2,
                        accountSub.attribute3, accountSub.attribute4, accountSub.attribute5
                ))
                .from(accountSub)
                .where(accountSub.enabledFlag.eq("Y")
                        .and(accountSub.compCd.eq(accountDto.compCd))
                        .and(subTrxTypeCdEq(accountDto.trxTypeCd))
                        .and(subAttribute1Eq(accountDto.attribute1))
                )
                .fetch();

        return result;
    }


    //비용예산 계정 조회
    @Override
    public List<AccountDto> getAccountPopList(AccountDto accountDto){


        String acctAttribute2 = "";

        acctAttribute2 = queryFactory
                .select(costCenter2.attribute2)
                .from(costCenter2)
                .where(costCenter2.deptCd.eq(accountDto.getDeptCd())
                ).fetchOne();


        List<AccountDto> result = queryFactory
                .select(new QAccountDto(
                        account.compCd, account.acctCd, account.acctNm, account.assetsTrackingFlag
                )).distinct()
                .from(account)
                .where(acctSearch(accountDto.acctCd, accountDto.acctCd)
                        .and(account.enabledFlag.eq("Y"))
                        .and(account.acctType.eq("E"))
                        .and(account.acctCd.likeIgnoreCase("53%").or(account.acctCd.likeIgnoreCase("55%")).or(account.acctCd.likeIgnoreCase("57%")) )
                        .and(deptSearch(acctAttribute2))
                ).orderBy(account.acctCd.asc())
                .fetch();


        return result;
    }


    private BooleanBuilder acctSearch(String acctCode, String acctName){
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(hasText(acctCode)){
            booleanBuilder.or(account.acctCd.containsIgnoreCase(acctCode));
        }
        if(hasText(acctName)) {
            booleanBuilder.or(account.acctNm.containsIgnoreCase(acctName.toUpperCase()));
        }

        return booleanBuilder;
    }

    private BooleanExpression trxTypeCdEq(String trxTypeCd) {
        return hasText(trxTypeCd) ? account.trxTypeCd.eq(trxTypeCd) : null;
    }

    private BooleanExpression subTrxTypeCdEq(String trxTypeCd) {
        return hasText(trxTypeCd) ? accountSub.trxTypeCd.eq(trxTypeCd) : null;
    }

    private BooleanExpression subAttribute1Eq(String attribute1) {
        return hasText(attribute1) ? accountSub.attribute1.eq(attribute1) : null;
    }

    private BooleanExpression interfaceSlipTypeEq(String interfaceSlipType) {
        return hasText(interfaceSlipType) ? account.interfaceSlipType.eq(interfaceSlipType) : null;
    }

    private BooleanExpression deptSearch(String deptCd){
        BooleanExpression booleanExpression = null;

        if(deptCd.equals("M")){
            booleanExpression = account.acctAttribute2.in("M","C");
        }else
        if(deptCd.equals("S")) {
            booleanExpression = account.acctAttribute2.in("S","C");
        }else{
            booleanExpression = account.acctAttribute2.in("M","S","C");
        }

        return booleanExpression;
    }

    private BooleanExpression subAcctTrxTypeCdCheck(String trxTypeCd, String attribute1){
        Long count = 0L;

        if(hasText(attribute1)){
            count = queryFactory.select(accountSub.childTrxTypeCd.count())
                    .from(accountSub)
                    .where(accountSub.trxTypeCd.eq(trxTypeCd)
                            .and(accountSub.attribute1.eq(attribute1)))
                    .fetchFirst();
        }else{
            count = queryFactory.select(accountSub.childTrxTypeCd.count())
                    .from(accountSub)
                    .where(accountSub.trxTypeCd.eq(trxTypeCd))
                    .fetchFirst();
        }


        if(count == 0L){
            return account.trxTypeCd.eq(trxTypeCd);
        }else{
            return account.trxTypeCd.in(JPAExpressions.select(accountSub.childTrxTypeCd)
                                                        .from(accountSub)
                                                        .where(accountSub.trxTypeCd.eq(trxTypeCd))
            );
        }

    }

}
