package com.iljin.apiServer.ijeas.approval;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalDetailDto implements Serializable {

    private static final long serialVersionUID = 8471926433415928454L;

    String compCd;
    BigDecimal apprGroupId;
    String slipNo;
    String apprNo;
    String apprTypeCd;
    String apprType;
    String apprStage;
    String slipStatus;
    String slipStatusNm;
    String apprStatus;

    /* 결재자 */
    String aprverId;
    String aprverUser;
    String aprverNm;
    String aprverDeptNm;
    String aprverJobNm;
    String fixYn;

    /* 실제결재자 */
    String aAprverId;
    String aAprverUser;
    String aAprverNm;

    String aAprverDeptNm;

    String aAprverJobNm;


    /* 결재일자 */
    String apprDtm;

    /* 결재의견 */
    String apprDesc;
    String remark;
    String docMngNo;
    String nextStage;
    String refUserId;
    String slipTypeCd;
    String evidenceYn;


    public ApprovalDetailDto(String apprNo, String apprTypeCd, String apprType, String aprverId,
        String aprverUser, String aAprverId, String aAprverUser, String slipStatus,
        String apprStatus, String apprDtm, String apprDesc) {
        this.apprNo = apprNo;
        this.apprTypeCd = apprTypeCd;
        this.apprType = apprType;
        this.aprverId = aprverId;
        this.aprverUser = aprverUser;
        this.aAprverId = aAprverId;
        this.aAprverUser = aAprverUser;
        this.slipStatus = slipStatus;
        this.apprStatus = apprStatus;
        this.apprDtm = apprDtm;
        this.apprDesc = apprDesc;
    }

    /**
     * getApprDetail constructor
     * */
    public ApprovalDetailDto(String apprNo, String apprStage, String apprTypeCd, String apprType,
        String aprverId, String aprverNm, String aAprverId, String aAprverNm, String slipStatus,
        String slipStatusNm, String apprDtm, String apprDesc) {
        this.apprNo = apprNo;
        this.apprStage = apprStage;
        this.apprTypeCd = apprTypeCd;
        this.apprType = apprType;
        this.aprverId = aprverId;
        this.aprverNm = aprverNm;
        this.aAprverId = aAprverId;
        this.aAprverNm = aAprverNm;
        this.slipStatus = slipStatus;
        this.slipStatusNm = slipStatusNm;
        this.apprDtm = apprDtm;
        this.apprDesc = apprDesc;
    }

}
