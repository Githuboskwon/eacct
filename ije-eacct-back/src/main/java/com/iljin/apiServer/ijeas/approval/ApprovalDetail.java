package com.iljin.apiServer.ijeas.approval;

import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.system.emp.Employee;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Data
@Table(name = "TB_APPR_DT")
@IdClass(ApprovalDetailKey.class)
public class ApprovalDetail extends BaseEntity {


    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    @Id
    @Column(name = "APPR_GROUP_ID", nullable = false)
    BigDecimal apprGroupId;

    @Id
    @Column(name = "SLIP_NO", nullable = false)
    String slipNo;

    @Id
    @Column(name = "APPR_NO", nullable = false)
    String apprNo;

    @Column(name = "APPR_TYPE_CD")
    String apprTypeCd;

    @Column(name = "APPR_STAGE")
    String apprStage;

    @Column(name = "SLIP_STATUS")
    String slipStatus;

    @Column(name = "APRVER_ID")
    String aprverId;

    @Column(name = "APRVER_NM")
    String aprverNm;

    @Column(name = "APRVER_DEPT_NM")
    String aprverDeptNm;

    @Column(name = "APRVER_JOB_NM")
    String aprverJobNm;

    @Column(name = "FIX_YN")
    String fixYn;

    @Column(name = "A_APRVER_ID")
    String aAprverId;

    @Column(name = "A_APRVER_NM")
    String aAprverNm;

    @Column(name = "A_APRVER_DEPT_NM")
    String aAprverDeptNm;

    @Column(name = "A_APRVER_JOB_NM")
    String aAprverJobNm;

    @Column(name = "APPR_DTM")
    LocalDateTime apprDtm;

    @Column(name = "APPR_DESC")
    String apprDesc;

    @Column(name = "REMARK")
    String remark;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "COMP_CD", insertable = false, updatable = false)
//    @JoinColumn(name = "APPR_GROUP_ID", insertable = false, updatable = false)
//    @JoinColumn(name = "SLIP_NO", insertable = false, updatable = false)
//    @JsonIgnore
//    ApprovalHeader approvalHeader;

    @Builder
    public ApprovalDetail(String compCd, BigDecimal apprGroupId, String slipNo, String apprNo,
        String apprTypeCd, String apprStage, String slipStatus, String aprverId, String aprverNm,
        String aprverDeptNm, String aprverJobNm, String fixYn, String aAprverId, String aAprverNm,
        String aAprverDeptNm, String aAprverJobNm, LocalDateTime apprDtm, String apprDesc,
        String remark) {
        this.compCd = compCd;
        this.apprGroupId = apprGroupId;
        this.slipNo = slipNo;
        this.apprNo = apprNo;
        this.apprTypeCd = apprTypeCd;
        this.apprStage = apprStage;
        this.slipStatus = slipStatus;
        this.aprverId = aprverId;
        this.aprverNm = aprverNm;
        this.aprverDeptNm = aprverDeptNm;
        this.aprverJobNm = aprverJobNm;
        this.fixYn = fixYn;
        this.aAprverId = aAprverId;
        this.aAprverNm = aAprverNm;
        this.aAprverDeptNm = aAprverDeptNm;
        this.aAprverJobNm = aAprverJobNm;
        this.apprDtm = apprDtm;
        this.apprDesc = apprDesc;
        this.remark = remark;
    }

    @Builder
    public ApprovalDetail(String compCd, BigDecimal apprGroupId, String slipNo,
                          String apprNo, String apprTypeCd, String apprStage,
                          String slipStatus, String aprverId, String aprverNm,
                          String aprverDeptNm, String aprverJobNm) {
        this.compCd = compCd;
        this.apprGroupId = apprGroupId;
        this.slipNo = slipNo;
        this.apprNo = apprNo;
        this.apprTypeCd = apprTypeCd;
        this.apprStage = apprStage;
        this.slipStatus = slipStatus;
        this.aprverId = aprverId;
        this.aprverNm = aprverNm;
        this.aprverDeptNm = aprverDeptNm;
        this.aprverJobNm = aprverJobNm;
    }

    @Builder
    public ApprovalDetail(String compCd, BigDecimal apprGroupId, Short apprSeq, String apprNo, String apprTypeCd, String apprFgCd,
                          String slipStatus, String aprverNm, String aprverDeptNm, String aprverJobNm) {
        this.compCd = compCd;
        this.apprGroupId = apprGroupId;
        this.apprNo = apprNo;
        this.slipStatus = slipStatus;
        this.aprverNm = aprverNm;
        this.aprverDeptNm = aprverDeptNm;
        this.aprverJobNm = aprverJobNm;
    }

    @Builder
    public ApprovalDetail(String aAprverId, String aAprverNm, String aAprverDeptNm, String aAprverJobNm, LocalDateTime apprDtm) {
        this.aAprverId = aAprverId;
        this.aAprverNm = aAprverNm;
        this.aAprverDeptNm = aAprverDeptNm;
        this.aAprverJobNm = aAprverJobNm;
        this.apprDtm = apprDtm;
    }

    public void changeStatus(String status){
        this.slipStatus = status;
    }

    public void changeApprDesc(String apprDesc) {
        this.apprDesc = apprDesc;
    }

    public void changeAAprver(Employee aAprver) {
        this.aAprverId = aAprver.getEmpNo();
        this.aAprverNm = aAprver.getEmpNm();
        this.aAprverJobNm = aAprver.getJobNm();
        this.aAprverDeptNm = aAprver.getDeptNm();
        this.apprDtm = LocalDateTime.now();
    }
}
