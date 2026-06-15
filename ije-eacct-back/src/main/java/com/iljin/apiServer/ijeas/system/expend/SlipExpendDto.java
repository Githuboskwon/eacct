package com.iljin.apiServer.ijeas.system.expend;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class SlipExpendDto implements Serializable {

    String compCd;

    String slipNo;

    BigDecimal slipHeaderId;

    // 경조구분코드
    String expendCd;

    // 대상자
    String expendReceiveNm;

    // 경조관계. 신청인과의 관계
    String expendRelation;

    // 경조일자
    String expendDt;

    // 경조금(원). 회사지원경조금
    String expendAmt;

    // 화환조화 구분
    String wreathYn;

    // 휴가일
    String holiday;

    // 상조용품(YN)
    String mutualYn;

    String temp1;

    String temp2;

    String temp3;

    String temp4;

    String temp5;

    String regId;

    LocalDateTime regDtm;

    String chgId;

    String chgNm;

    LocalDateTime chgDtm;

    String postingDt;

    String integrationVendorNum;

    String integrationVendorName;

    String startDate;

    String endDate;

    String empNo;


    /* 경조사 관리 - 리스트 */
    @QueryProjection
    public SlipExpendDto(String compCd, String slipNo, BigDecimal slipHeaderId, String expendCd,
                         String expendReceiveNm, String expendRelation, String expendDt, String expendAmt,
                         String wreathYn, String mutualYn, String holiday,
                         String integrationVendorNum, String integrationVendorName, String postingDt,
                         String chgId, String chgNm ,LocalDateTime chgDtm) {
        this.compCd = compCd ;
        this.slipNo = slipNo ;
        this.slipHeaderId = slipHeaderId ;
        this.expendCd = expendCd ;
        this.expendReceiveNm = expendReceiveNm;
        this.expendRelation = expendRelation ;
        this.expendDt = expendDt ;
        this.expendAmt = expendAmt ;
        this.wreathYn = wreathYn ;
        this.mutualYn = mutualYn ;
        this.holiday = holiday ;
        this.integrationVendorNum = integrationVendorNum;
        this.integrationVendorName = integrationVendorName;
        this.postingDt = postingDt;
        this.chgId = chgId ;
        this.chgNm = chgNm;
        this.chgDtm = chgDtm ;
    }

    String expendNm;

    @QueryProjection
    public SlipExpendDto(String compCd, String slipNo, BigDecimal slipHeaderId, String expendCd, String expendNm,
                         String expendReceiveNm, String expendRelation, String expendDt, String expendAmt,
                         String wreathYn, String mutualYn, String holiday) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipHeaderId = slipHeaderId;
        this.expendCd = expendCd;
        this.expendNm = expendNm;
        this.expendReceiveNm = expendReceiveNm;
        this.expendRelation = expendRelation;
        this.expendDt = expendDt;
        this.expendAmt = expendAmt;
        this.wreathYn = wreathYn;
        this.mutualYn = mutualYn;
        this.holiday = holiday;
    }
}
