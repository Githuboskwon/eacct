package com.iljin.apiServer.ijeas.system.expend;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ExpendDto implements Serializable {

    String compCd;

    String expendCd;

    String startDate;

    String endDate;

    String wreathYn;

    String mutualYn;

    String holiday;

    String expendAmt;

    String remark;

    String remark1;

    String remark2;

    String remark3;

    String remark4;

    String remark5;

    String regId;

    LocalDateTime regDtm;

    String chgId;

    LocalDateTime chgDtm;

    String basicDate;

    String chgNm;

    /* 경조사 관리 - 리스트 */
    @QueryProjection
    public ExpendDto(String compCd, String expendCd, String startDate, String endDate, String wreathYn, String mutualYn,
                     String holiday, String expendAmt, String remark, String regId, LocalDateTime regDtm, String chgId, String chgNm, LocalDateTime chgDtm) {
        this.compCd = compCd;
        this.expendCd = expendCd;
        this.startDate = startDate;
        this.endDate = endDate;
        this.wreathYn = wreathYn;
        this.mutualYn = mutualYn;
        this.holiday = holiday;
        this.expendAmt = expendAmt;
        this.remark = remark;
        this.regId = regId;
        this.regDtm = regDtm;
        this.chgId = chgId;
        this.chgNm = chgNm;
        this.chgDtm = chgDtm;
    }


    /* 발생 전표 - 경조사 팝업 - 리스트 */
    @QueryProjection
    public ExpendDto(String remark2, String remark3, String remark5, String wreathYn, String mutualYn, String expendAmt, String expendCd, String holiday) {
        this.remark2 = remark2;
        this.remark3 = remark3;
        this.remark5 = remark5;
        this.wreathYn = wreathYn;
        this.mutualYn = mutualYn;
        this.expendAmt = expendAmt;
        this.expendCd = expendCd;
        this.holiday = holiday;
    }

}
