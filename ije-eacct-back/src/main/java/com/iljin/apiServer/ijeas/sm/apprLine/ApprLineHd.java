package com.iljin.apiServer.ijeas.sm.apprLine;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;

import jakarta.persistence.*;

@Entity
@Getter
@Table(name = "tb_appr_line_hd")
@IdClass(ApprLineKey.class)
public class ApprLineHd extends BaseEntity {
    @Id
    @Column(name = "COMP_CD")
    String compCd;

    @Id
    @Column(name = "USER_ID")
    String userId;

    @Id
    @Column(name = "APPR_SEQ")
    Integer apprSeq;

    @Column(name = "APPR_LINE_TITLE")
    String apprLineTitle;

    @Column(name = "MAIN_APPR_YN")
    String mainApprYn;

    @Column(name = "REMARK")
    String remark;

    @Column(name = "USE_YN")
    String useYn;

    @Builder
    public ApprLineHd(String compCd, String userId, Integer apprSeq, String apprLineTitle, String mainApprYn, String remark, String useYn) {
        this.compCd = compCd;
        this.userId = userId;
        this.apprSeq = apprSeq;
        this.apprLineTitle = apprLineTitle;
        this.mainApprYn = mainApprYn;
        this.remark = remark;
        this.useYn = useYn;
    }

    public ApprLineHd() {

    }
}
