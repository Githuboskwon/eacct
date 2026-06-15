package com.iljin.apiServer.ijeas.system.item;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table(name = "CBO_AP_EMP_SEGMENT5")
@Entity
public class CboApEmpSegment5 implements Serializable {
    private static final long serialVersionUID = 1987264644734109371L;
    @Id
    @Column(name = "PERSON_ID")
    Integer personId;

    @Id
    @Column(name = "SEGMENT5")
    String segment5;

    @Column(name = "ENABLED_FLAG")
    String enabledFlag;

    @Column(name = "ORG_ID")
    Integer orgId;

    @Column(name = "LEDGER_ID")
    Integer ledgerId;

    @Column(name = "CREATED_BY")
    Integer createdBy;

    @Column(name = "CREATION_DATE")
    LocalDateTime creationDate;

    @Column(name = "LAST_UPDATED_BY")
    Integer lastUpdatedBy;

    @Column(name = "LAST_UPDATE_DATE")
    LocalDateTime lastUpdateDate;

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

}
