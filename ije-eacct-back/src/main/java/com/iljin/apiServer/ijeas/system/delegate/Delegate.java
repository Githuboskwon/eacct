package com.iljin.apiServer.ijeas.system.delegate;


import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "TB_MST_DELEGATE")
@IdClass(DelegateKey.class)
public class Delegate extends BaseEntity {

    // 회사코드
    @Id
    @Column(name = "COMP_CD", nullable = false)
    private String compCd;

    // 위임자
    @Id
    @Column(name = "GIVE_USER_ID", nullable = false)
    private String giveUserId;

    // 수임자
    @Id
    @Column(name = "RECEIVE_USER_ID")
    private String receiveUserId;

    // 위임순번
    @Id
    @Column(name = "DELEGATE_SEQ")
    private BigDecimal delegateSeq;

    // 위임상태
    @Column(name = "DELEGATE_STAT_CD")
    private String delegateStatCd;

    // 위임시작일자
    @Column(name = "START_DATE")
    private String startDate;

    // 위임종료일자
    @Column(name = "END_DATE")
    private String endDate;

    // 비고
    @Column(name = "REMARK")
    private String remark;



    @Builder
    public Delegate(String compCd, String giveUserId, String receiveUserId, BigDecimal delegateSeq, String delegateStatCd, String startDate
            , String endDate , String remark
    ) {
        this.compCd = compCd;
        this.giveUserId = giveUserId;
        this.receiveUserId = receiveUserId;
        this.delegateSeq = delegateSeq;
        this.delegateStatCd = delegateStatCd;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remark = remark;
    }

    public void update(DelegateDto delegateDto){
        this.compCd = delegateDto.getCompCd();
        this.giveUserId = delegateDto.getGiveUserId();
        this.receiveUserId = delegateDto.getReceiveUserId();
        this.startDate = delegateDto.getStartDate();
        this.endDate = delegateDto.getEndDate();
        this.remark = delegateDto.getRemark();
    }

    public void delete(DelegateDto delegateDto){
        this.compCd = delegateDto.getCompCd();
        this.giveUserId = delegateDto.getGiveUserId();
        this.receiveUserId = delegateDto.getReceiveUserId();
        this.delegateStatCd = "2";
    }



}
