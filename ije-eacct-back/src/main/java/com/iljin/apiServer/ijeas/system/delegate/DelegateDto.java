package com.iljin.apiServer.ijeas.system.delegate;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class DelegateDto {

    String compCd;

    String giveUserId;

    String receiveUserId;

    BigDecimal delegateSeq;

    String delegateStatCd;

    String startDate;

    String endDate;

    String  remark;

    String  regId;

    LocalDateTime regDtm;

    String chgId;

    LocalDateTime chgDtm;

    String modGiveUserId;

    String modReceiveUserId;

    String chgNm;

    String receiveUserNm;

    String giveUserNm;

    String search;

    /*
     * 조회 클릭 event
     * 위임관리 리스트 조회
     * */
    @Builder
    @QueryProjection
    public DelegateDto(String compCd, String giveUserId, String giveUserNm, String receiveUserId, String receiveUserNm, BigDecimal delegateSeq, String delegateStatCd, String startDate
            , String endDate , String remark, String regId, LocalDateTime regDtm, String chgId, String chgNm, LocalDateTime chgDtm
    ) {
        this.compCd = compCd;
        this.giveUserId = giveUserId;
        this.giveUserNm = giveUserNm;
        this.receiveUserId = receiveUserId;
        this.receiveUserNm = receiveUserNm;
        this.delegateSeq = delegateSeq;
        this.delegateStatCd = delegateStatCd;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remark = remark;
        this.regId = regId;
        this.regDtm = regDtm;
        this.chgId = chgId;
        this.chgNm = chgNm;
        this.chgDtm = chgDtm;
    }

    String deptCd;
    String deptNm;

    String giveCctrCd;

    String giveCctrNm;

    @QueryProjection
    public DelegateDto(String giveUserId, String giveUserNm, String deptCd, String deptNm,
                       String receiveUserId, String receiveUserNm, String giveCctrCd, String giveCctrNm) {
        this.giveUserId = giveUserId;
        this.giveUserNm = giveUserNm;
        this.receiveUserId = receiveUserId;
        this.receiveUserNm = receiveUserNm;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.giveCctrCd = giveCctrCd;
        this.giveCctrNm = giveCctrNm;
    }
}
