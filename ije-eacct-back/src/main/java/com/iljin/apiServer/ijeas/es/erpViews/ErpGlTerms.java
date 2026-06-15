package com.iljin.apiServer.ijeas.es.erpViews;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(ErpGlTermsKey.class)
@Table(name = "CBO_GL_TERMS_V")
@Entity
public class ErpGlTerms {

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
