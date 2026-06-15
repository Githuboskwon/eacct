package com.iljin.apiServer.ijeas.slip.etax;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table(name ="CBOTAX.XXSB_DTI_MAIN")
@IdClass(XxsbDtiMainKey.class)
@Entity
public class XxsbDtiMain {
    @Id
    @Column(name = "CONVERSATION_ID")
    String conversationId;

    @Id
    @Column(name = "SUPBUY_TYPE")
    String supbuyType;

    @Id
    @Column(name = "DIRECTION")
    String direction;

    @Column(name = "INTERFACE_BATCH_ID")
    BigDecimal interfaceBatchId;

    @Column(name = "DTI_WDATE")
    LocalDateTime dtiWdate;

    @Column(name = "BLANK_CNT")
    Integer blankCnt;

    @Column(name = "DTI_TYPE")
    String dtiType;

    @Column(name = "TAX_DEMAND")
    String taxDemand;

    @Column(name = "SUP_COM_ID")
    String supComId;

    @Column(name = "SUP_COM_REGNO")
    String supComRegno;

    @Column(name = "SUP_REP_NAME")
    String supRepName;

    @Column(name = "SUP_COM_NAME")
    String supComName;

    @Column(name = "SUP_COM_TYPE")
    String supComType;

    @Column(name = "SUP_COM_CLASSIFY")
    String supComClassify;

    @Column(name = "SUP_COM_ADDR")
    String supComAddr;

    @Column(name = "SUP_DEPT_NAME")
    String supDeptName;

    @Column(name = "SUP_EMP_NAME")
    String supEmpName;

    @Column(name = "SUP_TEL_NUM")
    String supTelNum;

    @Column(name = "SUP_EMAIL")
    String supEmail;

    @Column(name = "BYR_COM_ID")
    String byrComId;

    @Column(name = "BYR_COM_REGNO")
    String byrComRegno;

    @Column(name = "BYR_REP_NAME")
    String byrRepName;

    @Column(name = "BYR_COM_NAME")
    String byrComName;

    @Column(name = "BYR_COM_TYPE")
    String byrComType;

    @Column(name = "BYR_COM_CLASSIFY")
    String byrComClassify;

    @Column(name = "BYR_COM_ADDR")
    String byrComAddr;

    @Column(name = "BYR_DEPT_NAME")
    String byrDeptName;

    @Column(name = "BYR_EMP_NAME")
    String byrEmpName;

    @Column(name = "BYR_TEL_NUM")
    String byrTelNum;

    @Column(name = "BYR_EMAIL")
    String byrEmail;

    @Column(name = "SUP_AMOUNT")
    BigDecimal supAmount;

    @Column(name = "TAX_AMOUNT")
    BigDecimal taxAmount;

    @Column(name = "TOTAL_AMOUNT")
    BigDecimal totalAmount;

    @Column(name = "DTT_YN")
    String dttYn;

    @Column(name = "REMARK")
    String remark;

    @Column(name = "CREATED_BY")
    String createdBy;

    @Column(name = "CREATION_DATE")
    LocalDateTime creationDate;

    @Column(name = "AMEND_CODE")
    String amendCode;

    @Column(name = "BYR_BIZPLACE_CODE")
    String byrBizplaceCode;

    @Column(name = "ORI_ISSUE_ID")
    String oriIssueId;
}
