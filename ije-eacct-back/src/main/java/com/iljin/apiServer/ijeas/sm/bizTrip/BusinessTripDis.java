package com.iljin.apiServer.ijeas.sm.bizTrip;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@IdClass(BusinessTripDisKey.class)
@Table(name = "TB_BUSINESS_TRIP_DIS")
@Entity
public class BusinessTripDis extends BaseEntity {

    // 회사코드
    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    // 기준년월
    @Id
    @Column(name = "STANDARD_YYMM", nullable = false)
    String standardYymm;

    // 출발-도착지
    @Id
    @Column(name = "DEPARTURE_ARRIVAL_AREA", nullable = false)
    String departureArrivalArea;

    // 거리
    @Column(name = "DISTANCE")
    String distance;

    // 비고
    @Column(name = "REMARK")
    String remark;

    // 사용여부
    @Column(name = "USE_YN")
    String useYn;


    @Builder
    public BusinessTripDis(String compCd, String standardYymm, String departureArrivalArea, String distance, String remark, String useYn) {
        this.compCd = compCd;
        this.standardYymm = standardYymm;
        this.departureArrivalArea = departureArrivalArea;
        this.distance = distance;
        this.remark = remark;
        this.useYn = useYn;
    }

    public BusinessTripDis update(BusinessTripDisDto dto) {
        this.distance = dto.getDistance();
        this.remark = dto.getRemark();
        this.useYn = dto.getUseYn();
        return this;
    }
}
