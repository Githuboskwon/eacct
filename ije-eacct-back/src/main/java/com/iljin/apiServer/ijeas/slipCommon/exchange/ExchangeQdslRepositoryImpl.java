package com.iljin.apiServer.ijeas.slipCommon.exchange;

import static com.iljin.apiServer.ijeas.es.erpViews.QErpGlDailyRates.erpGlDailyRates;
import static com.iljin.apiServer.ijeas.es.erpViews.QErpSpCurrencies.erpSpCurrencies;
import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.ijeas.es.erpViews.ErpExchangeRtDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSpCurrencies;
import com.iljin.apiServer.ijeas.es.erpViews.QErpExchangeRtDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ExchangeQdslRepositoryImpl implements ExchangeQdslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ErpExchangeRtDto> getErpExchangeRate(String curCd, String excDt) {
        excDt.replaceAll("-", "");

        return queryFactory
                .select(new QErpExchangeRtDto(
                        erpGlDailyRates.fromCurrency,
                        erpGlDailyRates.toCurrency,
                        erpGlDailyRates.conversionDate,
                        erpGlDailyRates.conversionRate,
                        erpGlDailyRates.conversionType
                ))
                .from(erpGlDailyRates)
                .where(fromCurrencyEq(curCd),
                        conversionDateEq(excDt),
                        erpGlDailyRates.toCurrency.eq("KRW")

                )
                .fetch();
    }

    @Override
    public List<ErpSpCurrencies> getSpCurrentcieCode(){
      return queryFactory
              .selectFrom(erpSpCurrencies).orderBy(erpSpCurrencies.gubun.asc()).fetch();
    }



    private BooleanExpression fromCurrencyEq(String fromCurrency) {
        return hasText(fromCurrency) ? erpGlDailyRates.fromCurrency.eq(fromCurrency) : null;
    }

    private BooleanExpression conversionDateEq(String conversionDate) {
        return hasText(conversionDate) ? Expressions.stringTemplate("TO_CHAR({0}, 'YYYYMMDD')", erpGlDailyRates.conversionDate).eq(conversionDate) : null;
    }

}
