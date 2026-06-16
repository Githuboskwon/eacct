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
@Table(name = "CBO_AP_VIEW_PREPAYS_V", schema = "APPS")
@Entity
public class CboApViewPrepaysV implements Serializable{
    private static final long serialVersionUID = -4198746556514089330L;

    @Id
    @Column(name = "ROW_ID")
    String rowId;

    @Column(name = "INVOICE_ID")
    BigDecimal invoiceId;
}
