package com.iljin.apiServer.ijeas.system.trx;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Table(name = "CBO_SP_AWT_GROUP_V")
@Entity
public class CboSpAwtGroupV implements Serializable {
    private static final long serialVersionUID = -5462212804862163361L;

    @Id
    @Column(name = "AWT_GROUP_ID")
    Integer awtGroupId;

    @Column(name = "AWT_GROUP_NAME")
    String awtGroupName;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "ORG_ID")
    Integer orgId;

    @Column(name = "LOCATION_CODE")
    String locationCode;
}
