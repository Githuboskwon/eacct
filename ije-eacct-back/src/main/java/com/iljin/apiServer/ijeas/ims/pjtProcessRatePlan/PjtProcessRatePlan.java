package com.iljin.apiServer.ijeas.ims.pjtProcessRatePlan;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@NoArgsConstructor
@IdClass(PjtProcessRatePlanKey.class)
@Table(name = "SPJPPM")
@Entity
public class PjtProcessRatePlan {

    // ORG_ID
    @Id
    @Column(name = "ORG_ID",  nullable = false)
    String orgId;

    // PJT관리번호
    @Id
    @Column(name = "PROJECT_MANAGE_NO",  nullable = false)
    String projectMngNo;

    // 년도
    @Column(name = "PERIOD_YEAR",  nullable = false)
    String periodYear;

    // 월
    @Column(name = "PERIOD_MONTH")
    String periodMonth;

    // 년월
    @Id
    @Column(name = "YYYYMM",  nullable = false)
    String yyyyMm;

    // M TEXT
    @Id
    @Column(name = "M_TEXT",  nullable = false)
    String mText;

    @Column(name = "ITEM_CODE",  nullable = false)
    String itemCode;

    @Column(name = "ITEM_Name",  nullable = false)
    String itemName;

    @Column(name = "OBJECTIVE_ALL")
    String objectiveAll;

    @Column(name = "OBJECTIVE_MON")
    String objectiveMon;

    //비고
    @Column(name = "REMARK")
    String remark;

    // 작성일
    @Column(name = "ADD_DATE")
    String addDate;

    // 작성시간
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

    //
    @Id
    @Column(name = "DEGREE",  nullable = false)
    String degree;

    @Builder

    public PjtProcessRatePlan(String orgId, String projectMngNo, String periodYear, String periodMonth, String yyyyMm,
                              String mText, String itemCode , String objectiveAll, String objectiveMon, String remark,
                              String addDate, String addTime, String addUserId, String changeDate, String changeTime,
                              String changeUserId, String degree) {
        this.orgId = orgId;
        this.projectMngNo = projectMngNo;
        this.periodYear = periodYear;
        this.periodMonth = periodMonth;
        this.yyyyMm = yyyyMm;
        this.mText = mText;
        this.itemCode = itemCode;
        this.objectiveAll = objectiveAll;
        this.objectiveMon = objectiveMon;
        this.remark = remark;
        this.addDate = addDate;
        this.addTime = addTime;
        this.addUserId = addUserId;
        this.changeDate = changeDate;
        this.changeTime = changeTime;
        this.changeUserId = changeUserId;
        this.degree = degree;
    }

}

