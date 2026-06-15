package com.iljin.apiServer.ijeas.system.payBank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@IdClass(PayBankKey.class)
@NoArgsConstructor
@Table(name = "CBO_SP_PAY_BANK_ACCOUNT_NAME_V")
public class PayBank {

    @Id
    @Column(name = "OPERATING_UNIT_ID", nullable = false)
    private Long operatingUnitId;

    @Id
    @Column(name = "BANK_ACCOUNT_ID", nullable = false)
    private Long bankAccountId;

    @Column(name = "BANK_ACCOUNT_NAME", nullable = false)
    private String bankAccountName;

    @Column(name = "BANK_ACCOUNT_NUM")
    private String bankAccountNum;

    @Column(name = "BANK_NAME", nullable = false)
    private String bankName;

    @Column(name = "BANK_BRANCH_NAME", nullable = false)
    private String bankBranchName;

    @Column(name = "CURRENCY_CODE")
    private String currencyCode;

    @Column(name = "ATTRIBUTE1")
    private String attribute1;

    @Column(name = "ATTRIBUTE2")
    private String attribute2;

    @Column(name = "ATTRIBUTE3")
    private String attribute3;

    @Column(name = "ATTRIBUTE4")
    private String attribute4;

    @Column(name = "ATTRIBUTE5")
    private String attribute5;






}
