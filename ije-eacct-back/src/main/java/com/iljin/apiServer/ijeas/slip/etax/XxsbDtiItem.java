package com.iljin.apiServer.ijeas.slip.etax;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table(name ="CBOTAX.XXSB_DTI_ITEM")
@IdClass(XxsbDtiItemKey.class)
@Entity
public class XxsbDtiItem {
    @Id
    @Column(name = "CONVERSATION_ID")
    String conversationId;

    @Id
    @Column(name = "SUPBUY_TYPE")
    String supbuyType;

    @Id
    @Column(name = "DIRECTION")
    String direction;

    @Id
    @Column(name = "DTI_LINE_NUM")
    BigDecimal dtiLineNum;

    @Column(name = "ITEM_NAME")
    String itemName;

    @Column(name = "ITEM_MD")
    LocalDateTime itemMd;

    @Column(name = "UNIT_PRICE")
    BigDecimal unitPrice;

    @Column(name = "ITEM_QTY")
    BigDecimal itemQty;

    @Column(name = "SUP_AMOUNT")
    BigDecimal supAmount;

    @Column(name = "TAX_AMOUNT")
    BigDecimal taxAmount;

    @Column(name = "REMARK")
    String remark;

    @Column(name = "CREATED_BY")
    String createdBy;

    @Column(name = "CREATION_DATE")
    LocalDateTime creationDate;
}
