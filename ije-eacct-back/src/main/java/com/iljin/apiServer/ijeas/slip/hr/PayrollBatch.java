package com.iljin.apiServer.ijeas.slip.hr;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@NamedStoredProcedureQuery(
        name = "SP_DELETE_PAYROLL",
        procedureName = "SP_DELETE_PAYROLL",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "P_ORG_ID"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "P_PAYROLL_BATCH_ID"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "P_UPLOAD_EMP_NO"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "ERR_CODE"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "ERR_MSG")

        }
)

@NamedStoredProcedureQuery(
        name = "APPS.CBO_SP_PAYROLL_PKG.PAYROLL_VALIDATION",
        procedureName = "APPS.CBO_SP_PAYROLL_PKG.PAYROLL_VALIDATION",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "P_PAYROLL_BATCH_ID"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_flag"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_step"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_msg")
        }
)


@NamedStoredProcedureQuery(
        name = "APPS.CBO_SP_PAYROLL_PKG.PAYROLL_SLIP_CREATE",
        procedureName = "APPS.CBO_SP_PAYROLL_PKG.PAYROLL_SLIP_CREATE",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "P_PAYROLL_BATCH_ID"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_flag"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_step"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_msg")
        }
)


@Getter
@NoArgsConstructor
@Table(name ="CBO_SP_PAYROLL_BATCH")
@IdClass(PayrollBatchKey.class)
@Entity
public class PayrollBatch {
    @Id
    @Column(name = "ORG_ID", nullable = false)
    private Integer orgId;

    @Id
    @Column(name = "LEDGER_ID", nullable = false)
    private Integer ledgerId;

    @Id
    @Column(name = "PAYROLL_BATCH_ID", nullable = false)
    private Integer  payrollBatchId;

    @Column(name = "BATCH_PERIOD")
    private String batchPeriod;

    @Column(name = "PAYROLL_BATCH_NAME")
    private String payrollBatchName;

    @Column(name = "UPLOAD_EMP_NO")
    private String uploadEmpNo;

    @Column(name = "ATTRIBUTE1")
    private String attribute1;

    @Column(name = "ATTRIBUTE2")
    private String attribute2;

    @Column(name = "ATTRIBUTE3")
    private String attribute3;

    @Column(name = "ATTRIBUTE4")
    private String attribute4;

    @Column(name = "ATTRIBUTE5")
    private String attribute5;

    @Column(name = "CREATED_EMP_NO")
    private String createdEmpNo;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "LAST_UPDATED_EMP_NO")
    private String lastUpdatedEmpNo;

    @Column(name = "LAST_UPDATED_DATE")
    private LocalDateTime lastUpdatedDate;

    @Builder
    public PayrollBatch(Integer orgId, Integer ledgerId, Integer payrollBatchId, String batchPeriod, String payrollBatchName, String uploadEmpNo, String attribute1,
                        String attribute2, String attribute3, String attribute4, String attribute5, String createdEmpNo, LocalDateTime creationDate, String lastUpdatedEmpNo,
                        LocalDateTime lastUpdatedDate) {
        this.orgId = orgId;
        this.ledgerId = ledgerId;
        this.payrollBatchId = payrollBatchId;
        this.batchPeriod = batchPeriod;
        this.payrollBatchName = payrollBatchName;
        this.uploadEmpNo = uploadEmpNo;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
        this.createdEmpNo = createdEmpNo;
        this.creationDate = creationDate;
        this.lastUpdatedEmpNo = lastUpdatedEmpNo;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    @Builder
    public PayrollBatch(Integer orgId, Integer ledgerId, Integer payrollBatchId, String batchPeriod, String payrollBatchName, String uploadEmpNo, String createdEmpNo,
                        LocalDateTime creationDate, String lastUpdatedEmpNo, LocalDateTime lastUpdatedDate) {
        this.orgId = orgId;
        this.ledgerId = ledgerId;
        this.payrollBatchId = payrollBatchId;
        this.batchPeriod = batchPeriod;
        this.payrollBatchName = payrollBatchName;
        this.uploadEmpNo = uploadEmpNo;
        this.createdEmpNo = createdEmpNo;
        this.creationDate = creationDate;
        this.lastUpdatedEmpNo = lastUpdatedEmpNo;
        this.lastUpdatedDate = lastUpdatedDate;
    }

}
