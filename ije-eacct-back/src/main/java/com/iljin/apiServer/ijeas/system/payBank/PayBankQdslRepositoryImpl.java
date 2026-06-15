package com.iljin.apiServer.ijeas.system.payBank;

import static com.iljin.apiServer.ijeas.system.payBank.QPayBank.payBank;
import static org.springframework.util.StringUtils.hasText;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PayBankQdslRepositoryImpl implements PayBankQdslRepository{

    private final JPAQueryFactory queryFactory;

    public List<PayBankDto> getPayBankList(PayBankDto payBankDto) {

        return queryFactory.select(
            new QPayBankDto(
                payBank.operatingUnitId,
                payBank.bankAccountId,
                payBank.bankAccountName,
                payBank.bankAccountNum,
                payBank.bankName,
                payBank.bankBranchName,
                payBank.currencyCode
            ))
            .from(payBank)
            .where(payBank.operatingUnitId.eq(payBankDto.getOperatingUnitId())
                    .and(bankAccountNameContains(payBankDto.getBankAccountName()))
                )
            .fetch();
    }

    private BooleanBuilder bankAccountNameContains(String searchWord) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(hasText(searchWord)) {
            booleanBuilder.or(payBank.bankAccountName.containsIgnoreCase(searchWord))
                .or(payBank.bankName.containsIgnoreCase(searchWord))
                .or(payBank.bankBranchName.containsIgnoreCase(searchWord))
                .or(payBank.bankAccountNum.containsIgnoreCase(searchWord));
        }
        return booleanBuilder;
    }

}
