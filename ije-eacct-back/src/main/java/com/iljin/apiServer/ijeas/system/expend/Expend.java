package com.iljin.apiServer.ijeas.system.expend;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "TB_EXPEND_MONEY")
@Getter
@IdClass(ExpendKey.class)
public class Expend extends BaseEntity {

    //회사코드
    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    //경조구분코드
    @Id
    @Column(name = "EXPEND_CD", nullable = false)
    String expendCd;

    //시작일자
    @Id
    @Column(name = "START_DATE", nullable = false)
    String startDate;


    //종료일자
    @Column(name = "END_DATE")
    String endDate;

    //화환구분
    @Column(name = "WREATH_YN")
    String wreathYn;

    //상조용품구분
    @Column(name = "MUTUAL_YN")
    String mutualYn;

    //휴일
    @Column(name = "HOLIDAY")
    String holiday;

    //경조금(원)
    @Column(name = "EXPEND_AMT")
    String expendAmt;

    //비고
    @Column(name = "REMARK")
    String remark;


    @Builder
    public Expend(String compCd, String expendCd, String startDate, String endDate, String wreathYn, String mutualYn,
                  String holiday, String expendAmt, String remark)
    {
        this.compCd = compCd;
        this.expendCd = expendCd;
        this.startDate = startDate;
        this.endDate = endDate;
        this.wreathYn = wreathYn;
        this.mutualYn = mutualYn;
        this.holiday = holiday;
        this.expendAmt = expendAmt;
        this.remark = remark;
    }

    public Expend updateExpend(String expendCd, String endDate, String wreathYn, String mutualYn, String holiday, String expendAmt, String remark){
        this.expendCd = expendCd;
        this.endDate = endDate;
        this.wreathYn = wreathYn;
        this.mutualYn = mutualYn;
        this.holiday = holiday;
        this.expendAmt = expendAmt;
        this.remark = remark;
        return this;
    }
}
