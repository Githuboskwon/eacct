package com.iljin.apiServer.ijeas.slip.etax;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Table(name ="CBOTAX.XXSB_DTI_INVOICE")
@IdClass(XxsbDtiInvoiceKey.class)
@Entity
public class XxsbDtiInvoice {
    @Id
    @Column(name = "CONVERSATION_ID")
    String conversationId;

    @Column(name = "SUPBUY_TYPE")
    String supbuyType;

    @Column(name = "DIRECTION")
    String direction;

    @Column(name = "INVOICE_IDX")
    String invoiceIdx;

    @Column(name = "INVOICE_ID")
    BigDecimal invoiceId;

    @Column(name = "INVOICE_NUM")
    String invoiceNum;

    @Column(name = "APPROVE_ID")
    String approveId;

    @Column(name = "SLIP_NO")
    String slipNo;
}
