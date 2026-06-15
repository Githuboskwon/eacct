package com.iljin.apiServer.ijeas.sm.close;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class AcctPeriodDto implements Serializable {

    private static final long serialVersionUID = -8966685728227171036L;

    //회사코드
    String compCd;
    //마감년월
    String closYm;
    //사업영역코드
    String baCd;
    String baNm;
    //마감상태코드
    String closStatCd;
    String closStatNm;
    //수정자ID
    String chgId;
    String chgNm;
    //수정일시
    Timestamp chgDtm;

    // 회계년월 from
    String closeYmFrom;

    // 회계년월 to
    String closeYmTo;

    // 회계년월 시작날짜
    String openDay;

    // 회계년월 종료날짜
    String closeDay;

    String flag;


    // 메인기간 여부
    String mainCloseYn;


    /* EA-06-04 회계기간관리 리스트 조회 */
    public AcctPeriodDto(String compCd, String baCd, String baNm, String closYm, String closStatCd, String closStatNm, String mainCloseYn, String chgId, String chgNm, Timestamp chgDtm) {
        this.compCd = compCd;
        this.baCd = baCd;
        this.baNm = baNm;
        this.closYm = closYm;
        this.closStatCd = closStatCd;
        this.closStatNm = closStatNm;
        this.mainCloseYn = mainCloseYn;
        this.chgId = chgId;
        this.chgNm = chgNm;
        this.chgDtm = chgDtm;
    }

    /* EA-06-04 오픈 회계기간 리스트 조회 */
    @QueryProjection
    public AcctPeriodDto(String closYm, String closStatCd) {
        this.closYm = closYm;
        this.closStatCd = closStatCd;
    }


    /* 발생전표 회계기간 OPEN 날짜 조회 */
    public AcctPeriodDto(String openDay, String closeDay, String flag) {
        this.openDay = openDay;
        this.closeDay = closeDay;
        this.flag = flag;
    }
}
