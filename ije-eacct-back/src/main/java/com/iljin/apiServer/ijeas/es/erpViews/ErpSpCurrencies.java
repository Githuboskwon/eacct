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
@Table(name = "CBO_SP_CURRENCIES_V")
@Entity
public class ErpSpCurrencies {

    @Id
    @Column(name = "CURRENCY_CODE")
    String currencyCode;

    @Column(name = "PRECISION")
    Integer precision;

    @Column(name = "GUBUN")
    Integer gubun;

}
