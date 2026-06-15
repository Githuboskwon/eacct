package com.iljin.apiServer.ijeas.approval;

import com.iljin.apiServer.ijeas.approval.dlgt.ApprovalDelegateDto;
import com.iljin.apiServer.ijeas.approval.rule.ApprovalRule;
import com.iljin.apiServer.ijeas.approval.rule.ApprovalRuleDto;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface ApprovalService {

    /**
    * 결재할 문서
    * */
    List<ApprovalHeaderDto> getApprTodoList(ApprovalHeaderDto approvalHeaderDto);

    /**
    * 결재한 문서
    * */
    List<ApprovalHeaderDto> getApprDoneList(ApprovalHeaderDto approvalHeaderDto);

    /**
    * 상신한 문서
    * */
    List<ApprovalHeaderDto> getApprReqList(ApprovalHeaderDto approvalHeaderDto);

    List<ApprovalHeaderDto> getRefList(ApprovalHeaderDto approvalHeaderDto);

    /**
    * 결재 상세
    * */
    Map<String, Object> getApproval(String docNo);

    /**
    * 상신 취소
    * */
    @Modifying
    @Transactional
    String cancelApproval(ApprovalHeaderDto approvalHeaderDto);

    /**
     * 일괄 상신 취소
     * */
    @Modifying
    @Transactional
    String cancelApprovalBundle(List<ApprovalHeaderDto> approvalHeaderDtoList);

    /**
    * 결재 처리
    * */
    @Transactional
    ResponseEntity<String> doApproval(ApprovalDetailDto approvalDetailDto);

    /**
     * 결재할 문서 일괄결재
     * */
    @Transactional
    String doApprovalBundle(List<ApprovalDetailDto> approvalDetailDtos);

    /**
     * 결재 반려
     * */
    @Modifying
    @Transactional
    ResponseEntity<String> rejectApproval(ApprovalHeaderDto approvalHeaderDto);

    /**
     * 결재 일괄반려
     * */
    @Modifying
    @Transactional
    ResponseEntity<String> rejectApprovalBundle(ApprovalHeaderDto approvalHeaderDtos);

    /**
    * 결재 상신
    * */
    @Transactional
    ResponseEntity<String> requestApproval(ApprovalHeaderDto approvalHeaderDto);

    /**
     * 결재 단건상신
     * */
    @Transactional
    ResponseEntity<String> requestApprovalSingle(ApprovalHeaderDto approvalHeaderDto);
    /**
     * 결재 일괄상신
     * */
    @Transactional
    String requestApprovalBundle(ApprovalGroup approvalGroup) ;

    /**
     * 전표관리 검인
     * */
    @Modifying
    @Transactional
    ResponseEntity<String> verifySlip(ApprovalDetailDto approvalDetailDto);

    /**
     * 전표관리 일괄검인
     * */
    @Modifying
    @Transactional
    ResponseEntity<String> verifySlipBundle(List<ApprovalDetailDto> approvalDetailDtos);
    
    /**
     * 결재선 지정
     * */
    List<ApprovalEmployeeDto> getApprovalEmpList();

    /**
     * 결재위임관리 - 리스트 조회
     * */
    List<ApprovalDelegateDto> getApprovalDlgtList(ApprovalDelegateDto approvalDelegateDto);

    /**
     * 결재위임관리 - 저장(신규/갱신)
     * */
    @Modifying
    @Transactional
    ResponseEntity<String> saveApprovalDlgt(ApprovalDelegateDto approvalDelegateDto);

    /**
     * 결재위임관리 - 지정해제
     * */
    @Modifying
    @Transactional
    ResponseEntity<String> cancelApprovalDlgt(ApprovalDelegateDto approvalDelegateDto);

    /**
     * 전결규정관리 - 조회
     * */
    List<ApprovalRuleDto> getApprRuleList(ApprovalRuleDto approvalRuleDto);

    /**
     * 전결규정관리 - 행삭제
     * */
    ResponseEntity<String> deleteApprovalRule(List<ApprovalRuleDto> approvalRuleDto);

    /**
     * 전결규정관리 - 신규/수정
     * */
    ResponseEntity<String> saveApprovalRules(List<ApprovalRule> approvalRules);

    List<ApprovalHeaderDto> getApprIngList(ApprovalHeaderDto approvalHeaderDto);

    List<ApprovalHeaderDto> getApprAprList(ApprovalHeaderDto approvalHeaderDto);

    List<ApprovalHeaderDto> getApprRejList(ApprovalHeaderDto approvalHeaderDto);

    List<ApprovalEmployeeDto> getApprovalDeptTreeList();

    List<ApprovalRuleDto> getApprRuleLines(String docTypeCd, String dtlTypeCd, String curCd, String maxAmt);

    ResponseEntity<String> getDelegatingCheck(String adlgId, String actId);
}
