package com.iljin.apiServer.ijeas.system.benCountry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
