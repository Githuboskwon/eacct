package com.iljin.apiServer.ijeas.slip.surtax;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(SurTaxCodeKey.class)
@Table(name = "CBO_SP_TAX_CODE_V")
@Entity
public class SurTaxCode {

    @Id
    @Column(name = "TAX_RATE_CODE")
    String taxRateCode;

    @Column(name = "TAX_STATUS_CODE")
    String taxStatusCode;

    @Id
    @Column(name = "TAX_RATE_ID")
    Integer taxRateId;

    @Column(name = "TAX_ACCT_CODE")
    String taxAcctCode;

    @Column(name = "TAX_ACCOUNT_CCID")
    BigDecimal taxAccountCcid;

    @Column(name = "PERCENTAGE_RATE")
    Integer percentageRate;

    @Id
    @Column(name = "LEDGER_ID")
    Integer ledgerId;

    @Id
    @Column(name = "TAX_EVIDENCE_TYPE")
    String taxEvidenceType;

    @Column(name = "DTI_TYPE")
    String dtiType;

}
