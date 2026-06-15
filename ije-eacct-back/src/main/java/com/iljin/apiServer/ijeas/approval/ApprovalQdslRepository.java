package com.iljin.apiServer.ijeas.approval;


import java.math.BigDecimal;
import java.util.List;

public interface ApprovalQdslRepository {

    List<ApprovalHeaderDto> getApprTodoList(ApprovalHeaderDto approvalHeaderDto);

    List<ApprovalHeaderDto> getApprDoneList(ApprovalHeaderDto approvalHeaderDto);

    List<ApprovalHeaderDto> getApprReqList(ApprovalHeaderDto approvalHeaderDto);

    List<ApprovalHeaderDto> getNextStage(ApprovalDetailDto approvalDetailDto);

    void updateCardUseList(String compCd, BigDecimal approvalGroupId);

    ApprovalHeaderDto getNextAppUser(ApprovalHeaderDto approvalHeaderDto);

    Boolean existsByFirstStage(ApprovalHeaderDto approvalHeaderDto);

    List<ApprovalHeaderDto> getRefList(ApprovalHeaderDto approvalHeaderDto);

    List<ApprovalHeaderDto> findByCompCdAndSlipHeaderId(String compCd, BigDecimal apprGroupId);

    Integer getMaxStage(String compCd, BigDecimal apprGroupId);


}
