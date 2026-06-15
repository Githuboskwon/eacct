package com.iljin.apiServer.ijeas.system.trx;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@IdClass(TrxEmpKey.class)
@Table(name = "TB_MST_TRX_EMP")
@Entity
public class TrxEmp extends BaseEntity {
    @Id
    @Column(name = "COMP_CD")
    String compCd;

    @Id
    @Column(name = "TRX_TYPE_CD")
    String trxTypeCd;

    @Id
    @Column(name = "PERSON_ID")
    Integer personId;

    @Column(name = "REGISTRATION_METHOD")
    String registrationMethod;

    @Column(name = "ENABLE_FLAG")
    String enableFlag;

    @Column(name = "LEDGER_ID")
    Integer ledgerId;

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
