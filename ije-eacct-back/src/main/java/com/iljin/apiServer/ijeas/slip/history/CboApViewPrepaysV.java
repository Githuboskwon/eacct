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
@Table(name = "APPS.CBO_AP_VIEW_PREPAYS_V")
@Entity
public class CboApViewPrepaysV implements Serializable{
    private static final long serialVersionUID = -4198746556514089330L;

    @Id
    @Column(name = "ROW_ID")
    String rowId;

    @Column(name = "INVOICE_ID")
    BigDecimal invoiceId;
}
