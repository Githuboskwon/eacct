package com.iljin.apiServer.ijeas.system.currency;

import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurrencyDto implements Serializable {

    private static final long serialVersionUID = -5052100834453865734L;

    String currencyCode;

    String currencyName;

    Long precision;

    Long extendedPrecision;

    LocalDateTime startDateActive;

    LocalDateTime endDateActive;

    String enabledFlag;

    @QueryProjection
    public CurrencyDto(String currencyCode, String currencyName, Long precision,
        Long extendedPrecision,
        LocalDateTime startDateActive, LocalDateTime endDateActive, String enabledFlag) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.precision = precision;
        this.extendedPrecision = extendedPrecision;
        this.startDateActive = startDateActive;
        this.endDateActive = endDateActive;
        this.enabledFlag = enabledFlag;
    }
}
