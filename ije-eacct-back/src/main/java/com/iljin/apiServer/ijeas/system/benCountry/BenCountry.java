package com.iljin.apiServer.ijeas.system.benCountry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Getter
@NoArgsConstructor
@Immutable
@Table(name ="CBO_EST_TERRITORY_V")
@Entity
public class BenCountry {

    @Id
    @Column(name ="TERRITORY_CODE", nullable = false)
    String benCountryCd;

    @Column(name = "TERRITORY_SHORT_NAME", nullable = false)
    String benCountryNm;

    @Column(name = "ALTERNATE_TERRITORY_CODE")
    String alternateBenCountryCd;

    @Column(name = "DESCRIPTION")
    String description;
}
