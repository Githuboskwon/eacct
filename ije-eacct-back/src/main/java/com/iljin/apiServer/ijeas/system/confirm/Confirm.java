package com.iljin.apiServer.ijeas.system.confirm;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "TB_CONFIRM_LINE")
@IdClass(ConfirmKey.class)
@NoArgsConstructor
public class Confirm extends BaseEntity {

    // 회사코드
    @Id
    @Column(name = "COMP_CD", nullable = false)
    private String compCd;

    // 부서코드
    @Id
    @Column(name = "DEPT_CD", nullable = false)
    private String deptCd;

    // 검인자사번
    @Id
    @Column(name = "CONFIRM_USER_ID")
    private String confirmUserId;

    // 검인순서
    @Id
    @Column(name = "CONFIRM_SEQ")
    private String confirmSeq;

    // 검인기준시작금액
    @Column(name = "CONFIRM_START_AMT")
    private BigDecimal confirmStartAmt;

    // 검인기준종료금액
    @Column(name = "CONFIRM_END_AMT")
    private BigDecimal confirmEndAmt;

    // 비고
    @Column(name = "REMARK")
    private String remark;


    @Builder
    public Confirm(String compCd, String deptCd, String confirmUserId, String confirmSeq, BigDecimal confirmStartAmt
            , BigDecimal confirmEndAmt , String remark) {
        this.compCd = compCd;
        this.deptCd = deptCd;
        this.confirmUserId = confirmUserId;
        this.confirmSeq = confirmSeq;
        this.confirmStartAmt = confirmStartAmt;
        this.confirmEndAmt = confirmEndAmt;
        this.remark = remark;
    }

    public void update(ConfirmDto confirmDto){
        this.confirmStartAmt = confirmDto.getConfirmStartAmt();
        this.confirmEndAmt = confirmDto.getConfirmEndAmt();
        this.remark = confirmDto.getRemark();
    }

}
