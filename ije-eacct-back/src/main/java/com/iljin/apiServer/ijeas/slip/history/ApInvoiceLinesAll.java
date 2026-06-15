package com.iljin.apiServer.ijeas.slip.history;

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
@Table(name = "APPS.AP_INVOICE_LINES_ALL")
@Entity
public class ApInvoiceLinesAll implements Serializable{
    @Id
    @Column(name = "INVOICE_ID")
    BigDecimal invoiceId;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "LINE_NUMBER")
    Integer lineNumber;
}
