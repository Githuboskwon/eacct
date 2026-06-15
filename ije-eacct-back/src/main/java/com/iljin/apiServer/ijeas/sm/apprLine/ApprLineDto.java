package com.iljin.apiServer.ijeas.sm.apprLine;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ApprLineDto implements Serializable {

    private static final long serialVersionUID = -5732632334883441097L;
    String compCd;
    String userId;
    String userNm;
    Integer apprSeq;
    String apprLineTitle;
    String mainApprYn;
    String remark;
    String useYn;
    Integer subApprSeq;
    String apprUserId;
    String apprTypeCd;
    String regId;
    LocalDateTime regDtm;
    String chgId;
    LocalDateTime chgDtm;
    String apprUserNm;
    String jobCd;
    String jobNm;
    String cctrCd;
    String cctrNm;
    String serveCd;
    String deptCd;
    String deptNm;

    @Builder
    @QueryProjection
    public ApprLineDto(String compCd, String userId, Integer apprSeq, String apprLineTitle, String mainApprYn, String remark, String useYn,
                        String regId, LocalDateTime regDtm, String chgId, LocalDateTime chgDtm){
        this.compCd = compCd;
        this.userId = userId;
        this.apprSeq = apprSeq;
        this.apprLineTitle = apprLineTitle;
        this.mainApprYn = mainApprYn;
        this.remark = remark;
        this.useYn = useYn;
        this.regId= regId;
        this.regDtm = regDtm;
        this.chgId = chgId;
        this.chgDtm = chgDtm;
    }

    @QueryProjection
    public ApprLineDto(String compCd, String userId, String userNm, Integer apprSeq, String apprLineTitle, String mainApprYn, String remark, String useYn,
                       String regId, LocalDateTime regDtm, String chgId, LocalDateTime chgDtm, String temp){
        this.compCd = compCd;
        this.userId = userId;
        this.userNm = userNm;
        this.apprSeq = apprSeq;
        this.apprLineTitle = apprLineTitle;
        this.mainApprYn = mainApprYn;
        this.remark = remark;
        this.useYn = useYn;
        this.regId= regId;
        this.regDtm = regDtm;
        this.chgId = chgId;
        this.chgDtm = chgDtm;
    }

    @QueryProjection
    public ApprLineDto(String compCd, String userId, Integer apprSeq, Integer subApprSeq, String apprUserId, String apprTypeCd){
        this.compCd = compCd;
        this.userId = userId;
        this.apprSeq = apprSeq;
        this.subApprSeq = subApprSeq;
        this.apprUserId = apprUserId;
        this.apprTypeCd = apprTypeCd;
    }

    @QueryProjection
    public ApprLineDto(String compCd, String userId, Integer apprSeq, Integer subApprSeq, String apprUserId, String apprTypeCd,
                       String apprUserNm, String jobCd, String jobNm, String deptCd, String deptNm, String serveCd){
        this.compCd = compCd;
        this.userId = userId;
        this.apprSeq = apprSeq;
        this.subApprSeq = subApprSeq;
        this.apprUserId = apprUserId;
        this.apprTypeCd = apprTypeCd;
        this.apprUserNm = apprUserNm;
        this.jobCd = jobCd;
        this.jobNm = jobNm;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.serveCd = serveCd;
    }
}
