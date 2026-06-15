package com.iljin.apiServer.ijeas.es.erpViews.gl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(ErpGlLinesKey.class)
@Table(name = "CBO_EA_GL_LINES_V")
@Entity
public class ErpGlLines {

    @Id
    @Column(name = "JE_HEADER_ID", nullable = false)
    BigDecimal jeHeaderId;

    @Id
    @Column(name = "JE_LINE_NUM", nullable = false)
    Integer jeLineNum;

    @Column(name = "STATUS", nullable = false)
    String status;

    @Column(name = "SOURCE_NAME", nullable = false)
    String sourceName;

    @Column(name = "CATEGORY_NAME", nullable = false)
    String categoryName;

    @Column(name = "JE_BATCH_ID", nullable = false)
    BigDecimal jeBatchId;

    @Column(name = "BATCH_NAME", nullable = false)
    String batchName;

    @Column(name = "HEADER_NAME", nullable = false)
    String headerName;

    @Column(name = "CURRENCY_CODE", nullable = false)
    String currencyCode;

    @Column(name = "CURRENCY_RATE")
    BigDecimal currencyRate;

    @Column(name = "GL_DATE", nullable = false)
    LocalDateTime glDate;

    @Column(name = "CCID", nullable = false)
    BigDecimal ccid;

    @Column(name = "ACCOUNT_CODE")
    String accountCode;

    @Column(name = "ACCOUNT_NAME")
    String accountName;

    @Column(name = "ENTERED_DR")
    BigDecimal enteredDr;

    @Column(name = "ENTERED_CR")
    BigDecimal enteredCr;

    @Column(name = "ACCOUNTED_DR")
    BigDecimal accountedDr;

    @Column(name = "ACCOUNTED_CR")
    BigDecimal accountedCr;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "USER_NAME", nullable = false)
    String userName;

    @Column(name = "FULL_NAME")
    String fullName;

    @Column(name = "COA_CODE")
    String coaCode;

    @Column(name = "COA_DESC")
    String coaDesc;


}
