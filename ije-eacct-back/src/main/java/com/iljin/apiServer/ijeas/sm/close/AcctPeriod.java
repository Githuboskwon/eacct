package com.iljin.apiServer.ijeas.sm.close;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_close_mng")
@IdClass(AcctPeriodKey.class)
@Data
public class AcctPeriod {

    //회사코드
    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    //마감년월
    @Id
    @Column(name = "CLOS_YM", nullable = false)
    String closYm;

    //사업영역코드
    @Id
    @Column(name = "BA_CD", nullable = false)
    String baCd;

    //마감상태코드
    @Column(name = "CLOS_STAT_CD")
    String closStatCd;

    //메인기간여부
    @Column(name = "MAIN_CLOSE_YN")
    String mainCloseYn;

    //마감적요
    @Column(name = "CLOS_OLN")
    String closOln;

    //등록자ID
    @Column(name = "reg_id")
    String regId;

    //등록일시
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "reg_dtm")
    LocalDateTime regDtm;

    //수정자ID
    @Column(name = "chg_id")
    String chgId;

    //수정일시
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "chg_dtm")
    LocalDateTime chgDtm;
}
