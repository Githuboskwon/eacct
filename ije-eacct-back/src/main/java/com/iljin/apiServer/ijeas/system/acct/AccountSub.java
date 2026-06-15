package com.iljin.apiServer.ijeas.system.acct;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "tb_mst_acct_sub")
@IdClass(AccountSubKey.class)
@Data
public class AccountSub extends BaseEntity {
    @Id
    @Column(name = "COMP_CD")
    String compCd;

    @Id
    @Column(name = "TRX_TYPE_CD")
    String trxTypeCd;

    @Id
    @Column(name = "CHILD_TRX_TYPE_CD")
    String childTrxTypeCd;

    @Column(name = "CHILD_TRX_TYPE_NM")
    String childTrxTypeNm;

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
