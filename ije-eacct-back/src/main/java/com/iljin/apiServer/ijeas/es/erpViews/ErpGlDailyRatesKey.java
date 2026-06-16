package com.iljin.apiServer.ijeas.es.erpViews;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ErpGlDailyRatesKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039749L;

    BigDecimal conversionRate;

    String fromCurrency;

    String toCurrency;

    LocalDateTime conversionDate;
}
