package com.iljin.apiServer.ijeas.system.currency;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.system.currency.QCurrency.currency;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class CurrencyQdslRepositoryImpl implements CurrencyQdslRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CurrencyDto> getCurrencyList(CurrencyDto currencyDto) {
        return queryFactory.select(
            new QCurrencyDto(
                currency.currencyCode,
                currency.currencyName,
                currency.precision,
                currency.extendedPrecision,
                currency.startDateActive,
                currency.endDateActive,
                currency.enabledFlag
            ))
            .from(currency)
            .where(searchConditionContains(currencyDto.getCurrencyName()))
            .fetch();
    }

    private BooleanBuilder searchConditionContains(String searchWord) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(hasText(searchWord)) {
            booleanBuilder.or(currency.currencyCode.contains(searchWord))
                .or(currency.currencyName.containsIgnoreCase(searchWord));
        }
        return booleanBuilder;
    }

}
