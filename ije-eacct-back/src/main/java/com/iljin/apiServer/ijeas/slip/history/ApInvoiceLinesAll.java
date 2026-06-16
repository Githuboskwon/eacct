package com.iljin.apiServer.ijeas.slip.history;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Table(name = "AP_INVOICE_LINES_ALL", schema = "APPS")
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
