package com.iljin.apiServer.ijeas.slip.header;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SlipBusinessTripDto implements Serializable {

    private static final long serialVersionUID = 6309001589409011520L;

    String compCd;

    // 마스터 전표번호
    String masterSlipNo;

    // 마스터 헤더ID
    BigDecimal masterSlipHeaderId;

    // 순번
    Integer seq;

    // 장부코드
    Integer ledgerId;

    // 디테일 헤더ID
    BigDecimal detailSlipHeaderId;

    // 디테일 전표번호
    String detailSlipNo;

    // 디테일 회계일자
    String detailGlDt;

    // 디테일 거래유형
    String detailTrxType;

    // 디테일 공급자ID
    BigDecimal detailVendorId;

    // 디테일 공급자명
    String detailVendorNm;

    // 디테일 통화
    String detailUsedCur;

    // 디테일 금액
    BigDecimal detailUsedAmt;

    // 디테일 등록자
    String detailRegId;

    // 디테일 등록일자
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime detailRegDtm;


    @QueryProjection
    public SlipBusinessTripDto(String compCd,String masterSlipNo,BigDecimal masterSlipHeaderId,
                               Integer seq,Integer ledgerId,BigDecimal detailSlipHeaderId,
                               String detailSlipNo,String detailGlDt,String detailTrxType,
                               BigDecimal detailVendorId,String detailVendorNm,String detailUsedCur,
                               BigDecimal detailUsedAmt,String detailRegId,LocalDateTime detailRegDtm){
        this.compCd = compCd;
        this.masterSlipNo = masterSlipNo;
        this.masterSlipHeaderId = masterSlipHeaderId;
        this.seq = seq;
        this.ledgerId = ledgerId;
        this.detailSlipHeaderId = detailSlipHeaderId;
        this.detailSlipNo = detailSlipNo;
        this.detailGlDt = detailGlDt;
        this.detailTrxType = detailTrxType;
        this.detailVendorId = detailVendorId;
        this.detailVendorNm = detailVendorNm;
        this.detailUsedCur = detailUsedCur;
        this.detailUsedAmt = detailUsedAmt;
        this.detailRegId = detailRegId;
        this.detailRegDtm = detailRegDtm;
    }

}
