package com.iljin.apiServer.ijeas.approval;

import com.iljin.apiServer.core.audit.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NamedStoredProcedureQuery(
    name = "APPS.CBO_SP_SLIP_PKG.CONFIRM_CHECK",
    procedureName = "APPS.CBO_SP_SLIP_PKG.CONFIRM_CHECK",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, type = BigDecimal.class, name = "p_approval_group_id"),
        @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "p_final_flag"),
        @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_flag"),
        @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_step"),
        @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_msg")
    }
)
@NamedStoredProcedureQuery(
    name = "APPS.CBO_GL_BUDGET_PKG.CHECK_FUNDS_AVAILABLE",
    procedureName = "APPS.CBO_GL_BUDGET_PKG.CHECK_FUNDS_AVAILABLE",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, type = BigDecimal.class, name = "p_source_id"),
        @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "p_source_type"),
        @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_available_flag"),
        @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_summary_account"),
        @StoredProcedureParameter(mode = ParameterMode.OUT, type = BigDecimal.class, name = "x_funds_available"),
        @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_flag"),
        @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_step"),
        @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_msg")
    }
)
@NamedStoredProcedureQuery(
        name = "APPS.CBO_SP_SLIP_PKG.VALIDATION_MAIN",
        procedureName = "APPS.CBO_SP_SLIP_PKG.VALIDATION_MAIN",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = BigDecimal.class, name = "p_approval_group_id"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "p_validation_call_function"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_flag"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_step"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_msg")
        }
)
@NamedStoredProcedureQuery(
        name = "APPS.CBO_GL_BUDGET_PKG.AP_SLIP_ENCUMBRANCE",
        procedureName = "APPS.CBO_GL_BUDGET_PKG.AP_SLIP_ENCUMBRANCE",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = BigDecimal.class, name = "p_slip_header_id"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "p_reject_flag"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_flag"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_msg")
        }
)

@NamedStoredProcedureQuery(
        name = "APPS.CBO_GL_BUDGET_PKG.INSERT_ENCUMBRANCE",
        procedureName = "APPS.CBO_GL_BUDGET_PKG.INSERT_ENCUMBRANCE",
        parameters = {
            @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "p_org_id"),
            @StoredProcedureParameter(mode = ParameterMode.IN, type = BigDecimal.class, name = "p_ledger_id"),
            @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "p_source_module"),
            @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "p_source_table"),
            @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "p_currency_code"),
            @StoredProcedureParameter(mode = ParameterMode.IN, type = BigDecimal.class, name = "p_source_slip_id"),
            @StoredProcedureParameter(mode = ParameterMode.IN, type = BigDecimal.class, name = "p_source_line_num"),
            @StoredProcedureParameter(mode = ParameterMode.IN, type = LocalDateTime.class, name = "p_gl_date"),
            @StoredProcedureParameter(mode = ParameterMode.IN, type = BigDecimal.class, name = "p_ccid"),
            @StoredProcedureParameter(mode = ParameterMode.IN, type = BigDecimal.class, name = "p_amount"),
            @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "p_cancelled_flag"),
            @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_flag"),
            @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_msg"),
        }
)
@NamedStoredProcedureQuery(
    name = "APPS.CBO_SP_SLIP_PKG.SLIP_CANCEL_CONCURRENT",
    procedureName = "APPS.CBO_SP_SLIP_PKG.SLIP_CANCEL_CONCURRENT",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, type = BigDecimal.class, name = "p_approval_group_id"),
        @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "p_cancel_user_name"),
        @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "p_slip_process"),
        @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_flag"),
        @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "x_err_msg")
    }
)

@NoArgsConstructor
@Entity
@Data
@IdClass(ApprovalHeaderKey.class)
@Table(name = "TB_APPR_HD")
public class ApprovalHeader extends BaseEntity {

    //회사코드
    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    //결재그룹번호
    @Id
    @Column(name = "APPR_GROUP_ID", nullable = false)
    BigDecimal apprGroupId;

    //전표번호
    @Id
    @Column(name = "SLIP_NO", nullable = false)
    String slipNo;

    //전표거래유형
    @Column(name = "SLIP_TYPE")
    String slipType;

    //문서제목
    @Column(name = "DOC_TITLE_NM")
    String docTitleNm;

    //내용
    @Column(name = "DOC_CONTENTS")
    String docContents;

    //전표상태
    @Column(name = "SLIP_STATUS")
    String slipStatus;

    //기안자
    @Column(name = "DRAFT_ID")
    String draftId;

    //다음결재자아이디
    @Column(name = "NEXT_APP_USER_ID")
    String nextAppUserId;

    //다음 결재자 순서번호
    @Column(name = "NEXT_STAGE")
    String nextStage;

    //전기일자
    @Column(name = "GL_DATE")
    String glDate;

    //참조사번
    @Column(name = "REF_USER_ID")
    String refUserId;

    //회람사번
    @Column(name = "SEND_USER_ID")
    String sendUserId;

    //비고
    @Column(name = "REMARK")
    String remark;

//    @OneToMany(mappedBy = "approvalHeader", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    List<ApprovalDetail> approvalDetails = new ArrayList<ApprovalDetail>();

    @Builder
    public ApprovalHeader(String compCd, BigDecimal apprGroupId, String slipNo, String slipType,
        String docTitleNm, String docContents, String slipStatus, String draftId, String nextAppUserId,
        String nextStage, String glDate, String refUserId, String sendUserId, String remark) {
        this.compCd = compCd;
        this.apprGroupId = apprGroupId;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.docTitleNm = docTitleNm;
        this.docContents = docContents;
        this.slipStatus = slipStatus;
        this.draftId = draftId;
        this.nextAppUserId = nextAppUserId;
        this.nextStage = nextStage;
        this.glDate = glDate;
        this.refUserId = refUserId;
        this.sendUserId = sendUserId;
        this.remark = remark;
    }


    public void setSlipStatusSD(){
        this.slipStatus = "SD";
    }

    public void changeSlipStatus(String status){
        this.slipStatus = status;
    }

    public void updateForNextStage(String nextStage, String slipStatus, String nextAppUserId) {
        this.nextStage = nextStage;
        this.slipStatus = slipStatus;
        this.nextAppUserId = nextAppUserId;
    }
}
