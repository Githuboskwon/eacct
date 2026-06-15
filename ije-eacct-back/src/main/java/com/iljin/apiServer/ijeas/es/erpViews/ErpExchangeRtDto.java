package com.iljin.apiServer.ijeas.es.erpViews;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ErpExchangeRtDto {
    //FROM_CURRENCY
    String fromCurrency;
    //TO_CURRENCY
    String toCurrency;
    //CONV_DATE
    LocalDateTime conversionDate;
    //CONV_RATE
    BigDecimal conversionRate;
    //USER_CONV_TYPE
    String conversionType;

    @QueryProjection
    public ErpExchangeRtDto(String fromCurrency, String toCurrency, LocalDateTime conversionDate, BigDecimal conversionRate, String conversionType) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.conversionDate = conversionDate;
        this.conversionRate = conversionRate;
        this.conversionType = conversionType;
    }
}
