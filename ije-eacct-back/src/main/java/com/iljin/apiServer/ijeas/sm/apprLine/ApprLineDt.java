package com.iljin.apiServer.ijeas.sm.apprLine;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;

import jakarta.persistence.*;

@Entity
@Getter
@Table(name = "tb_appr_line_dt")
@IdClass(ApprLineDtKey.class)
public class ApprLineDt extends BaseEntity {
    @Id
    @Column(name = "COMP_CD")
    String compCd;

    @Id
    @Column(name = "USER_ID")
    String userId;

    @Id
    @Column(name = "APPR_SEQ")
    Integer apprSeq;

    @Id
    @Column(name = "SUB_APPR_SEQ")
    Integer subApprSeq;

    @Column(name = "APPR_USER_ID")
    String apprUserId;

    @Column(name = "APPR_TYPE_CD")
    String apprTypeCd;


    @Builder
    public ApprLineDt(String compCd, String userId, Integer apprSeq, Integer subApprSeq, String apprUserId, String apprTypeCd) {
        this.compCd = compCd;
        this.userId = userId;
        this.apprSeq = apprSeq;
        this.subApprSeq = subApprSeq;
        this.apprUserId = apprUserId;
        this.apprTypeCd = apprTypeCd;
    }

    public ApprLineDt() {

    }
}
