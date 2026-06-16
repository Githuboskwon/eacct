package com.iljin.apiServer.ijeas.system.oil;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "tb_std_oil_upce")
@Getter
@IdClass(OilPriceKey.class)
public class OilPrice extends BaseEntity {

    //회사코드
    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    //기준년월
    @Id
    @Column(name = "BASE_YM", nullable = false)
    String baseYm;

    //유종코드
    @Id
    @Column(name = "OIL_KIND_CD", nullable = false)
    String oilKindCd;


    //유류단가
    @Column(name = "OIL_UPCE")
    BigDecimal oilUpce;

    //연비
    @Column(name = "OIL_EFF")
    BigDecimal oilEff;

    @Column(name = "REMARK")
    String remark;


    @Builder
    public OilPrice ( String compCd, String baseYm, String oilKindCd, BigDecimal oilUpce, BigDecimal oilEff,
                      String remark)
    {
        this.compCd = compCd;
        this.baseYm = baseYm;
        this.oilKindCd = oilKindCd;
        this.oilUpce = oilUpce;
        this.oilEff = oilEff;
        this.remark = remark;
    }

    public OilPrice update (OilPriceDto dto) {
        this.baseYm = dto.getBaseYm();
        this.oilKindCd = dto.getOilKindCd();
        this.oilUpce = dto.getOilUpce();
        this.oilEff = dto.getOilEff();
        this.remark = dto.getRemark();
        return this;
    }
}
