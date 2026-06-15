package com.iljin.apiServer.ijeas.system.vendor;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Table(name = "CBO_GL_TERMS_V")
@Entity
public class CboGlTermsV  implements Serializable {

    @Id
    @Column(name = "TRX_TYPE")
    String trxType;

    @Id
    @Column(name = "TERM_ID")
    Integer termId;

    @Column(name = "NAME")
    String name;

    @Column(name = "DESCRIPTION")
    String description;

    @Id
    @Column(name = "ORG_ID")
    String orgId;

    @Column(name = "NOTES_FLAG")
    String notesFlag;

    @Column(name = "MATURITY_DAYS")
    String maturityDays;

    @Column(name = "DUE_DATE_CALC_FLAG")
    String dueDateCalcFlag;

    @Column(name = "CURRENCY_TYPE")
    String currencyType;

    @Column(name = "PAYMENT_METHOD")
    String paymentMethod;

    @Column(name = "VENDOR_ACCT_CHECK")
    String vendorAcctCheck;

    @Column(name = "SP_ENABLED_FLAG")
    String spEnabledFlag;

    @Column(name = "DEFAULT_TERM_FLAG")
    String defaultTermFlag;
}
