package com.iljin.apiServer.ijeas.ims.spjpbm;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@NoArgsConstructor
@IdClass(SpjpbmKey.class)
@Table(name = "SPJPBM")
@Entity
public class Spjpbm {

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

    // 개월수(M+n)
    @Id
    @Column(name = "M_TEXT",  nullable = false)
    String mText;

    // 현장,본사구분타입(사용안함)
    @Column(name = "AMT_TYPE",  nullable = false)
    String amtType;

    // 계정코드
    @Column(name = "ACCT_CODE")
    String acctCode;

    //계정명
    @Id
    @Column(name = "ACCT_NAME",  nullable = false)
    String acctName;

    //금액
    @Column(name = "AMT")
    String amt;

    //비고
    @Column(name = "REMARK")
    String remark;

    //정렬순서
    @Column(name = "ORDER_SEQ")
    String orderSeq;

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

    //
    @Id
    @Column(name = "DEGREE",  nullable = false)
    String degree;

    @Builder
    public Spjpbm(String orgId, String projectMngNo, String periodYear, String periodMonth, String yyyyMm,
                  String mText, String amtType, String acctCode, String acctName, String amt, String remark,
                  String orderSeq, String addDate, String addTime, String addUserId, String changeDate, String changeTime,
                  String changeUserId, String degree) {
        this.orgId = orgId;
        this.projectMngNo = projectMngNo;
        this.periodYear = periodYear;
        this.periodMonth = periodMonth;
        this.yyyyMm = yyyyMm;
        this.mText = mText;
        this.amtType = amtType;
        this.acctCode = acctCode;
        this.acctName = acctName;
        this.amt = amt;
        this.remark = remark;
        this.orderSeq = orderSeq;
        this.addDate = addDate;
        this.addTime = addTime;
        this.addUserId = addUserId;
        this.changeDate = changeDate;
        this.changeTime = changeTime;
        this.changeUserId = changeUserId;
        this.degree = degree;
    }

}

