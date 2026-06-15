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
@Table(name = "APPS.AP_INVOICES_ALL")
@Entity
public class ApInvoicesAll implements Serializable{
    @Id
    @Column(name = "INVOICE_ID")
    BigDecimal invoiceId;

    @Column(name = "PAYMENT_STATUS_FLAG")
    String paymentStatusFlag;

    @Column(name = "ORG_ID")
    Integer orgId;

    @Column(name = "GLOBAL_ATTRIBUTE19")
    BigDecimal globalAttribute19;

    @Column(name = "INVOICE_NUM")
    String invoiceNum;

}
