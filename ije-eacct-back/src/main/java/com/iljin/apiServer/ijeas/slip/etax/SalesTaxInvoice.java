package com.iljin.apiServer.ijeas.slip.etax;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@NamedStoredProcedureQuery(
        name = "APPS.CBO_SP_COMMON_PKG.GET_AR_ACCOUNTING_STATUS",
        procedureName = "APPS.CBO_SP_COMMON_PKG.GET_AR_ACCOUNTING_STATUS",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "p_customer_trx_id"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "complete_yn")
        }
)

@NamedStoredProcedureQuery(
        name = "APPS.CBO_SP_SLIP_PKG.CREATE_ACCOUNT_OM",
        procedureName = "APPS.CBO_SP_SLIP_PKG.CREATE_ACCOUNT_OM",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "p_customer_trx_id"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_flag")
        }
)

@NamedStoredProcedureQuery(
        name = "APPS.CBO_SP_COMMON_PKG.TRX_AMT_MODIFY",
        procedureName = "APPS.CBO_SP_COMMON_PKG.TRX_AMT_MODIFY",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = BigDecimal.class, name = "p_customer_trx_id"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_flag"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_step"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_msg")
        }
)


@NoArgsConstructor
@Table(name ="CBO_AR_TAX_CUST_TRX_ALL_V")
@IdClass(SalesTaxInvoiceKey.class)
@Entity
public class SalesTaxInvoice {
    @Id
    @Column(name = "CUSTOMER_TRX_ID", nullable = false)
    BigDecimal customerTrxId;

    @Column(name = "TRX_NUMBER")
    String trxNumber;

    @Column(name = "GROUP_NUMBER")
    String groupNumber;

    @Column(name = "TRX_DATE")
    LocalDateTime trxDate;

    @Column(name = "GL_DATE")
    LocalDateTime glDate;

    @Column(name = "ORG_ID")
    BigDecimal orgId;

    @Column(name = "CUSTOMER_ID")
    BigDecimal customerId;

    @Column(name = "CREATE_DEPT_CODE")
    String createDeptCode;

    @Column(name = "CREATE_EMP_NO")
    String createEmpNo;

    @Column(name = "CUSTOMER_NAME")
    String customerName;

    @Column(name = "INTEGRATION_VENDOR_NUM")
    String integrationVendorNum;

    @Column(name = "TAX_REFERENCE")
    String taxReference;

    @Column(name = "REPRESENTATIVE_NAME")
    String representativeName;

    @Column(name = "CUST_BUSINESS_CONDITION")
    String custBusinessCondition;

    @Column(name = "CUST_BUSINESS_TYPE")
    String custBusinessType;

    @Column(name = "CORPORATION_NUMBER")
    String corporationNumber;

    @Column(name = "CUSTOMER_ADDRESS")
    String customerAddress;

    @Column(name = "POSTAL_CODE")
    String postalCode;

    @Column(name = "PHONE_NUMBER")
    String phoneNumber;

    @Column(name = "FAX_NUMBER")
    String faxNumber;

    @Column(name = "E_MAIL")
    String eMail;

    @Column(name = "COMMENTS")
    String comments;

    @Column(name = "CURRENCY_CODE")
    String currencyCode;

    @Column(name = "TAX_EVIDENCE_TYPE")
    String taxEvidenceType;

    @Column(name = "TAX_EVIDENCE_TYPE_NAME")
    String taxEvidenceTypeName;

    @Column(name = "SUPBUY_TYPE")
    String supbuyType;

    @Column(name = "ETAX_ISSUE_ID")
    String etaxIssueId;

    @Column(name = "DTI_STATUS")
    String dtiStatus;

    @Column(name = "DTI_STATUS_MEANING")
    String dtiStatusMeaning;

    @Column(name = "TAX_CODE")
    String taxCode;

    @Column(name = "DTI_TYPE")
    String dtiType;

    @Column(name = "TOTAL_AMOUNT")
    BigDecimal totalAmount;

    @Column(name = "SUPPLY_AMOUNT")
    BigDecimal supplyAmount;

    @Column(name = "TAX_AMOUNT")
    BigDecimal taxAmount;

    @Column(name = "TRX_TYPE")
    String trxType;

    @Column(name = "COMPLETE_FLAG")
    String completeFlag;

    @Column(name = "SUPPLY_AMOUNT_MODIFY")
    BigDecimal supplyAmountModify;

    @Column(name = "TAX_AMOUNT_MODIFY")
    BigDecimal taxAmountModify;

    @Column(name = "TAX_LOCATION")
    String taxLocation;

    @Column(name = "PROJECT_NUMBER")
    String projectNumber;

    @Column(name = "ONACCT_FLAG")
    String onacctFlag;

    @Column(name = "ONACCT_APPLY_COUNT")
    BigDecimal onacctApplyCount;

    @Column(name = "DIRECTION")
    String direction;

}
