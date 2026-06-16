package com.iljin.apiServer.ijeas.costBudget;


import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.approval.ApprovalDetailRepository;
import com.iljin.apiServer.ijeas.approval.ApprovalHeaderRepository;
import com.iljin.apiServer.ijeas.slip.SlipHeader;
import com.iljin.apiServer.ijeas.slip.SlipHeaderKey;
import com.iljin.apiServer.ijeas.slip.SlipHeaderRepository;
import com.iljin.apiServer.ijeas.system.acct.AccountDto;
import com.iljin.apiServer.ijeas.system.item.ItemDto;
import com.iljin.apiServer.ijeas.system.pjt.ProjectDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service
public class CostBudgetServiceImpl implements CostBudgetService {

    @PersistenceContext
    private EntityManager entityManager;

    private final CostBudgetRepositoryCustom costBudgetRepositoryCustom;

    private final SlipHeaderRepository slipHeaderRepository;

    private final BudgetHdRepository budgetHdRepository;

    private final BudgetDtRepository budgetDtRepository;

    private final ApprovalHeaderRepository approvalHeaderRepository;

    private final ApprovalDetailRepository approvalDetailRepository;

    private final CostBudgetQdslRepository costBudgetQdslRepository;

    private final Util util;

    public List<CostBudgetDto> getBudgetAmountListCnt(CostBudgetDto costBudgetDto) throws Exception {
        return costBudgetRepositoryCustom.getBudgetAmountListCnt(costBudgetDto);
    }

    public CostBudgetResult getBudgetAmountList(CostBudgetDto costBudgetDto) throws Exception {
        return costBudgetRepositoryCustom.getBudgetAmountList(costBudgetDto);
    }

    public List<CostBudgetDto> getPlanTotal(CostBudgetDto costBudgetDto) throws Exception {
        return costBudgetRepositoryCustom.getPlanTotal(costBudgetDto);
    }

    public List<CostBudgetDto> getSumDetail(CostBudgetDto costBudgetDto) throws Exception {
        return costBudgetRepositoryCustom.getSumDetail(costBudgetDto);
    }

    public List<CostBudgetDto> getPerformanceCheck(CostBudgetDto costBudgetDto) throws Exception {

        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();
        BigDecimal ledger_id = BigDecimal.valueOf(Long.parseLong(ledgerId));

        StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_SP_BD_VS_ACT_PKG.GET_SP_CCTR_CD_SPLIT_P");
        storedProcedureQuery.setParameter("P_LEDGER_ID", ledger_id);
        storedProcedureQuery.setParameter("P_PERIOD_YEAR", costBudgetDto.getPeriodYear());
        storedProcedureQuery.setParameter("P_PERIOD_MONTH", costBudgetDto.getPeriodMonth());
        storedProcedureQuery.setParameter("P_CCTR_CD", costBudgetDto.getSearchDeptCd());
        storedProcedureQuery.setParameter("P_GET_TYPE", "PERIOD");
        storedProcedureQuery.execute();


        return costBudgetRepositoryCustom.getPerformanceCheck(costBudgetDto);
    }

    public AccountDetailResult getPerformanceCheckDetail(CostBudgetDto costBudgetDto) throws Exception {
        return costBudgetRepositoryCustom.getPerformanceCheckDetail(costBudgetDto);
    }

    public String doApprovalSave(CostBudgetDto costBudgetDto) throws Exception {
        return costBudgetRepositoryCustom.doApprovalSave(costBudgetDto);
    }

    public String doApprovalClear(CostBudgetDto costBudgetDto) throws Exception {
        return costBudgetRepositoryCustom.doApprovalClear(costBudgetDto);
    }

    public String doSave(CostBudgetSaveList costBudgetSaveList) throws Exception {
        return costBudgetRepositoryCustom.doSave(costBudgetSaveList);
    }

    public String doDelete(CostBudgetSaveList costBudgetSaveList) throws Exception {
        return costBudgetRepositoryCustom.doDelete(costBudgetSaveList);
    }

    public DraftListResult getDraftList(CostBudgetDto costBudgetDto) throws Exception {

        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();
        BigDecimal ledger_id = BigDecimal.valueOf(Long.parseLong(ledgerId));

        StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_SP_BD_VS_ACT_PKG.GET_SP_CCTR_CD_SPLIT_P");
        storedProcedureQuery.setParameter("P_LEDGER_ID", ledger_id);
        storedProcedureQuery.setParameter("P_PERIOD_YEAR", costBudgetDto.getPeriodYear());
        storedProcedureQuery.setParameter("P_PERIOD_MONTH", costBudgetDto.getPeriodMonth());
        storedProcedureQuery.setParameter("P_CCTR_CD", costBudgetDto.getSearchDeptCd());
        storedProcedureQuery.setParameter("P_GET_TYPE", "PERIOD");
        storedProcedureQuery.execute();


        return costBudgetRepositoryCustom.getDraftList(costBudgetDto);
    }

    public CostBudgetDto doDraftSave(DraftResult draftResult) throws Exception {

        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();
        BigDecimal ledger_id = BigDecimal.valueOf(Long.parseLong(ledgerId));

        CostBudgetDto data = draftResult.getData();

        StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_SP_BD_VS_ACT_PKG.GET_SP_CCTR_CD_SPLIT_P");
        storedProcedureQuery.setParameter("P_LEDGER_ID", ledger_id);
        storedProcedureQuery.setParameter("P_PERIOD_YEAR", data.getPeriodYear());
        storedProcedureQuery.setParameter("P_PERIOD_MONTH", data.getPeriodMonth());
        storedProcedureQuery.setParameter("P_CCTR_CD", data.getCctrCd());
        storedProcedureQuery.setParameter("P_GET_TYPE", "PERIOD");
        storedProcedureQuery.execute();

        return costBudgetRepositoryCustom.doDraftSave(draftResult);
    }

    @Override
    public String doDraftDelete(CostBudgetDto costBudgetDto) throws Exception {

        String compCd = util.getLoginCompCd();;
        String msg = "삭제 하였습니다.";

        try {

            // 전표 헤더 삭제
            SlipHeaderKey slipHeaderKey = new SlipHeaderKey(compCd,costBudgetDto.getSlipNo());
            slipHeaderRepository.deleteById(slipHeaderKey);

            // 비용예산기안 헤더 삭제
            budgetHdRepository.deleteBySlipNoAndSlipHeaderId(costBudgetDto.getSlipNo(),costBudgetDto.getSlipHeaderId());

            // 비용예산기안 라인 삭제
            budgetDtRepository.deleteBySlipNoAndSlipHeaderId(costBudgetDto.getSlipNo(),costBudgetDto.getSlipHeaderId());

            // 결재 헤더 삭제
            approvalHeaderRepository.deleteByCompCdAndApprGroupId(compCd,costBudgetDto.getSlipHeaderId());

            // 결재 라인 삭제
            approvalDetailRepository.deleteByCompCdAndApprGroupId(compCd,costBudgetDto.getSlipHeaderId());

        }catch (Exception e){
            msg = "삭제 실패하였습니다.";
        }

        return msg;
    }

    @Override
    public List<ItemDto> getBudgetItemList(CostBudgetDto costBudgetDto){
        return costBudgetQdslRepository.getBudgetItemList(costBudgetDto);
    }

    @Override
    public List<ProjectDto> getBudgetProjectList(CostBudgetDto costBudgetDto) {
        return costBudgetQdslRepository.getBudgetProjectList(costBudgetDto);
    }

    @Override
    public List<AccountDto> getAccountPopList(CostBudgetDto costBudgetDto) {
        return costBudgetRepositoryCustom.getAccountPopList(costBudgetDto);
    }

}
