package com.iljin.apiServer.ijeas.slip.etax;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table(name = "CBOSLIP.CBO_AR_ETAX_ISSUE_INFO")
@IdClass(CboArEtaxIssueInfoKey.class)
@Entity
public class CboArEtaxIssueInfo implements Serializable {
    @Id
    @Column(name = "ETAX_ISSUE_ID")
    BigDecimal etaxIssueId;

    @Column(name = "ITEM_NAME")
    String itemName;

    @Column(name = "REMARK")
    String remark;

    @Column(name = "ITEM_QTY")
    BigDecimal itemQty;

    @Column(name = "UNIT_PRICE")
    BigDecimal unitPrice;

    @Column(name = "CREATED_PERSON_ID")
    BigDecimal createdPersonId;

    @Column(name = "CREATION_DATE")
    LocalDateTime creationDate;

    @Column(name = "LAST_UPDATED_PERSON_ID")
    BigDecimal lastUpdatedPersonId;

    @Column(name = "LAST_UPDATE_DATE")
    LocalDateTime lastUpdateDate;

    public CboArEtaxIssueInfo(BigDecimal etaxIssueId, String itemName, String remark, BigDecimal itemQty, BigDecimal unitPrice,
                              BigDecimal createdPersonId, LocalDateTime creationDate, BigDecimal lastUpdatedPersonId, LocalDateTime lastUpdateDate) {
        this.etaxIssueId = etaxIssueId;
        this.itemName = itemName;
        this.remark = remark;
        this.itemQty = itemQty;
        this.unitPrice = unitPrice;
        this.createdPersonId = createdPersonId;
        this.creationDate = creationDate;
        this.lastUpdatedPersonId = lastUpdatedPersonId;
        this.lastUpdateDate = lastUpdateDate;
    }
}
