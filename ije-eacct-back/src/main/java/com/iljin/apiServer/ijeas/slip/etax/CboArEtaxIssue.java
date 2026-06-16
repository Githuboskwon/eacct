package com.iljin.apiServer.ijeas.slip.etax;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table(name = "CBO_AR_ETAX_ISSUE")
@IdClass(CboArEtaxIssueKey.class)
@Entity
public class CboArEtaxIssue implements Serializable {
    @Id
    @Column(name = "ETAX_ISSUE_ID")
    BigDecimal etaxIssueId;

    @Column(name = "CONVERSATION_ID")
    String conversationId;

    @Column(name = "SUPBUY_TYPE")
    String supbuyType;

    @Column(name = "DIRECTION")
    String direction;

    @Column(name = "INTERFACE_BATCH_ID")
    BigDecimal interfaceBatchId;

    @Column(name = "DTI_WDATE")
    LocalDateTime dtiWdate;

    @Column(name = "TAX_DEMAND")
    String taxDemand;

    @Column(name = "ITEM_NAME")
    String itemName;

    @Column(name = "REMARK")
    String remark;

    @Column(name = "BYR_TEL_NUM")
    String byrTelNum;

    @Column(name = "BYR_EMAIL")
    String byrEmail;

    @Column(name = "ISSUE_EMP_NAME")
    String issueEmpName;

    @Column(name = "ISSUE_EMP_NO")
    String issueEmpNo;

    @Column(name = "ISSUE_EMP_EMAIL")
    String issueEmpEmail;

    @Column(name = "ISSUE_EMP_PHONE")
    String issueEmpPhone;

    @Column(name = "AMEND_CODE")
    String amendCode;

    @Column(name = "ORI_DTI_WDATE")
    LocalDateTime oriDtiWdate;

    @Column(name = "LOCAL_LC_OPEN_DATE")
    LocalDateTime localLcOpenDate;

    @Column(name = "ORI_ISSUE_ID")
    String oriIssueId;

    @Column(name = "ORI_ETAX_ISSUE_ID")
    BigDecimal oriEtaxIssueId;

    @Column(name = "CREATED_PERSON_ID")
    BigDecimal createdPersonId;

    @Column(name = "CREATION_DATE")
    LocalDateTime creationDate;

    @Column(name = "LAST_UPDATED_PERSON_ID")
    BigDecimal lastUpdatedPersonId;

    @Column(name = "LAST_UPDATE_DATE")
    LocalDateTime lastUpdateDate;

    public CboArEtaxIssue(BigDecimal etaxIssueId, String conversationId, String supbuyType, String direction, BigDecimal interfaceBatchId, LocalDateTime dtiWdate,
                          String taxDemand, String itemName, String remark, String byrTelNum, String byrEmail, String issueEmpName, String issueEmpNo,
                          String issueEmpEmail, String issueEmpPhone, BigDecimal createdPersonId, LocalDateTime creationDate, BigDecimal lastUpdatedPersonId,
                          LocalDateTime lastUpdateDate, String amendCode, String oriIssueId, BigDecimal oriEtaxIssueId, LocalDateTime oriDtiWdate,
                          LocalDateTime localLcOpenDate
                          ) {
        this.etaxIssueId = etaxIssueId;
        this.conversationId = conversationId;
        this.supbuyType = supbuyType;
        this.direction = direction;
        this.interfaceBatchId = interfaceBatchId;
        this.dtiWdate = dtiWdate;
        this.taxDemand = taxDemand;
        this.itemName = itemName;
        this.remark = remark;
        this.byrTelNum = byrTelNum;
        this.byrEmail = byrEmail;
        this.issueEmpName = issueEmpName;
        this.issueEmpNo = issueEmpNo;
        this.issueEmpEmail = issueEmpEmail;
        this.issueEmpPhone = issueEmpPhone;
        this.amendCode = amendCode;
        this.oriDtiWdate = oriDtiWdate;
        this.localLcOpenDate = localLcOpenDate;
        this.oriIssueId = oriIssueId;
        this.oriEtaxIssueId = oriEtaxIssueId;
        this.createdPersonId = createdPersonId;
        this.creationDate = creationDate;
        this.lastUpdatedPersonId = lastUpdatedPersonId;
        this.lastUpdateDate = lastUpdateDate;
    }
}
