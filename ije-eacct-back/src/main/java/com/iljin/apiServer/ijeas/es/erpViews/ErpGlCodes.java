package com.iljin.apiServer.ijeas.es.erpViews;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(ErpGlCodesKey.class)
@Table(name = "CBO_GL_CODE_V")
@Entity
public class ErpGlCodes {

    @Id
    @Column(name = "CODE_TYPE_ID")
    Integer codeTypeId;

    @Id
    @Column(name = "CODE_TYPE")
    String codeType;

    @Id
    @Column(name = "HEADER_MEANING")
    String headerMeaning;

    @Column(name = "HEADER_DESCRIPTION")
    String headerDescription;

    @Column(name = "HEADER_ENABLED_FLAG")
    String headerEnabledFlag;

    @Column(name = "HEADER_ORDER_BY")
    Integer headerOrderBy;

    @Id
    @Column(name = "CODE")
    String code;

    @Id
    @Column(name = "LINE_MEANING")
    String lineMeaning;

    @Column(name = "LINE_DESCRIPTION")
    String lineDescription;

    @Column(name = "LINE_ENABLED_FLAG")
    String lineEnabledFlag;

    @Column(name = "LINE_ORDER_BY")
    Integer lineOrderBy;

    @Column(name = "HEADER_ATTRIBUTE1")
    String headerAttribute1;

    @Column(name = "HEADER_ATTRIBUTE2")
    String headerAttribute2;

    @Column(name = "HEADER_ATTRIBUTE3")
    String headerAttribute3;

    @Column(name = "HEADER_ATTRIBUTE4")
    String headerAttribute4;

    @Column(name = "HEADER_ATTRIBUTE5")
    String headerAttribute5;

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

    @Column(name = "LINE_ATTRIBUTE7")
    String lineAttribute7;

    @Column(name = "LINE_ATTRIBUTE8")
    String lineAttribute8;

    @Column(name = "LINE_ATTRIBUTE9")
    String lineAttribute9;

    @Column(name = "LINE_ATTRIBUTE10")
    String lineAttribute10;


}
