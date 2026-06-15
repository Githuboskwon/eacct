package com.iljin.apiServer.ijeas.es.erpViews;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(ErpGlTrxTypeEvidenceKey.class)
@Table(name = "CBO_GL_TRX_TYPE_EVIDENCE_V")
@Entity
public class ErpGlTrxTypeEvidence {

    @Id
    @Column(name = "TRX_TYPE_CODE")
    String trxTypeCode;

    @Id
    @Column(name = "EVIDENCE_CODE")
    String evidenceCode;

    @Column(name = "EVIDENCE_NAME")
    String evidenceName;

    @Id
    @Column(name = "ORG_ID")
    String orgId;

    @Column(name = "LINE_ATTRIBUTE1")
    String lineAttribute1;

    @Column(name = "LINE_ATTRIBUTE2")
    String lineAttribute2;

    @Column(name = "LINE_ATTRIBUTE3")
    String lineAttribute3;

    @Column(name = "LINE_ATTRIBUTE4")
    String lineAttribute4;

    @Column(name = "LINE_ATTRIBUTE5")
    String lineAttribute5;

    @Column(name = "LINE_ATTRIBUTE6")
    String lineAttribute6;

    @Column(name = "ENABLED_FLAG")
    String enabledFlag;

    @Column(name = "CREATED_BY")
    Integer createdBy;

    @Column(name = "CREATION_DATE")
    LocalDateTime creationDate;

    @Column(name = "LAST_UPDATED_BY")
    Integer lastUpdatedBy;

    @Column(name = "LAST_UPDATE_DATE")
    LocalDateTime lastUpdateDate;

    @Column(name = "LAST_UPDATE_LOGIN")
    BigDecimal lastUpdateLogin;

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
