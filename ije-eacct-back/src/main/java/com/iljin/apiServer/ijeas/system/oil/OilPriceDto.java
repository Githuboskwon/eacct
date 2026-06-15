package com.iljin.apiServer.ijeas.system.oil;

import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OilPriceDto implements Serializable {

    //회사코드
    String compCd;
    //기준년월
    String baseYm;
    //유종코드
    String oilKindCd;

    String oilKindNm;
    //유류단가
    BigDecimal oilUpce;
    //연비
    BigDecimal oilEff;

    // 등록자ID
    String regId;

    // 등록일시
    LocalDateTime regDtm;

    // 수정자ID
    String chgId;

    // 수정일시
    LocalDateTime chgDtm;

    String chgNm;

    String remark;


    /* EA-06-11 유류단가관리 - 리스트 */
    @QueryProjection
    public OilPriceDto(String compCd, String baseYm, String oilKindCd, String oilKindNm, BigDecimal oilUpce, BigDecimal oilEff,
                       String regId, LocalDateTime regDtm, String chgId, String chgNm, LocalDateTime chgDtm, String remark) {
        this.compCd = compCd;
        this.baseYm = baseYm;
        this.oilKindCd = oilKindCd;
        this.oilKindNm = oilKindNm;
        this.oilUpce = oilUpce;
        this.oilEff = oilEff;
        this.regId = regId;
        this.regDtm = regDtm;
        this.chgId = chgId;
        this.chgNm =  chgNm;
        this.chgDtm = chgDtm;
        this.remark = remark;
    }

    @QueryProjection
    public OilPriceDto(String compCd, String baseYm, String oilKindCd, String oilKindNm,
        BigDecimal oilUpce, BigDecimal oilEff, String remark) {
        this.compCd = compCd;
        this.baseYm = baseYm;
        this.oilKindCd = oilKindCd;
        this.oilKindNm = oilKindNm;
        this.oilUpce = oilUpce;
        this.oilEff = oilEff;
        this.remark = remark;
    }
}
