package com.iljin.apiServer.ijeas.es.erpViews;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(ErpGlCoaSegmentKey.class)
@Table(name = "CBO_GL_COA_SEGMENT_V")
@Entity
public class ErpGlCoaSegment {

    @Id
    @Column(name = "VALUE_CODE")
    String valueCode;

    @Column(name = "VALUE_NAME")
    String valueName;

    @Id
    @Column(name = "COA_ID")
    BigDecimal coaId;

    @Id
    @Column(name = "SEGMENT_NUM")
    BigDecimal segmentNum;

    @Column(name = "SEGMENT_NAME")
    String segmentName;

    @Column(name = "APPLICATION_COLUMN_NAME")
    String applicationColumnName;

    @Column(name = "ORG_ID")
    Integer orgId;

    @Column(name = "LEDGER_ID")
    BigDecimal ledgerId;

    @Column(name = "DEFAULT_VALUE")
    String defaultValue;

    @Column(name = "START_DATE_ACTIVE")
    LocalDateTime startDateActive;

    @Column(name = "END_DATE_ACTIVE")
    LocalDateTime endDateActive;

    @Column(name = "ENABLED_FLAG")
    String enabledFlag;

    @Column(name = "ATTRIBUTE1")
    String attribute1;

    @Column(name = "ATTRIBUTE2")
    String attribute2;

    @Column(name = "ATTRIBUTE3")
    String attribute3;

    @Column(name = "ATTRIBUTE4")
    String attribute4;

    @Column(name = "ATTRIBUTE5")
    String attribute5;

    @Column(name = "ATTRIBUTE6")
    String attribute6;

    @Column(name = "ATTRIBUTE7")
    String attribute7;

    @Column(name = "ATTRIBUTE8")
    String attribute8;

    @Column(name = "ATTRIBUTE9")
    String attribute9;

    @Column(name = "ATTRIBUTE10")
    String attribute10;

    @Column(name = "ATTRIBUTE11")
    String attribute11;

    @Column(name = "ATTRIBUTE12")
    String attribute12;

    @Column(name = "ATTRIBUTE13")
    String attribute13;

    @Column(name = "ATTRIBUTE14")
    String attribute14;

    @Column(name = "ATTRIBUTE15")
    String attribute15;

    @Column(name = "FLEX_VALUE_SET_ID")
    BigDecimal flexValueSetId;

}
