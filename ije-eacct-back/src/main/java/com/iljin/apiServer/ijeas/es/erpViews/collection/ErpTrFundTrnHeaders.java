package com.iljin.apiServer.ijeas.es.erpViews.collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(ErpTrFundTrnHeadersKey.class)
@Table(name = "CBO_SP_TR_FUND_TRN_HEADER_V")
@Entity
public class ErpTrFundTrnHeaders {

    @Id
    @Column(name = "XTR_SLIP_TYPE")
    String xtrSlipType;

    @Id
    @Column(name = "ORG_ID" , nullable = false)
    Integer orgId;

    @Id
    @Column(name = "GL_DATE")
    LocalDateTime glDate;

    @Column(name = "CREATED_BY")
    String createdBy;

    @Column(name = "CURRENCY_CODE")
    String currencyCode;

    @Column(name = "REMARK")
    String remark;

    @Column(name = "FUNCTIONAL_AMOUNT_DR")
    BigDecimal functionalAmountDr;

    @Column(name = "FUNCTIONAL_AMOUNT_CR")
    BigDecimal functionalAmountCr;

}
