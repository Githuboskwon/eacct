package com.iljin.apiServer.ijeas.system.acct;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "tb_mst_acct")
@IdClass(AccountKey.class)
@Data
public class Account extends BaseEntity {

    @Id
    @Column(name = "COMP_CD")
    String compCd;

    @Id
    @Column(name = "TRX_TYPE_CD")
    String trxTypeCd;

    @Id
    @Column(name = "ACCT_CD")
    String acctCd;

    @Column(name = "ACCT_NM")
    String acctNm;

    @Column(name = "ACCT_TYPE")
    String acctType;

    @Column(name = "ENABLED_FLAG")
    String enabledFlag;

    @Id
    @Column(name = "DR_CR")
    String drCr;

    @Column(name = "DRCR_TYPE")
    String drcrType;

    @Column(name = "INTERFACE_SLIP_TYPE")
    String interfaceSlipType;

    @Column(name = "INTERFACE_SLIP_TYPE_NM")
    String interfaceSlipTypeNm;

    @Column(name = "ACCT_MODULE")
    String acctModule;

    @Column(name = "INTERFACE_MODULE")
    String interfaceModule;

    @Column(name = "CHECK_FLAG")
    String checkFlag;

    @Column(name = "ACCT_ATTRIBUTE1")
    String acctAttribute1;

    @Column(name = "ACCT_ATTRIBUTE2")
    String acctAttribute2;

    @Column(name = "ASSETS_TRACKING_FLAG")
    String assetsTrackingFlag;

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
