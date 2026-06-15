package com.iljin.apiServer.ijeas.system.currency;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "CBO_SP_CURRENCY_CODE_V")
public class Currency {

    @Id
    @Column(name = "CURRENCY_CODE", nullable = false)
    private String currencyCode;

    @Column(name = "CURRENCY_NAME", nullable = false)
    private String currencyName;

    @Column(name = "PRECISION")
    private Long precision;

    @Column(name = "EXTENDED_PRECISION")
    private Long extendedPrecision;

    @Column(name = "START_DATE_ACTIVE")
    private LocalDateTime startDateActive;

    @Column(name = "END_DATE_ACTIVE")
    private LocalDateTime endDateActive;

    @Column(name = "ENABLED_FLAG", nullable = false)
    private String enabledFlag;

    @Column(name = "ATTRIBUTE1")
    private String attribute1;

    @Column(name = "ATTRIBUTE2")
    private String attribute2;

    @Column(name = "ATTRIBUTE3")
    private String attribute3;

    @Column(name = "ATTRIBUTE4")
    private String attribute4;

    @Column(name = "ATTRIBUTE5")
    private String attribute5;

}
