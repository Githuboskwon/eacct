package com.iljin.apiServer.ijeas.system.cctr;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class CostCenterDto {

    // 회사코드
    String compCd;

    // 귀속부서코드(ERP)
    String deptCd;

    // 귀속부서명(ERP)
    String deptNm;

    // 시작일자
    String startDateActive;

    // 종료일자
    String endDateActive;

    // 사용여부
    String enabledFlag;

    // 속성 1
    String attribute1;

    // 속성 2
    String attribute2;

    // 속성 3
    String attribute3;

    // 속성 4
    String attribute4;

    // 속성 5
    String attribute5;

    // 속성 6
    String attribute6;

    // 속성 7
    String attribute7;

    // 속성 8
    String attribute8;

    // 속성 9
    String attribute9;

    // 속성 10
    String attribute10;

    // IF ID
    String picode;

    // IF 상태
    String pistat;

    // IF 일자
    String pidate;

    // IF 시간
    String pitime;

    // IF 유저
    String piuser;

    // 전송메시지
    String pimsg;

    // 메시지ID
    String pimsgid;

    @QueryProjection
    public CostCenterDto(String compCd, String deptCd, String deptNm, String startDateActive, String endDateActive, String enabledFlag, String attribute1
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

    String postingDate;
    Integer personId;
    String deptType;


    @QueryProjection
    public CostCenterDto(String compCd, String deptCd, String deptNm, String deptType) {
        this.compCd = compCd;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.deptType = deptType;
    }

    @QueryProjection
    public CostCenterDto(String compCd, String deptCd, String deptNm, String attribute2, Integer personId, String postingDate) {
        this.compCd = compCd;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.attribute2 = attribute2;
        this.personId = personId;
        this.postingDate = postingDate;
    }

    String search;
    String empNo;
    public CostCenterDto(String search, String empNo){
        this.search = search;
        this.empNo = empNo;
    }
}
