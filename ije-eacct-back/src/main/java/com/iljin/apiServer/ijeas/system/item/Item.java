package com.iljin.apiServer.ijeas.system.item;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Immutable
@Table(name = "TB_ITEM_GROUP")
@Entity
public class Item extends BaseEntity {

    @Column(name = "COMP_CD")
    String compCd;

    @Id
    @Column(name = "ITEM_GROUP_CD")
    String itemGroupCd;

    @Column(name = "ITEM_GROUP_NM")
    String itemGroupNm;

    @Column(name = "START_DATE_ACTIVE")
    LocalDateTime startDateActive;

    @Column(name = "END_DATE_ACTIVE")
    LocalDateTime endDateActive;

    @Column(name = "SEGMENT_NUM")
    String segmentNum;

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

}
