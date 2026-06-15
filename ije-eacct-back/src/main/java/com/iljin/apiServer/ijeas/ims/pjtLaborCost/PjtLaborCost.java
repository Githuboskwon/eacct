package com.iljin.apiServer.ijeas.ims.pjtLaborCost;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@IdClass(PjtLaborCostKey.class)
@Table(name = "SPJPEM")
@Entity
public class PjtLaborCost {

    // 직책코드
    @Id
    @Column(name = "ORG_ID",  nullable = false)
    String orgId;
    
    //PJT관리번호
    @Id
    @Column(name = "PROJECT_MANAGE_NO",  nullable = false)
    String projectMngNo;

    //직책코드
    @Id
    @Column(name = "POSITION_CODE",  nullable = false)
    String positionCd;

    //인건비
    @Column(name = "AMT")
    String amt;

    //비고
    @Column(name = "REMARK")
    String remark;

    //작성일
    @Column(name = "ADD_DATE")
    String addDate;

    //작성시간
    @Column(name = "ADD_TIME")
    String addTime;

    //작성자
    @Column(name = "ADD_USER_ID")
    String addUserId;

    //수정일
    @Column(name = "CHANGE_DATE")
    String changeDate;

    //수정시간
    @Column(name = "CHANGE_TIME")
    String changeTime;

    //수정자
    @Column(name = "CHANGE_USER_ID")
    String changeUserId;

    //비용구분 A변동비 B고정비
    @Column(name = "AMT_TYPE")
    String amtType;

    @Column(name = "ACCT_CODE")
    String acctCode;

    @Column(name = "ACCT_NAME")
    String acctName;

    @Builder
    public PjtLaborCost(String orgId, String projectMngNo, String positionCd, String amt,
                        String remark, String addDate, String addTime, String addUserId,
                        String changeDate, String changeTime, String changeUserId,
                        String amtType, String acctCode, String acctName) {
        this.orgId = orgId;
        this.projectMngNo = projectMngNo;
        this.positionCd = positionCd;
        this.amt = amt;
        this.remark = remark;
        this.addDate = addDate;
        this.addTime = addTime;
        this.addUserId = addUserId;
        this.changeDate = changeDate;
        this.changeTime = changeTime;
        this.changeUserId = changeUserId;
        this.amtType = amtType;
        this.acctCode = acctCode;
        this.acctName = acctName;
    }

    public PjtLaborCost update(PjtLaborCostDto dto) {
        this.amt = dto.getAmt();
        this.amtType = dto.getAmtType();
        this.acctCode = dto.getAcctCode();
        this.acctName = dto.getAcctName();
        this.remark = dto.getRemark();
        this.changeDate = dto.getChangeDate();
        this.changeTime = dto.getChangeTime();
        this.changeUserId = dto.getChangeUserId();
        this.positionCd = dto.getPositionCd();
        return this;
    }
}
