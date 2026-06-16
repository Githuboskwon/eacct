package com.iljin.apiServer.ijeas.system.cctr;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "TB_MST_CCTR_BLG_ERP")
@IdClass(CostCenterKey.class)
public class CostCenter extends BaseEntity {

    // 회사코드
    @Id
    @Column(name = "comp_cd", nullable = false)
    String compCd;

    // 귀속부서코드(ERP)
    @Id
    @Column(name = "dept_cd", nullable = false)
    String deptCd;

    // 귀속부서명(ERP)
    @Column(name = "dept_nm")
    String deptNm;

    // 시작일자
    @Column(name = "start_date_active")
    String startDateActive;

    // 종료일자
    @Column(name = "end_date_active")
    String endDateActive;

    // 사용여부
    @Column(name = "enabled_flag", nullable = false)
    String enabledFlag;

    // 속성 1
    @Column(name = "attribute1", nullable = false)
    String attribute1;

    // 속성 2
    @Column(name = "attribute2", nullable = false)
    String attribute2;

    // 속성 3
    @Column(name = "attribute3", nullable = false)
    String attribute3;

    // 속성 4
    @Column(name = "attribute4", nullable = false)
    String attribute4;

    // 속성 5
    @Column(name = "attribute5", nullable = false)
    String attribute5;

    // 속성 6
    @Column(name = "attribute6", nullable = false)
    String attribute6;

    // 속성 7
    @Column(name = "attribute7", nullable = false)
    String attribute7;

    // 속성 8
    @Column(name = "attribute8", nullable = false)
    String attribute8;

    // 속성 9
    @Column(name = "attribute9", nullable = false)
    String attribute9;

    // 속성 10
    @Column(name = "attribute10", nullable = false)
    String attribute10;

    // IF ID
    @Column(name = "picode")
    String picode;

    // IF 상태
    @Column(name = "pistat")
    String pistat;

    // IF 일자
    @Column(name = "pidate")
    String pidate;

    // IF 시간
    @Column(name = "pitime")
    String pitime;

    // IF 유저
    @Column(name = "piuser")
    String piuser;

    // 전송메시지
    @Column(name = "pimsg")
    String pimsg;

    // 메시지ID
    @Column(name = "pimsgid")
    String pimsgid;

    @Builder
    public CostCenter(String compCd, String deptCd, String deptNm, String startDateActive, String endDateActive, String enabledFlag, String attribute1
            , String attribute2, String attribute3, String attribute4, String attribute5, String attribute6, String attribute7, String attribute8
            , String attribute9, String attribute10, String picode, String pistat, String pidate, String pitime, String piuser, String pimsg, String pimsgid
                ) {
        this.compCd = compCd;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.startDateActive = startDateActive;
        this.endDateActive = endDateActive;
        this.enabledFlag = enabledFlag;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
        this.attribute6 = attribute6;
        this.attribute7 = attribute7;
        this.attribute8 = attribute8;
        this.attribute9 = attribute9;
        this.attribute10 = attribute10;
        this.picode = picode;
        this.pistat = pistat;
        this.pidate = pidate;
        this.pitime = pitime;
        this.piuser = piuser;
        this.pimsg = pimsg;
        this.pimsgid = pimsgid;
    }
    
}
