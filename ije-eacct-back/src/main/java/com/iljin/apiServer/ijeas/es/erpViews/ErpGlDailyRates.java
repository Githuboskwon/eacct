package com.iljin.apiServer.ijeas.es.erpViews;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(ErpGlDailyRatesKey.class)
@Table(name = "CBO_GL_DAILY_RATES_V")
@Entity
public class ErpGlDailyRates {

    @Id
    @Column(name = "CONVERSION_RATE")
    BigDecimal conversionRate;

    @Id
    @Column(name = "FROM_CURRENCY")
    String fromCurrency;

    @Id
    @Column(name = "TO_CURRENCY")
    String toCurrency;

    @Id
    @Column(name = "CONVERSION_DATE")
    LocalDateTime conversionDate;

    @Column(name = "CONVERSION_TYPE")
    String conversionType;

}
