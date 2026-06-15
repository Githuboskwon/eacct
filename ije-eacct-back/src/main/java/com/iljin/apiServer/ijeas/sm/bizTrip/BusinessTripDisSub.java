package com.iljin.apiServer.ijeas.sm.bizTrip;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Subselect(
    " SELECT "
    + " ROW_NUMBER() OVER (PARTITION BY B.COMP_CD, B.DEPARTURE_ARRIVAL_AREA ORDER BY B.STANDARD_YYMM DESC) AS ROW_NUM , "
    + " B.COMP_CD, B.STANDARD_YYMM, B.DEPARTURE_ARRIVAL_AREA, B.DISTANCE, B.REMARK, B.USE_YN, B.CHG_ID, B.CHG_DTM "
 + " FROM TB_BUSINESS_TRIP_DIS B"
)
@Immutable
@Synchronize("TB_BUSINESS_TRIP_DIS")
@IdClass(BusinessTripDisSubKey.class)
@Entity
public class BusinessTripDisSub {

    @Column(name = "ROW_NUM")
    private Long rowNum;

    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    @Id
    @Column(name = "STANDARD_YYMM", nullable = false)
    String standardYymm;

    @Id
    @Column(name = "DEPARTURE_ARRIVAL_AREA", nullable = false)
    String departureArrivalArea;

    @Column(name = "DISTANCE")
    String distance;

    @Column(name = "REMARK")
    String remark;

    @Column(name = "USE_YN")
    String useYn;

    @Column(name = "CHG_ID")
    String chgId;

    @Column(name = "CHG_DTM")
    LocalDateTime chgDtm;

}
