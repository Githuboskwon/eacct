package com.iljin.apiServer.ijeas.approval;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ApprovalHeaderDto implements Serializable {

    private static final long serialVersionUID = 8197722439686221411L;

    String loginId;

    // 회사구분코드
    String compCd;

    // 결재그룹번호
    BigDecimal apprGroupId;

    // 전표번호. 전표번호
    String slipNo;

    // 전표거래유형
    String slipType;

    // 전표거래유형명
    String slipTypeNm;

    // 문서제목
    String docTitleNm;

    // 내용
    String docContents;

    // 전표상태코드
    String slipStatus;

    // 전표상태명
    String slipStatusNm;

    // 다음결재자아이디
    String nextAppUserId;

    // 다음 결재자 순서번호
    String nextStage;

    //전기일자
    String glDate;

    // 참조사번
    String refUserId;

    // 회람사번
    String sendUserId;

    // 비고
    String remark;

    // 결재번호 (TB_APPR_DT)
    String apprNo;

    // 결재유형코드 (TB_APPR_DT)
    String apprTypeCd;

    // 결재유형명
    String apprTypeNm;

    // 결재단계
    String apprStage;

    // 결재자 사번
    String aprverId;

    // 결재자 이름
    String aprverNm;

    // 결재의견
    String apprDesc;


    // 기안자 사번
    String draftId;

    // 기안자
    String draftNm;

    // 기안자 부서코드
    String draftDeptCd;

    // 기안자 부서명
    String draftDeptNm;

    // 기안자 직급
    String draftUserJob;

    // 기안일자
    LocalDateTime draftDtm;



    String searchDtmFr;
    String searchDtmTo;
    String delegateChk;// 위임여부

    BigDecimal totAmt;// TB_SLIP_HD.TOT_AMT
    String headerRemark;// 전표 적요
    LocalDateTime slipApprDtm;// 전표 결재 기안일시
    BigDecimal reqTotAmt;// TB_BUD_HD.REQ_TOT_AMT
    String reqRsn;// 신청 사유
    String budApprDtm;// 예산신청 결재 기안일시
    List<ApprovalDetail> approvalDetails = new ArrayList<ApprovalDetail>();
    String approvalGroupId;
    BigDecimal slipHeaderId;
    BigDecimal cnt;


    String docType;
    String status;
    String docStatNm;

    String apprType;

    String deptCd;

    BigDecimal totalAmt;

    //작성자 부서
    String wrtDeptCd;

    String nextAppUserNm;
    String slipReusePossibleYn;
    List<ApprovalHeaderDto> approvalHeaderDtos;
    BigDecimal supplyAmount;
    BigDecimal taxAmount;
    String usedAmt;



    /*
     * approvalRepositoryCustom resultMapping on qlrm getApprTodoList() /
     * getApprDoneList() / getApprReqList
     */
    public ApprovalHeaderDto(String slipType, String slipStatus, String docTitleNm, String apprNo, String draftId,
            String draftNm, String draftUserJob, LocalDateTime draftDtm, BigDecimal totAmt, String headerRemark,
            LocalDateTime slipApprDtm, BigDecimal reqTotAmt, String reqRsn, String budApprDtm) {
        this.slipType = slipType;
        this.slipStatus = slipStatus;
        this.docTitleNm = docTitleNm;
        this.apprNo = apprNo;
        this.draftId = draftId;
        this.draftNm = draftNm;
        this.draftUserJob = draftUserJob;
        this.draftDtm = draftDtm;
        this.totAmt = totAmt;
        this.headerRemark = headerRemark;
        this.slipApprDtm = slipApprDtm;
        this.reqTotAmt = reqTotAmt;
        this.reqRsn = reqRsn;
        this.budApprDtm = budApprDtm;
    }

    // static: Jackson 직렬화/역직렬화 대상에서 제외 (Java 21 JPMS 가 java.time.format 내부 접근 차단 → InaccessibleObjectException 회피)
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @QueryProjection
    public ApprovalHeaderDto(BigDecimal apprGroupId, String slipNo, String slipType, String docTitleNm,
        String docContents, String slipStatus, String apprNo, BigDecimal totAmt, String headerRemark,
        LocalDateTime slipApprDtm) {
        this.apprGroupId = apprGroupId;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.docTitleNm = docTitleNm;
        this.docContents = docContents;
        this.slipStatus = slipStatus;
        this.apprNo = apprNo;
        this.totAmt = totAmt;
        this.headerRemark = headerRemark;
        this.slipApprDtm = slipApprDtm;
    }

    /**
     * 결재할 문서, 결재한 문서 리스트
     * */
    @QueryProjection
    public ApprovalHeaderDto(BigDecimal apprGroupId, String slipNo, String slipType, String slipTypeNm, String docTitleNm,
        String docContents, String slipStatus, String slipStatusNm, String apprNo, String aprverId, String aprverNm,
        String draftId, String draftNm, String draftDeptCd, String draftDeptNm,
        String draftUserJob, LocalDateTime draftDtm, BigDecimal totAmt, String headerRemark) {
        this.apprGroupId = apprGroupId;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipTypeNm = slipTypeNm;
        this.docTitleNm = docTitleNm;
        this.docContents = docContents;
        this.slipStatus = slipStatus;
        this.slipStatusNm = slipStatusNm;
        this.apprNo = apprNo;
        this.aprverId = aprverId;
        this.aprverNm = aprverNm;
        this.draftId = draftId;
        this.draftNm = draftNm;
        this.draftDeptCd = draftDeptCd;
        this.draftDeptNm = draftDeptNm;
        this.draftUserJob = draftUserJob;
        this.draftDtm = draftDtm;
        this.totAmt = totAmt;
        this.headerRemark = headerRemark;
    }

    /**
     * 결재할 문서 리스트 공급가, 부가세액, 총금액 추가한 DTO
     * */
    @QueryProjection
    public ApprovalHeaderDto(BigDecimal apprGroupId, String slipNo, String slipType, String slipTypeNm, String docTitleNm,
        String docContents, String slipStatus, String slipStatusNm, String apprNo, String aprverId, String aprverNm,
        String draftId, String draftNm, String draftDeptCd, String draftDeptNm,
        String draftUserJob, LocalDateTime draftDtm, BigDecimal totAmt, String headerRemark, BigDecimal supplyAmount, BigDecimal taxAmount, String usedAmt) {
        this.apprGroupId = apprGroupId;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipTypeNm = slipTypeNm;
        this.docTitleNm = docTitleNm;
        this.docContents = docContents;
        this.slipStatus = slipStatus;
        this.slipStatusNm = slipStatusNm;
        this.apprNo = apprNo;
        this.aprverId = aprverId;
        this.aprverNm = aprverNm;
        this.draftId = draftId;
        this.draftNm = draftNm;
        this.draftDeptCd = draftDeptCd;
        this.draftDeptNm = draftDeptNm;
        this.draftUserJob = draftUserJob;
        this.draftDtm = draftDtm;
        this.totAmt = totAmt;
        this.headerRemark = headerRemark;
        this.supplyAmount = supplyAmount;
        this.taxAmount = taxAmount;
        this.usedAmt = usedAmt;
    }

    @QueryProjection
    public ApprovalHeaderDto(BigDecimal apprGroupId, String slipNo, String slipType, String slipTypeNm, String docTitleNm,
                             String docContents, String slipStatus, String slipStatusNm,
                             String draftId, String draftNm, String draftDeptCd, String draftDeptNm,
                             String draftUserJob, LocalDateTime draftDtm, BigDecimal totAmt, String headerRemark) {
        this.apprGroupId = apprGroupId;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipTypeNm = slipTypeNm;
        this.docTitleNm = docTitleNm;
        this.docContents = docContents;
        this.slipStatus = slipStatus;
        this.slipStatusNm = slipStatusNm;
        this.draftId = draftId;
        this.draftNm = draftNm;
        this.draftDeptCd = draftDeptCd;
        this.draftDeptNm = draftDeptNm;
        this.draftUserJob = draftUserJob;
        this.draftDtm = draftDtm;
        this.totAmt = totAmt;
        this.headerRemark = headerRemark;
    }

    public ApprovalHeaderDto(BigDecimal apprGroupId, String slipNo, String slipType, String slipStatus,
        String docStatNm, String docTitleNm, String docContents, String apprType, String apprNo,
        BigDecimal totAmt, String headerRemark, LocalDateTime slipApprDtm) {
        this.apprGroupId = apprGroupId;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipStatus = slipStatus;
        this.docStatNm = docStatNm;
        this.docTitleNm = docTitleNm;
        this.docContents = docContents;
        this.apprType = apprType;
        this.apprNo = apprNo;
        this.totAmt = totAmt;
        this.headerRemark = headerRemark;
        this.slipApprDtm = slipApprDtm;
    }

    /*
     * approvalRepositoryCustom.getApprHeader() resultMapping on qlrm
     */

    public ApprovalHeaderDto(BigDecimal apprGroupId, String slipNo, String slipType, String draftId, String nextAppUserId,
        String nextStage, String slipTypeNm, String slipStatus, String status, String docTitleNm) {
        this.apprGroupId = apprGroupId;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.draftId = draftId;
        this.nextAppUserId = nextAppUserId;
        this.nextStage = nextStage;
        this.slipTypeNm = slipTypeNm;
        this.slipStatus = slipStatus;
        this.status = status;
        this.docTitleNm = docTitleNm;
    }

    public ApprovalHeaderDto(BigDecimal apprGroupId, String slipNo, String slipType, String draftId, String nextAppUserId,
                             String nextStage, String slipTypeNm, String slipStatus, String status, String docTitleNm, String refUserId) {
        this.apprGroupId = apprGroupId;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.draftId = draftId;
        this.nextAppUserId = nextAppUserId;
        this.nextStage = nextStage;
        this.slipTypeNm = slipTypeNm;
        this.slipStatus = slipStatus;
        this.status = status;
        this.docTitleNm = docTitleNm;
        this.refUserId = refUserId;
    }

    @QueryProjection
    public ApprovalHeaderDto(String nextStage, String nextAppUserId, String apprTypeCd) {
        this.nextStage = nextStage;
        this.nextAppUserId = nextAppUserId;
        this.apprTypeCd = apprTypeCd;
    }

    @QueryProjection
    public ApprovalHeaderDto(BigDecimal apprGroupId, String nextAppUserId, String apprTypeCd, String nextAppUserNm) {
        this.apprGroupId = apprGroupId;
        this.nextAppUserId = nextAppUserId;
        this.apprTypeCd = apprTypeCd;
        this.nextAppUserNm = nextAppUserNm;
    }

    @QueryProjection
    public ApprovalHeaderDto(BigDecimal cnt) {
        this.cnt = cnt;
    }

    @QueryProjection
    public ApprovalHeaderDto(String apprTypeCd, String apprStage, String aprverId, String status) {
        this.apprTypeCd = apprTypeCd;
        this.apprStage = apprStage;
        this.aprverId = aprverId;
        this.status = status;
    }
}
