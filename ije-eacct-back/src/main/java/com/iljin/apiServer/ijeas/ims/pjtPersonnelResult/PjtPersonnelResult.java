package com.iljin.apiServer.ijeas.ims.pjtPersonnelResult;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@IdClass(PjtPersonnelResultKey.class)
@Table(name = "SPJEBM")
@Entity
public class PjtPersonnelResult {

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

    // 사번
    @Column(name = "USER_ID",  nullable = false)
    String userId;

    // 이름
    @Column(name = "USER_NAME")
    String userNm;

    //직책코드
    @Id
    @Column(name = "POSITION_CODE",  nullable = false)
    String positionCd;

    //직책
    @Column(name = "POSITION_NAME")
    String positionNm;

    //비고
    @Column(name = "REMARK")
    String remark;

    // 일자 1 -- 20170111 총근무일수로만 계산변경 D1 계획총일자
    @Column(name = "D1")
    String d1;

    //
//    @Column(name = "D2")
//    String d2;
//
//    //
//    @Column(name = "D3")
//    String d3;
//
//    //
//    @Column(name = "D4")
//    String d4;
//
//    //
//    @Column(name = "D5")
//    String d5;
//
//    //
//    @Column(name = "D6")
//    String d6;
//
//    //
//    @Column(name = "D7")
//    String d7;
//
//    //
//    @Column(name = "D8")
//    String d8;
//
//    //
//    @Column(name = "D9")
//    String d9;
//
//    //
//    @Column(name = "D10")
//    String d10;
//
//    //
//    @Column(name = "D11")
//    String d11;
//
//    //
//    @Column(name = "D12")
//    String d12;
//
//    //
//    @Column(name = "D13")
//    String d13;
//
//    //
//    @Column(name = "D14")
//    String d14;
//
//    //
//    @Column(name = "D15")
//    String d15;
//
//    //
//    @Column(name = "D16")
//    String d16;
//
//    //
//    @Column(name = "D17")
//    String d17;
//
//    //
//    @Column(name = "D18")
//    String d18;
//
//    //
//    @Column(name = "D19")
//    String d19;
//
//    //
//    @Column(name = "D20")
//    String d20;
//
//    //
//    @Column(name = "D21")
//    String d21;
//
//    //
//    @Column(name = "D22")
//    String d22;
//
//    //
//    @Column(name = "D23")
//    String d23;
//
//    //
//    @Column(name = "D24")
//    String d24;
//
//    //
//    @Column(name = "D25")
//    String d25;
//
//    //
//    @Column(name = "D26")
//    String d26;
//
//    //
//    @Column(name = "D27")
//    String d27;
//
//    //
//    @Column(name = "D28")
//    String d28;
//
//    //
//    @Column(name = "D29")
//    String d29;
//
//    //
//    @Column(name = "D30")
//    String d30;
//
//    // 일자 31
//    @Column(name = "D31")
//    String d31;

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
    public PjtPersonnelResult(String orgId, String projectMngNo, String degree, String periodYear, String periodMonth, String yyyyMm,
                              String mText, String positionCd, String remark,
                              String d1, String addDate, String addTime, String addUserId) {
        this.orgId = orgId;
        this.projectMngNo = projectMngNo;
        this.degree = degree;
        this.periodYear = periodYear;
        this.periodMonth = periodMonth;
        this.yyyyMm = yyyyMm;
        this.mText = mText;
        this.positionCd = positionCd;
        this.remark = remark;
        this.d1 = d1;
        this.addDate = addDate;
        this.addTime = addTime;
        this.addUserId = addUserId;
    }

}

