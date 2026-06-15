package com.iljin.apiServer.ijeas.ims.pjtLaborCost;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class PjtLaborCostDto implements Serializable {

    // 법인코드
    String orgId;

    //PJT관리번호
    String projectMngNo;

    //직책코드
    String positionCd;

    //인건비
    String amt;

    //비고
    String remark;

    //작성일
    String addDate;

    //작성시간
    String addTime;

    //작성자
    String addUserId;

    //수정일
    String changeDate;

    //수정시간
    String changeTime;

    //수정자
    String changeUserId;

    //비용구분 A변동비 B고정비
    String amtType;

    String acctCode;

    String acctName;

    @QueryProjection
    public PjtLaborCostDto(String orgId, String projectMngNo, String positionCd, String amt, String remark, String addDate, String addTime,
                           String addUserId, String changeDate, String changeTime, String changeUserId, String amtType, String acctCode, String acctName) {
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
}
