package com.iljin.apiServer.ijeas.slip.tax;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@IdClass(TaxDtiNtsMainKey.class)
@Entity
@Table(name = "XXSB_DTI_NTS_MAIN")
public class TaxDtiNtsMain {

    @Id
    @Column(name = "ISSUE_ID")
    String issueId;

    //@Id
    @Column(name = "SUPBUY_TYPE")
    String supbuyType;

    @Column(name = "DTI_TYPE")
    String dtiType;

    @Column(name = "AMEND_YN")
    String amendYn;

    @Column(name = "INTERFACE_BATCH_ID")
    Integer interfaceBatchId;

    @Column(name = "FILE_IDX")
    Integer fileIdx;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "DTI_WDATE")
    LocalDateTime dtiWdate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "DTI_IDATE")
    LocalDateTime dtiIdate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "DTI_SDATE")
    LocalDateTime dtiSdate;

    @Column(name = "ISSUE_SYSTEM")
    String issueSystem;

    @Column(name = "TYPE_CODE")
    String typeCode;

    @Column(name = "TAX_DEMAND")
    String taxDemand;

    @Column(name = "SEQ_ID")
    String seqId;

    @Column(name = "EXCHANGED_DOC_ID")
    String exchangedDocId;

    @Column(name = "SUP_COM_REGNO")
    String supComRegno;

    @Column(name = "SUP_BIZPLACE_CODE")
    String supBizplaceCode;

    @Column(name = "SUP_COM_NAME")
    String supComName;

    @Column(name = "SUP_EMP_NAME")
    String supEmpName;

    @Column(name = "SUP_COM_TYPE")
    String supComType;

    @Column(name = "SUP_COM_CLASSIFY")
    String supComClassify;

    @Column(name = "SUP_COM_ADDR")
    String supComAddr;

    @Column(name = "SUP_DEPT_NAME")
    String supDeptName;

    @Column(name = "SUP_PERSON_NAME")
    String supPersonName;

    @Column(name = "SUP_TEL_NUM")
    String supTelNum;

    @Column(name = "SUP_EMAIL")
    String supEmail;

    @Column(name = "BYR_COM_REGNO")
    String byrComRegno;

    @Column(name = "BYR_BIZPLACE_CODE")
    String byrBizplaceCode;

    @Column(name = "BYR_COM_NAME")
    String byrComName;

    @Column(name = "BYR_EMP_NAME")
    String byrEmpName;

    @Column(name = "BYR_COM_TYPE")
    String byrComType;

    @Column(name = "BYR_COM_CLASSIFY")
    String byrComClassify;

    @Column(name = "BYR_COM_ADDR")
    String byrComAddr;

    @Column(name = "BYR_DEPT_NAME")
    String byrDeptName;

    @Column(name = "BYR_PERSON_NAME")
    String byrPersonName;

    @Column(name = "BYR_TEL_NUM")
    String byrTelNum;

    @Column(name = "BYR_EMAIL")
    String byrEmail;

    @Column(name = "BYR_DEPT_NAME2")
    String byrDeptName2;

    @Column(name = "BYR_PERSON_NAME2")
    String byrPersonName2;

    @Column(name = "BYR_TEL_NUM2")
    String byrTelNum2;

    @Column(name = "BYR_EMAIL2")
    String byrEmail2;

    @Column(name = "BYR_BUSINESS_TYPE_CODE")
    String byrBusinessTypeCode;

    @Column(name = "BROKER_COM_REGNO")
    String brokerComRegno;

    @Column(name = "BROKER_BIZPLACE_CODE")
    String brokerBizplaceCode;

    @Column(name = "BROKER_COM_NAME")
    String brokerComName;

    @Column(name = "BROKER_EMP_NAME")
    String brokerEmpName;

    @Column(name = "BROKER_COM_TYPE")
    String brokerComType;

    @Column(name = "BROKER_COM_CLASSIFY")
    String brokerComClassify;

    @Column(name = "BROKER_COM_ADDR")
    String brokerComAddr;

    @Column(name = "BROKER_DEPT_NAME")
    String brokerDeptName;

    @Column(name = "BROKER_PERSON_NAME")
    String brokerPersonName;

    @Column(name = "BROKER_TEL_NUM")
    String brokerTelNum;

    @Column(name = "BROKER_EMAIL")
    String brokerEmail;

    @Column(name = "CASH_AMOUNT")
    BigDecimal cashAmount;

    @Column(name = "CHECK_AMOUNT")
    BigDecimal checkAmount;

    @Column(name = "NOTE_AMOUNT")
    BigDecimal noteAmount;

    @Column(name = "RECEIVABLE_AMOUNT")
    BigDecimal receivableAmount;

    @Column(name = "SUP_AMOUNT")
    BigDecimal supAmount;

    @Column(name = "TAX_AMOUNT")
    BigDecimal taxAmount;

    @Column(name = "TOTAL_AMOUNT")
    BigDecimal totalAmount;

    @Column(name = "REMARK")
    String remark;

    @Column(name = "REMARK2")
    String remark2;

    @Column(name = "REMARK3")
    String remark3;

    @Column(name = "DTI_MSG")
    String dtiMsg;

    @Column(name = "AMEND_CODE")
    String amendCode;

    @Column(name = "CREATED_BY")
    String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "CREATION_DATE")
    LocalDateTime creationDate;

    @Column(name = "LAST_UPDATED_BY")
    String lastUpdatedBy;

    @Column(name = "ORI_ISSUE_ID")
    String oriIssueId;

}
