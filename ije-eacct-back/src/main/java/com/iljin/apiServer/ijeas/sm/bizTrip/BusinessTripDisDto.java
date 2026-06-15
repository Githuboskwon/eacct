package com.iljin.apiServer.ijeas.sm.bizTrip;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class BusinessTripDisDto implements Serializable {
    private static final long serialVersionUID = 7521258649568452113L;

    // 회사코드
    String compCd;

    // 기준년월
    String standardYymm;

    // 출발도착지
    String departureArrivalArea;

    // 거리
    String distance;

    // 비고
    String remark;

    // 사용여부
    String useYn;

    // 수정자
    String chgId;

    // 수정자 이름
    String chgNm;

    // 수정일시
    LocalDateTime chgDtm;

    // 출장거리 조회
    @QueryProjection
    public BusinessTripDisDto(String compCd, String standardYymm, String departureArrivalArea, String distance, String remark, String useYn, String chgId, String chgNm, LocalDateTime chgDtm) {
        this.compCd = compCd;
        this.standardYymm = standardYymm;
        this.departureArrivalArea = departureArrivalArea;
        this.distance = distance;
        this.remark = remark;
        this.useYn = useYn;
        this.chgId = chgId;
        this.chgNm = chgNm;
        this.chgDtm = chgDtm;
    }

    String search;

    // 전표 입력 출장거리 조회
    @QueryProjection
    public BusinessTripDisDto(String compCd, String standardYymm, String departureArrivalArea, String distance, String remark, String useYn, String search) {
        this.compCd = compCd;
        this.standardYymm = standardYymm;
        this.departureArrivalArea = departureArrivalArea;
        this.distance = distance;
        this.remark = remark;
        this.useYn = useYn;
        this.search = search;
    }
}
