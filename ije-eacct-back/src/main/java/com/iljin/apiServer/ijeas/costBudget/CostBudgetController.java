package com.iljin.apiServer.ijeas.costBudget;

import com.iljin.apiServer.core.util.Error;
import com.iljin.apiServer.ijeas.approval.ApprovalException;
import com.iljin.apiServer.ijeas.system.acct.AccountDto;
import com.iljin.apiServer.ijeas.system.item.ItemDto;
import com.iljin.apiServer.ijeas.system.pjt.ProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/cost/budget")
@RequiredArgsConstructor
public class CostBudgetController {
    private final CostBudgetService costBudgetService;

    @ExceptionHandler(CostBudgetException.class)
    public ResponseEntity<Error> budgetNotFound(ApprovalException e) {
        Error error = new Error(2001, e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/list/cnt")
    public ResponseEntity<List<CostBudgetDto>> getBudgetAmountListCnt(@RequestBody CostBudgetDto costBudgetDto) throws Exception {
        return new ResponseEntity<>(costBudgetService.getBudgetAmountListCnt(costBudgetDto), HttpStatus.OK);
    }

    /*비용 예산 관리 리스트 조회*/
    @PostMapping("/list")
    public ResponseEntity<CostBudgetResult> getBudgetAmountList(@RequestBody CostBudgetDto costBudgetDto) throws Exception {
        return new ResponseEntity<>(costBudgetService.getBudgetAmountList(costBudgetDto), HttpStatus.OK);
    }

    /*비용 예산 관리 연간사업계획*/
    @PostMapping("/pop/yearplan")
    public ResponseEntity<List<CostBudgetDto>> getPlanTotal(@RequestBody CostBudgetDto costBudgetDto) throws Exception {
        return new ResponseEntity<>(costBudgetService.getPlanTotal(costBudgetDto), HttpStatus.OK);
    }

    /*비용 예산 관리 요약*/
    @PostMapping("/pop/acctTypeSum")
    public ResponseEntity<List<CostBudgetDto>> getSumDetail(@RequestBody CostBudgetDto costBudgetDto) throws Exception {
        return new ResponseEntity<>(costBudgetService.getSumDetail(costBudgetDto), HttpStatus.OK);
    }

    /*비용 예산 관리 실적조회*/
    @PostMapping("/pop/performanceCheck")
    public ResponseEntity<List<CostBudgetDto>> getPerformanceCheck(@RequestBody CostBudgetDto costBudgetDto) throws Exception {
        return new ResponseEntity<>(costBudgetService.getPerformanceCheck(costBudgetDto), HttpStatus.OK);
    }

    /*비용 예산 관리 실적조회 상세*/
    @PostMapping("/pop/performanceCheck/detail")
    public ResponseEntity<AccountDetailResult> getPerformanceCheckDetail(@RequestBody CostBudgetDto costBudgetDto) throws Exception {
        return new ResponseEntity<>(costBudgetService.getPerformanceCheckDetail(costBudgetDto), HttpStatus.OK);
    }

    /*비용 예산 예산 확정*/
    @PostMapping("/approval/save")
    public ResponseEntity<String> doApprovalSave(@RequestBody CostBudgetDto costBudgetDto) throws Exception {
        return new ResponseEntity<>(costBudgetService.doApprovalSave(costBudgetDto), HttpStatus.OK);
    }

    /*비용 예산 예산 확정 해제*/
    @PostMapping("/approval/clear")
    public ResponseEntity<String> doApprovalClear(@RequestBody CostBudgetDto costBudgetDto) throws Exception {
        return new ResponseEntity<>(costBudgetService.doApprovalClear(costBudgetDto), HttpStatus.OK);
    }

    /*비용 예산 저장*/
    @PostMapping("/save")
    public ResponseEntity<String> doSave(@RequestBody CostBudgetSaveList costBudgetSaveList) throws Exception {
        return new ResponseEntity<>(costBudgetService.doSave(costBudgetSaveList), HttpStatus.OK);
    }

    /*비용 예산 삭제*/
    @PostMapping("/delete")
    public ResponseEntity<String> doDelete(@RequestBody CostBudgetSaveList costBudgetSaveList) throws Exception {
        return new ResponseEntity<>(costBudgetService.doDelete(costBudgetSaveList), HttpStatus.OK);
    }

    /*비용 예산 관리 기안 조회*/
    @PostMapping("/pop/draft/list")
    public ResponseEntity<DraftListResult> getDraftList(@RequestBody CostBudgetDto costBudgetDto) throws Exception {
        return new ResponseEntity<>(costBudgetService.getDraftList(costBudgetDto), HttpStatus.OK);
    }

    /*비용 예산 관리 기안 저장*/
    @PostMapping("/pop/draft/save")
    public ResponseEntity<CostBudgetDto> doDraftSave(@RequestBody DraftResult draftResult) throws Exception {
        return new ResponseEntity<>(costBudgetService.doDraftSave(draftResult), HttpStatus.OK);
    }

    /*비용 예산 관리 기안 삭제*/
    @PostMapping("/pop/draft/delete")
    public ResponseEntity<String> doDraftDelete(@RequestBody CostBudgetDto costBudgetDto) throws Exception {
        return new ResponseEntity<>(costBudgetService.doDraftDelete(costBudgetDto), HttpStatus.OK);
    }


    /*비용 예산 관리 제품군 팝업 조회 */
    @PostMapping("/pop/item/list")
    public ResponseEntity<List<ItemDto>> getBudgetItemList(@RequestBody CostBudgetDto costBudgetDto){
        return new ResponseEntity<>(costBudgetService.getBudgetItemList(costBudgetDto), HttpStatus.OK);
    }


    /*비용 예산 관리 프로젝트 팝업 조회 */
    @PostMapping("/pop/project/list")
    public ResponseEntity<List<ProjectDto>> getBudgetProjectList(@RequestBody CostBudgetDto costBudgetDto){
        return new ResponseEntity<>(costBudgetService.getBudgetProjectList(costBudgetDto), HttpStatus.OK);
    }

    /*비용 예산 관리 계정 팝업 조회 */
    @PostMapping("/pop/acct/list")
    public ResponseEntity<List<AccountDto>> getAccountPopList(@RequestBody CostBudgetDto costBudgetDto){
        List<AccountDto> list = costBudgetService.getAccountPopList(costBudgetDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
