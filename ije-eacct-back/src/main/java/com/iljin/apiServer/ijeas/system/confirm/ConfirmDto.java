package com.iljin.apiServer.ijeas.system.confirm;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ConfirmDto {

    String compCd;

    String deptCd;

    String confirmUserId;

    String confirmSeq;

    BigDecimal confirmStartAmt;

    BigDecimal confirmEndAmt;

    String  remark;

    String  regId;

    LocalDateTime regDtm;

    String chgId;

    String chgNm;

    LocalDateTime chgDtm;

    String modDeptCd;

    String modConfirmUserId;

    String modConfirmSeq;

    BigDecimal modConfirmStartAmt;

    BigDecimal modConfirmEndAmt;

    String modRemark;

    String confirmUserNm;

    String deptNm;


    /*
     * 조회 클릭 event
     * 검인관리 리스트 조회
     * */
    @QueryProjection
    public ConfirmDto(String compCd, String deptCd, String deptNm, String confirmUserId, String confirmUserNm, String confirmSeq, BigDecimal confirmStartAmt
            , BigDecimal confirmEndAmt
            , String remark, String regId, LocalDateTime regDtm, String chgId, String chgNm, LocalDateTime chgDtm
            ) {
        this.compCd = compCd;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.confirmUserId = confirmUserId;
        this.confirmUserNm = confirmUserNm;
        this.confirmSeq = confirmSeq;
        this.confirmStartAmt = confirmStartAmt;
        this.confirmEndAmt = confirmEndAmt;
        this.remark = remark;
        this.regId = regId;
        this.regDtm = regDtm;
        this.chgId = chgId;
        this.chgNm = chgNm;
        this.chgDtm = chgDtm;
    }

}
